import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

class CodeFactory {
	private static int tempCount;
	private static ArrayList<String> intVariablesList;
	private static ArrayList<String> boolVariablesList;
	private static HashMap<String, Integer> assignedVariables;

	private static int labelCount = 0;
	public static boolean firstWrite = true;

	public CodeFactory() {
		tempCount = 0;
		intVariablesList = new ArrayList<String>();
		boolVariablesList = new ArrayList<String>();
		assignedVariables = new HashMap<>();
		    
		String testcase = Parser.filename;
		String outputFile = "AssemblyCode/" + testcase.substring(0, testcase.length() - 4) + ".s";		
		
		// Create Assembly File
		try {
	    	File file = new File(outputFile);
	    	FileOutputStream FOS = new FileOutputStream(file);
	    	PrintStream out = new PrintStream(FOS);
	    	System.setOut(out);
		} catch (FileNotFoundException e) {
			System.out.print("Error creating file.");
			// e.printStackTrace();
		}
	}
	
	void generateDeclaration( Expression variable ) {
		String var = variable.expressionName;
		int type = variable.expressionValueType;
		
		//System.out.println(type);
		if ( type == Token.INTLITERAL ) {
			intVariablesList.add(var);
		} else if ( type == Token.BOOL ) {
			boolVariablesList.add(var);
		}
	}
	

	Expression generateMathExpr(Expression left, Expression right, Operation op) {
		Expression tempExpr = new Expression(Expression.TEMPEXPR, createIntTempName());
				
		// Process left side of expression
		if (left.expressionType == Expression.LITERALEXPR) {
			System.out.println("\tMOVL " + "$" + left.expressionName + ", %eax");
		} else {
			System.out.println("\tMOVL " + left.expressionName + ", %eax");
		}
		
		// Process right side of expression
		if (right.expressionType == Expression.LITERALEXPR) {
			System.out.println("\tMOVL " + "$" + right.expressionName + ", %ebx");
		} else {
			System.out.println("\tMOVL " + right.expressionName + ", %ebx");
		}
		
		
		if (op.opType == Token.PLUS) {
			// ADDITION
			System.out.println("\tADD %ebx, %eax");
		} else if (op.opType == Token.MINUS) {
			// SUBTRACTION
			System.out.println("\tSUB %ebx, %eax");
		} else if (op.opType == Token.MULT) {
			// MULTIPLICATION
			System.out.println("\tIMULL %ebx");
		} else if (op.opType == Token.DIV || op.opType == Token.MOD) {
			// DIVISION & MODULO
			System.out.println("\tIDIVL %ebx");
		} 
		
		if (op.opType == Token.MOD) {
			// Modulo takes from the remainder register, EDX
			System.out.println("\tMOVL " + "%edx, " + tempExpr.expressionName);
		} else {
			System.out.println("\tMOVL " + "%eax, " + tempExpr.expressionName);
		}
		
		return tempExpr;
	}
	
	
	
	
	/* Generate OR / AND boolean expressions */
	Expression generateBoolExpr(Expression left, Expression right, Operation op) {
		Expression tempExpr = new Expression(Expression.TEMPEXPR, createBoolTempName());

		//if (left.expressionName)
		System.out.println("\t/* Clear out EAX and EBX registers */");
		System.out.println("\tXORL %eax, %eax");
		System.out.println("\tXORL %ebx, %ebx");
		
		// Process left expression
		if (left.expressionType == Expression.LITERALEXPR) {
			System.out.println("\tMOVL $" + left.expressionIntValue + ", %eax");
		} else {
			System.out.println("\tMOVL " + left.expressionName + ", %eax");
		}
		
		if (left.NOTflag) {
			System.out.println("\t/* Negate and increment to enforce the NOT */");
			System.out.println("\tNEG  %eax");
			System.out.println("\tINC  %eax");
		} 
		
		// Process right expression
		System.out.println("\tMOVL $" + right.expressionIntValue + ", %ebx");
		
		if (right.NOTflag) {
			System.out.println("\t/* Negate and increment to enforce the NOT */");
			System.out.println("\tNEG  %ebx");
			System.out.println("\tINC  %ebx");
		}
		
		// Process operation
		if (op.opType == Token.AND) {
			System.out.println("\t/* Generate AND expression */");
			System.out.println("\tANDL %ebx, %eax");	
		} else if (op.opType == Token.OR) {
			System.out.println("\t/* Generate OR expression */");
			System.out.println("\tORL %ebx, %eax");
		}
		
		System.out.println("\tMOVL " + "%eax, " + tempExpr.expressionName);
		return tempExpr;
	}
	
	
	
	
	void generateWrite(Expression expr) {
		switch (expr.expressionType) {
		case Expression.IDEXPR:
			case Expression.TEMPEXPR: {
				generateAssemblyCodeForWriting(expr.expressionName);
				break;
			}
			case Expression.LITERALEXPR: {
				generateAssemblyCodeForWriting("$" + expr.expressionName);
			}
		}
	}

	private void generateAssemblyCodeForWriting(String idName) {
		if (!firstWrite) {
			System.out.println("\t/* Clear out registers */");
			System.out.println("\tXORL %eax, %eax");
			System.out.println("\tXORL %ebx, %ebx");
			System.out.println("\tXORL %ecx, %ecx");
			System.out.println("\tXORL %edx, %edx");
			System.out.println("");
			
			System.out.println("\tmovl " + idName + ",%eax");
			System.out.println("\tpushl %eax");
			System.out.println("\tcall __reversePrint    /* The return address is at top of stack! */");
			System.out.println("\tpopl  %eax    /* Remove value pushed onto the stack */");
			
		} else
		{
			firstWrite = false;
			
			System.out.println("\tmovl " + idName + ",%eax");
			
			String nonzeroPrintLabel = generateLabel("__nonzeroPrint");
			System.out.println("\tcmpl $0, %eax");
			System.out.println("jne " + nonzeroPrintLabel);
			System.out.println("\tpush $'0'");
			System.out.println("\tmovl $4, %eax       /* The system call for write (sys_write) */");
			System.out.println("\tmovl $1, %ebx       /* File descriptor 1 - standard output */");
			System.out.println("\tmovl $1, %edx     /* Place number of characters to display */");
			System.out.println("\tleal (%esp), %ecx   /* Put effective address of zero into ecx */");
			System.out.println("\tint $0x80	    /* Call to the Linux OS */");
			System.out.println("popl %eax");
			System.out.println("\tjmp __writeExit   /* Needed to jump over the reversePrint code since we printed the zero */ ");
			System.out.println(nonzeroPrintLabel + ":");
			System.out.println("\tpushl %eax");
			System.out.println("\tcall __reversePrint    /* The return address is at top of stack! */");
			System.out.println("\tpopl  %eax    /* Remove value pushed onto the stack */");
			System.out.println("\tjmp __writeExit");  /* Needed to jump over the reversePrint code since it was called */
			
			System.out.println("__reversePrint: ");
			System.out.println("\t/* Save registers this method modifies */");
			System.out.println("\tpushl %eax");
			System.out.println("\tpushl %edx");
			System.out.println("\tpushl %ecx");
			System.out.println("\tpushl %ebx");

			System.out.println("\tcmpw $0, 20(%esp)");
			System.out.println("\tjge __positive");
			System.out.println("\t/* Display minus on console */");
			System.out.println("\tmovl $4, %eax       /* The system call for write (sys_write) */");
			System.out.println("\tmovl $1, %ebx       /* File descriptor 1 - standard output */");
			System.out.println("\tmovl $1, %edx     /* Place number of characters to display */");
			System.out.println("\tmovl $__minus, %ecx   /* Put effective address of stack into ecx */");
			System.out.println("\tint $0x80	    /* Call to the Linux OS */");
			
			System.out.println("\t__positive:");
			System.out.println("\txorl %eax, %eax       /* eax = 0 */");
			System.out.println("\txorl %ecx, %ecx       /* ecx = 0, to track characters printed */");

			System.out.println("\t/** Skip 16-bytes of register data stored on stack and 4 bytes");
			System.out.println("\tof return address to get to first parameter on stack ");
			System.out.println("\t*/   ");
			System.out.println("\tmovw 20(%esp), %ax     /* ax = parameter on stack */");

			System.out.println("\tcmpw $0, %ax");
			System.out.println("\tjge __reverseLoop");
			System.out.println("\tmulw __negOne\n");
			
			System.out.println("__reverseLoop:");

			System.out.println("\tcmpw $0, %ax");
			System.out.println("\tje   __reverseExit");
			System.out.println("\t/* Do div and mod operations */");
			System.out.println("\tmovl $10, %ebx         /* ebx = 10 as divisor  */");
			System.out.println("\txorl %edx, %edx        /* edx = 0 to get remainder */");
			System.out.println("\tidivl %ebx             /* edx = eax % 10, eax /= 10 */");
			System.out.println("\taddb $'0', %dl         /* convert 0..9 to '0'..'9'  */");

			System.out.println("\tdecl %esp              /* use stack to store digit  */");
			System.out.println("\tmovb %dl, (%esp)       /* Save character on stack.  */");
			System.out.println("\tincl %ecx              /* track number of digits.   */");

			System.out.println("\tjmp __reverseLoop");

			System.out.println("__reverseExit:");

			System.out.println("__printReverse:");

			System.out.println("\t/* Display characters on _stack_ on console */");

			System.out.println("\tmovl $4, %eax       /* The system call for write (sys_write) */");
			System.out.println("\tmovl $1, %ebx       /* File descriptor 1 - standard output */");
			System.out.println("\tmovl %ecx, %edx     /* Place number of characters to display */");
			System.out.println("\tleal (%esp), %ecx   /* Put effective address of stack into ecx */");
			System.out.println("\tint $0x80	    /* Call to the Linux OS */");

			System.out.println("\t /* Clean up data and registers on the stack */");
			System.out.println("\taddl %edx, %esp");
			System.out.println("\tpopl %ebx");
			System.out.println("\tpopl %ecx");
			System.out.println("\tpopl %edx");
			System.out.println("\t popl %eax");

			System.out.println("\tret");
			System.out.println("__writeExit:");
		}
	}

	void generateRead(Expression expr) {
		switch (expr.expressionType) {
			case Expression.IDEXPR:
			case Expression.TEMPEXPR: {
				generateAssemblyCodeForReading(expr.expressionName);
				break;
			}
			case Expression.LITERALEXPR: {
				// not possible since you cannot read into a literal. An error
				// should be generated
			}
		}
	}

	private void generateAssemblyCodeForReading(String idName) {
		
		String readLoopLabel = generateLabel("__readLoop");
		String readLoopEndLabel = generateLabel("__readLoopEnd");
		String readEndLabel = generateLabel("__readEnd");
		String readPositiveLabel = generateLabel("__readPositive");
		
		System.out.println("\tmovl $0, " + idName);
		
		System.out.println("\tmovl %esp, %ebp");
		System.out.println("\t/* read first character to check for negative */");
		System.out.println("\tmovl $3, %eax        /* The system call for read (sys_read) */");
		System.out.println("\tmovl $0, %ebx        /* File descriptor 0 - standard input */");
		System.out.println("\tlea 4(%ebp), %ecx      /* Put the address of character in a buffer */");
		System.out.println("\tmovl $1, %edx        /* Place number of characters to read in edx */");
		System.out.println("\tint $0x80	     /* Call to the Linux OS */ ");
		System.out.println("\tmovb 4(%ebp), %al");
		System.out.println("\tcmpb $'\\n', %al      /* Is the newline character? */");
		System.out.println("\tje  " + readEndLabel);
		System.out.println("\tcmpb $'-', %al		/* Is the character '-'? */");
		System.out.println("\tjne " + readPositiveLabel);
		
		System.out.println("\tmovb $'-', __negFlag	");
		System.out.println("\tjmp " + readLoopLabel);
		
		
		System.out.println(readPositiveLabel + ":");
		System.out.println("\tcmpb $'+', %al");
		System.out.println("\tje " + readLoopLabel);
		System.out.println("\t/*Process the first digit that is not a minnus or newline.*/");
		System.out.println("\tsubb $'0', 4(%ebp)      /* Convert '0'..'9' to 0..9 */ \n");

		System.out.println("\t/* result  = (result * 10) + (idName  - '0') */");
		System.out.println("\tmovl $10, %eax");
		System.out.println("\txorl %edx, %edx");
		System.out.println("\tmull " + idName + "        /* result  *= 10 */");
		System.out.println("\txorl %ebx, %ebx    /* ebx = (int) idName */");
		System.out.println("\tmovb 4(%ebp), %bl");
		System.out.println("\taddl %ebx, %eax    /* eax += idName */");
		System.out.println("\tmovl %eax, " + idName);
		
		
		System.out.println(readLoopLabel + ":");
		System.out.println("\tmovl $3, %eax        /* The system call for read (sys_read) */");
		System.out.println("\tmovl $0, %ebx        /* File descriptor 0 - standard input */");
		System.out.println("\tlea 4(%ebp), %ecx      /* Put the address of character in a buffer */");
		System.out.println("\tmovl $1, %edx        /* Place number of characters to read in edx */");
		System.out.println("\tint $0x80	     /* Call to the Linux OS */ \n");

		System.out.println("\tmovb 4(%ebp), %al");
		System.out.println("\tcmpb $'\\n', %al      /* Is the character '\\n'? */");

		
		System.out.println("\tje  " + readLoopEndLabel);
		System.out.println("\tsubb $'0', 4(%ebp)      /* Convert '0'..'9' to 0..9 */ \n");

		System.out.println("\t/* result  = (result * 10) + (idName  - '0') */");
		System.out.println("\tmovl $10, %eax");
		System.out.println("\txorl %edx, %edx");
		System.out.println("\tmull " + idName + "        /* result  *= 10 */");
		System.out.println("\txorl %ebx, %ebx    /* ebx = (int) idName */");
		System.out.println("\tmovb 4(%ebp), %bl");
		System.out.println("\taddl %ebx, %eax    /* eax += idName */");
		System.out.println("\tmovl %eax, " + idName);
		System.out.println("\t/* Read the next character */");
		System.out.println("\tjmp " + readLoopLabel);
		System.out.println(readLoopEndLabel + ":\n");
		System.out.println("\tcmpb $'-', __negFlag");
		System.out.println("\tjne " + readEndLabel);
		System.out.println("\tmovl " + readEndLabel + ", %eax");
		System.out.println("\tmull __negOne");
		System.out.println("\tmovl %eax, " + readEndLabel);
		System.out.println("\tmovb $'+', __negFlag");
		System.out.println(readEndLabel + ":\n");

	}

	private String generateLabel(String start) {
		String label = start + labelCount++;
		return label;
	}

	
	void generateAssignment(Expression lValue, Expression expr) {
		System.out.println("\t/* Clear out EAX and EBX registers */");
		System.out.println("\tXORL %eax, %eax");
		System.out.println("\tXORL %ebx, %ebx");
		System.out.println("");
		
		if (expr.expressionType == Expression.LITERALEXPR && expr.expressionValueType != Token.BOOL ) {
			System.out.println("\tMOVL " + "$" + expr.expressionIntValue + ", %eax");
			System.out.println("\tMOVL %eax, " + lValue.expressionName);
			
		} else {
			System.out.println("\tMOVL " + expr.expressionName + ", %eax");	
			
			if (expr.NOTflag) {
				System.out.println("\tNEG  %eax");
				System.out.println("\tINC  %eax");	
			}
			
			System.out.println("\tMOVL %eax, " + lValue.expressionName);
		}
	}

	
	
	void generateInitAssignment(Expression lValue, Expression expr) {
		String var = lValue.expressionName;
		int type = expr.expressionValueType;
		int value = expr.expressionIntValue; // new Integer( expr.expressionName ).intValue();
		
		if ( type == Token.INTLITERAL ) {
			intVariablesList.add(var);
			assignedVariables.put(var, value);
		} else if ( type == Token.BOOL ) {
			boolVariablesList.add(var);
			assignedVariables.put(var, value);
		} else {
			// Error			
		}
	}
	
	void generateStart() {
		System.out.println(".text\n.global _start\n\n_start:\n");
	}

	void generateExit() {
		System.out.println("exit:");
		System.out.println("\tmov $1, %eax");
		System.out.println("\tmov $1, %ebx");
		System.out.println("\tint $0x80");
	}

	public void generateData() {
		System.out.println("\n\n.data");
		
		for (String var : intVariablesList) {	
			if (assignedVariables.get(var) == null) {
				System.out.println(var + ":\t.int 0");
			} else {
				System.out.println(var + ":\t.int " + assignedVariables.get(var));
			}
		}
			
		//TODO: Distinguish between bool and int
		for (String var : boolVariablesList) {						
			if (assignedVariables.get(var) == null) {
				System.out.println(var + ":\t.int 0");
			} else {
				System.out.println(var + ":\t.int " + assignedVariables.get(var));
			}
		}
			
		System.out.println("");
		System.out.println("__minus:  .byte '-'");
		System.out.println("__negOne: .int -1");
		System.out.println("__negFlag: .byte '+'");
	}

	private String createIntTempName() {
		String tempVar = new String("temp" + tempCount++);
		intVariablesList.add(tempVar);
		return tempVar;
	}

	private String createBoolTempName() {
		String tempVar = new String("temp" + tempCount++);
		boolVariablesList.add(tempVar);
		return tempVar;
	}
}
