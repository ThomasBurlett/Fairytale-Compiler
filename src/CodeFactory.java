import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

class CodeFactory {
	private static int tempCount;
	private static int loopCount;
	private static int ifCount;
	public static int orCount;
	public static int andCount;
	public static ArrayList<String> intVariablesList;
	public static ArrayList<String> boolVariablesList;
	private static HashMap<String, Integer> assignedVariables;

	private static int labelCount = 0;
	public static boolean firstWrite;

	public CodeFactory() {
		tempCount = 0;
		loopCount = 0;
		ifCount = 0;
		orCount = 0;
		andCount = 0;
		firstWrite = true;
		
		intVariablesList = new ArrayList<String>();
		boolVariablesList = new ArrayList<String>();
		assignedVariables = new HashMap<>();
		    
		// Uncomment for JUnit
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
			System.out.println("\tADDL %ebx, %eax");
		} else if (op.opType == Token.MINUS) {
			// SUBTRACTION
			System.out.println("\tSUBL %ebx, %eax");
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
		
		System.out.println("");
		
		return tempExpr;
	}
	
	/* Generate OR / AND boolean expressions */
	Expression generateBoolExpr(Expression left, Expression right, Operation op) {
		Expression tempExpr = new Expression(Expression.TEMPEXPR, createBoolTempName());

		//if (left.expressionName)
		System.out.println("\t/* Clear out EAX and EBX registers */");
		System.out.println("\tXORL %eax, %eax");
		System.out.println("\tXORL %ebx, %ebx \n");
		
		// Process left expression
//		if (left.expressionType == Expression.LITERALEXPR) {
//			System.out.println("\tMOVL $" + left.expressionIntValue + ", %eax");
//		} else {
		System.out.println("\tMOVL " + left.expressionName + ", %eax");
		//}
		
		if (left.NOTflag) {
			System.out.println("\t/* Negate and increment to enforce the NOT */");
			System.out.println("\tNEG  %eax");
			System.out.println("\tINC  %eax");
		} 
		
		// Process right expression
		System.out.println("\tMOVL " + right.expressionName + ", %ebx");
		
		if (right.NOTflag) {
			System.out.println("\t/* Negate and increment to enforce the NOT */");
			System.out.println("\tNEG  %ebx");
			System.out.println("\tINC  %ebx");
		}
		
		// Process operation
		if (op.opType == Token.AND) {
			System.out.println("\t/* Generate AND expression */");
			System.out.println("\tANDL %ebx, %eax");	
			System.out.println("\tMOVL " + "%eax, " + tempExpr.expressionName + "\n");
			//System.out.println("\tJMP OR_" + orCount);
		} else if (op.opType == Token.OR) {
			System.out.println("\t/* Generate OR expression */");
			System.out.println("\tORL %ebx, %eax");
			System.out.println("\tMOVL " + "%eax, " + tempExpr.expressionName + "\n");			
		}

		return tempExpr;
	}
	
	/* LOOP # */
	static void printDeLoop(int count) {
		System.out.println("\tADDL $1, LpCt_" + count + "");		
		System.out.println("\tJMP  LOOP_" + count + "\n");		

		System.out.println("DELOOP_" + count + ":");		
	}
	
	public static Expression comparisonStatement(Expression left, Operation op, Expression right) {
		Expression tempExpr = new Expression(Expression.TEMPEXPR, createBoolTempName());
		
		// Clear out EAX and EBX
		System.out.println("\t/* Clear out EAX, EBX */");
		System.out.println("\tXORL %eax, %eax");
		System.out.println("\tXORL %ebx, %ebx \n");

		System.out.println("\t/* Set " + tempExpr.expressionName + " to be 0 */");
		System.out.println("\tMOVL $0, %eax");
		System.out.println("\tMOVL %eax, " + tempExpr.expressionName + "\n");
		
		System.out.println("\t/* Compare the variables */");

		// Put left into EAX 
		if (left.expressionType == Expression.LITERALEXPR) {
			System.out.println("\tMOVL $" + left.expressionIntValue + ", %eax");
		} else {
			System.out.println("\tMOVL " + left.expressionName + ", %eax");
		}
		
		// Put right into EBX
		if (right.expressionType == Expression.LITERALEXPR) {
			System.out.println("\tMOVL $" + right.expressionIntValue + ", %ebx");
		} else {
			System.out.println("\tMOVL " + right.expressionName + ", %ebx");
		}
		
		System.out.println("\tCMP  %ebx, %eax");
		System.out.println("\t" + op.jump + "  _" + tempExpr.expressionName);
		
		// If true, move 1 into tempExpr
		System.out.println("\n\t/* Set " + tempExpr.expressionName + " to True */");
		System.out.println("\tXORL %ecx, %ecx");
		System.out.println("\tMOVL $1, %ecx");
		System.out.println("\tMOVL %ecx, " + tempExpr.expressionName);
		System.out.println("\tXORL %ecx, %ecx");
			 	
		// Else, tempExpr will stay 0
		System.out.println("_" + tempExpr.expressionName + ": ");
		
	 	// System.out.println("\tJMP AND_" + andCount + "\n");
		return tempExpr;
	}
	
	static Expression generateBeginIntLoop(Expression expr, int count) {
		String loopVar = "LpCt_" + Integer.toString(count);
		Expression loopExpr = new Expression(Expression.TEMPEXPR, loopVar);
		
		intVariablesList.add(loopVar); // lpCt_# = 0
		
		// Clear out EAX
		System.out.println("\t/* Clear out EAX */");
		System.out.println("\tXORL %eax, %eax");
		
		// Put LpCt into EAX
		System.out.println("\tMOVL " + loopVar + ", %eax");
		
		// Compare expression value to LpCt
		if (expr.expressionType == Expression.LITERALEXPR) {
			System.out.println("\tCMP $" + expr.expressionIntValue + ", %eax");
		} else {
			System.out.println("\tCMP " + expr.expressionName + ", %eax");
		}
		
		// Quit loop
		System.out.println("\tJE  DELOOP_" + count);

		return loopExpr;
	}
	
	
	
	void generateWrite(Expression expr) {
		switch (expr.expressionType) {
			case Expression.IDEXPR: {
				generateAssemblyCodeForWriting(expr.expressionName);
				break;
			}
			case Expression.TEMPEXPR: {
				generateAssemblyCodeForWriting(expr.expressionName);
				break;
			}
			case Expression.LITERALEXPR: {
				generateAssemblyCodeForWriting("$" + expr.expressionName);
				break;
			}
		}
	}

	private void generateAssemblyCodeForWriting(String idName) {
		if (!firstWrite) {
			System.out.println("\n\t/* Clear out registers */");
			System.out.println("\tXORL %eax, %eax");
			System.out.println("\tXORL %ebx, %ebx");
			System.out.println("\tXORL %ecx, %ecx");
			System.out.println("\tXORL %edx, %edx");
			System.out.println("");
			
			System.out.println("\tMOVL " + idName + ",%eax");
			System.out.println("\tPUSHL %eax");
			System.out.println("\tCALL __reversePrint	/* The return address is at top of stack! */");
			System.out.println("\tPOPL  %eax			/* Remove value pushed onto the stack */");
			
		} else
		{
			firstWrite = false;
			
			System.out.println("\tMOVL " + idName + ",%eax");
			
			String nonzeroPrintLabel = generateLabel("__nonzeroPrint");
			System.out.println("\tCMPL $0, %eax");
			System.out.println("\tJNE " + nonzeroPrintLabel);
			System.out.println("\tPUSH $'0'");
			System.out.println("\tMOVL $4, %eax			/* The system call for write (sys_write) */");
			System.out.println("\tMOVL $1, %ebx			/* File descriptor 1 - standard output */");
			System.out.println("\tMOVL $1, %edx			/* Place number of characters to display */");
			System.out.println("\tLEAL (%esp), %ecx		/* Put effective address of zero into ecx */");
			System.out.println("\tINT $0x80				/* Call to the Linux OS */");
			System.out.println("\tPOPL %eax");
			System.out.println("\tJMP __writeExit		/* Needed to jump over the reversePrint code since we printed the zero */ ");
			System.out.println(nonzeroPrintLabel + ":");
			System.out.println("\tPUSHL %eax");
			System.out.println("\tCALL __reversePrint	/* The return address is at top of stack! */");
			System.out.println("\tPOPL  %eax			/* Remove value pushed onto the stack */");
			System.out.println("\tJMP __writeExit		/* Needed to jump over the reversePrint code since it was called */");
			
			System.out.println("__reversePrint: ");
			System.out.println("\t/* Save registers this method modifies */");
			System.out.println("\tPUSHL %eax");
			System.out.println("\tPUSHL %edx");
			System.out.println("\tPUSHL %ecx");
			System.out.println("\tPUSHL %ebx");

			System.out.println("\tCMPW $0, 20(%esp)");
			System.out.println("\tJGE __positive");
			System.out.println("\t/* Display minus on console */");
			System.out.println("\tMOVL $4, %eax			/* The system call for write (sys_write) */");
			System.out.println("\tMOVL $1, %ebx			/* File descriptor 1 - standard output */");
			System.out.println("\tMOVL $1, %edx			/* Place number of characters to display */");
			System.out.println("\tMOVL $__minus, %ecx	/* Put effective address of stack into ecx */");
			System.out.println("\tINT $0x80				/* Call to the Linux OS */");
			
			System.out.println("__positive:");
			System.out.println("\tXORL %eax, %eax       /* eax = 0 */");
			System.out.println("\tXORL %ecx, %ecx       /* ecx = 0, to track characters printed */");

			System.out.println("\t/** Skip 16-bytes of register data stored on stack and 4 bytes");
			System.out.println("\t *  of return address to get to first parameter on stack ");
			System.out.println("\t */   ");
			System.out.println("\tMOVW 20(%esp), %ax     /* ax = parameter on stack */");

			System.out.println("\tCMPW $0, %ax");
			System.out.println("\tJGE  __reverseLoop");
			System.out.println("\tMULW __negOne\n");
			
			System.out.println("__reverseLoop:");

			System.out.println("\tCMPW $0, %ax");
			System.out.println("\tJE   __reverseExit");
			System.out.println("\t/* Do div and mod operations */");
			System.out.println("\tMOVL $10, %ebx		/* ebx = 10 as divisor  */");
			System.out.println("\tXORL %edx, %edx		/* edx = 0 to get remainder */");
			System.out.println("\tIDIVL %ebx			/* edx = eax % 10, eax /= 10 */");
			System.out.println("\tADDB $'0', %dl		/* convert 0..9 to '0'..'9'  */");

			System.out.println("\tDECL %esp				/* use stack to store digit  */");
			System.out.println("\tMOVB %dl, (%esp)		/* Save character on stack.  */");
			System.out.println("\tINCL %ecx 			/* track number of digits.   */");

			System.out.println("\tJMP __reverseLoop \n");

			System.out.println("__reverseExit:");

			System.out.println("__printReverse:");

			System.out.println("\t/* Display characters on _stack_ on console */");

			System.out.println("\tMOVL $4, %eax			/* The system call for write (sys_write) */");
			System.out.println("\tMOVL $1, %ebx			/* File descriptor 1 - standard output */");
			System.out.println("\tMOVL %ecx, %edx		/* Place number of characters to display */");
			System.out.println("\tLEAL (%esp), %ecx		/* Put effective address of stack into ecx */");
			System.out.println("\tINT $0x80				/* Call to the Linux OS */");

			System.out.println("\t/* Clean up data and registers on the stack */");
			System.out.println("\tADDL %edx, %esp");
			System.out.println("\tPOPL %ebx");
			System.out.println("\tPOPL %ecx");
			System.out.println("\tPOPL %edx");
			System.out.println("\tPOPL %eax");

			System.out.println("\tRET \n");
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
				System.out.println("ERROR - Cannot read into a literal.");
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
		System.out.println("\tmovl $3, %eax         /* The system call for read (sys_read) */");
		System.out.println("\tmovl $0, %ebx         /* File descriptor 0 - standard input */");
		System.out.println("\tlea 4(%ebp), %ecx     /* Put the address of character in a buffer */");
		System.out.println("\tmovl $1, %edx         /* Place number of characters to read in edx */");
		System.out.println("\tint $0x80	     		/* Call to the Linux OS */ ");
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
		System.out.println("\tsubb $'0', 4(%ebp)     /* Convert '0'..'9' to 0..9 */ \n");

		System.out.println("\t/* result  = (result * 10) + (idName  - '0') */");
		System.out.println("\tmovl $10, %eax");
		System.out.println("\txorl %edx, %edx");
		System.out.println("\tmull " + idName + "    /* result  *= 10 */");
		System.out.println("\txorl %ebx, %ebx    	 /* ebx = (int) idName */");
		System.out.println("\tmovb 4(%ebp), %bl");
		System.out.println("\taddl %ebx, %eax    	 /* eax += idName */");
		System.out.println("\tmovl %eax, " + idName);
		
		
		System.out.println(readLoopLabel + ":");
		System.out.println("\tmovl $3, %eax        	/* The system call for read (sys_read) */");
		System.out.println("\tmovl $0, %ebx        	/* File descriptor 0 - standard input */");
		System.out.println("\tlea 4(%ebp), %ecx    	/* Put the address of character in a buffer */");
		System.out.println("\tmovl $1, %edx        	/* Place number of characters to read in edx */");
		System.out.println("\tint $0x80	     		/* Call to the Linux OS */ \n");

		System.out.println("\tmovb 4(%ebp), %al");
		System.out.println("\tcmpb $'\\n', %al      /* Is the character '\\n'? */");

		
		System.out.println("\tje  " + readLoopEndLabel);
		System.out.println("\tsubb $'0', 4(%ebp)    /* Convert '0'..'9' to 0..9 */ \n");

		System.out.println("\t/* result  = (result * 10) + (idName  - '0') */");
		System.out.println("\tmovl $10, %eax");
		System.out.println("\txorl %edx, %edx");
		System.out.println("\tmull " + idName + "  	/* result  *= 10 */");
		System.out.println("\txorl %ebx, %ebx    	/* ebx = (int) idName */");
		System.out.println("\tmovb 4(%ebp), %bl");
		System.out.println("\taddl %ebx, %eax    	/* eax += idName */");
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
			System.out.println("ERROR - Incorrect data type.");
		}
	}
	
	
	void generateStart() {
		System.out.println(".text\n.global _start\n\n_start:\n");
	}
	

	void generateExit() {
		System.out.println("\nexit:");
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
			
		for (String var : boolVariablesList) {						
			if (assignedVariables.get(var) == null) {
				System.out.println(var + ":\t.int 0");
			} else {
				System.out.println(var + ":\t.int " + assignedVariables.get(var));
			}
		}
			
		System.out.println("");
		System.out.println("__minus:   .byte '-'");
		System.out.println("__negOne:  .int  -1");
		System.out.println("__negFlag: .byte '+'");
	}

	private String createIntTempName() {
		String tempVar = new String("temp" + tempCount++);
		intVariablesList.add(tempVar);
		return tempVar;
	}

	private static String createBoolTempName() {
		String tempVar = new String("temp" + tempCount++);
		boolVariablesList.add(tempVar);
		return tempVar;
	}
	
	public static int createLoopCount() {
		return loopCount++;
	}
	
	public static int createIfCount() {
		return ifCount++;
	}
}