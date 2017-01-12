import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

class CodeFactory {
	private static int tempCount;
	//private static ArrayList<String> variablesList;
	//private static ArrayList<String> stringList;
	//private static ArrayList<String> assign;
	//private static ArrayList<String> stringListAssign;
	//private static ArrayList<String> variables;
	//private static ArrayList<String> variablesAssign;
	private static HashMap<String, String> integers;
	private static HashMap<String, String> strings;
	private static ArrayList<String> strs;
	private static ArrayList<String> ints;
	private static int labelCount = 0;
	private static boolean firstWrite = true;

	public CodeFactory() {
		tempCount = 0;
		//variablesList = new ArrayList<String>();			// Create list of variables
		//stringList = new ArrayList<String>();	// Create list of strings
		//assign = new ArrayList<String>();
		//stringListAssign = new ArrayList<String>();
		//variables = new ArrayList<String>();
		//variablesAssign = new ArrayList<String>();
		integers = new HashMap<>();
		strings = new HashMap<>();
		strs = new ArrayList<>();
		ints = new ArrayList<>();
	}

	void generateDeclaration(int dataType, Token token) {
		if (dataType == Token.STRINGDT) {
			// stringList.add(token.getId());
			// If a string is undeclared
			if (strings.get(token.getId()) == null){
				strings.put(token.getId(), "");
				strs.add(token.getId());
			}
		} else {
			// variablesList.add(token.getId());
			if (integers.get(token.getId()) == null){
				integers.put(token.getId(), "0");
				ints.add(token.getId());
			}
		}
	}

	Expression generateArithExpr(Expression left, Expression right, Operation op) {
		Expression tempExpr = new Expression(Expression.TEMPEXPR, createTempName());
				
		if ( ints.contains(right.expressionName) || right.expressionType == Expression.LITERALEXPR) {
			System.out.println("\tMOVL " + right.expressionName + ", %ebx");
		} else {
			System.out.println("\tMOVL " + "$" + right.expressionName + ", %ebx");
		}
		
		if ( ints.contains(left.expressionName) || left.expressionType == Expression.LITERALEXPR) {
			System.out.println("\tMOVL " + left.expressionName + ", %eax");
		} else {
			System.out.println("\tMOVL " + "$" + left.expressionName + ", %eax");
		}
		
		if (op.opType == Token.PLUS) {
			System.out.println("\tADD %ebx, %eax");
		} else if (op.opType == Token.MINUS) {
			System.out.println("\tSUB %ebx, %eax");
		}
		
		System.out.println("\tMOVL " + "%eax, " + tempExpr.expressionName);
		return tempExpr;
	}

	void generateWrite(Expression expr) {
		switch (expr.expressionType) {
		//case Expression.IDEXPR:
		case 5: {
			generateAssemblyCodeForWriting(expr.expressionName);
			break;
		}
		default: {
			generateAssemblyCodeForWriting("$" + expr.expressionName);
		}
		}
	}

	private void generateAssemblyCodeForWriting(String idName) {
		if (!firstWrite) {
			
			System.out.println("\tmovl " + idName + ",%eax");
			System.out.println("\tpushl %eax");
			System.out.println("\tcall __reversePrint    /* The return address is at top of stack! */");
			System.out.println("\tpopl  %eax    /* Remove value pushed onto the stack */");
			
		} else
		// String reverseLoopLabel = generateLabel("reverseLoop");
		{
			firstWrite = false;
			
			System.out.println("\tmovl " + idName + ",%eax");
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
	
	void generateStringWrite(Expression expr) {
		int length = expr.expressionName.length();
		String str = expr.expressionName;
		
		System.out.println("\txorl %edx, %edx");
		System.out.println("\tmov $4, %eax");
		System.out.println("\tmov $1, %ecx");
		System.out.println("\tmov " + str + ", %ecx");
		System.out.println("\tmov $" + length + ", %edx");
		System.out.println("\t int $0x80");
	}
	

	void generateRead(Expression expr) {
		boolean str = true; // Assume string
		
		// If in integers table, switch to integer
		if (integers.get(expr.expressionName) != null)
			str = false;
		
		if (!str) {
			generateAssemblyCodeForReading(expr.expressionName);
		} else {
			generateAssemblyCodeForStringReading(expr.expressionName); 
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
		System.out.println("\tmovl a, %eax");
		System.out.println("\tmull __negOne");
		System.out.println("\tmovl %eax, a");
		System.out.println("\tmovb $'+', __negFlag");
		System.out.println(readEndLabel + ":\n");
	}

	private void generateAssemblyCodeForStringReading(String idName) {		
		System.out.println("\tmovl $3, %eax \t /* The system call for read (sys_read) */ ");
		System.out.println("\tmovl $0, %ebx \t /* File descriptor 0 - standard input */ ");
		System.out.println("\tmovl $" + idName + ", %ecx \t /* Put the character in a buffer */ ");
		System.out.println("\tmovl $256, %edx \t /* Can read in a maximum of 256 characters */ ");
		System.out.println("\tint  $0x80 \t /* Call to the Linux OS */ \n");
	}

	private String generateLabel(String start) {
		String label = start + labelCount++;
		return label;
	}

	void generateAssignment(Expression lValue, Expression expr) {

		if (! ints.contains(lValue) && (expr.expressionType == Expression.LITERALEXPR 
				|| expr.expressionType == Expression.IDEXPR || expr.expressionType == Expression.TEMPEXPR 
				||expr.expressionType == Token.INTLITERAL)) {
			System.out.println("Error - cannot assign an int to a string.");
		}
		
		if (! strs.contains(lValue) && expr.expressionType == Token.STRING ) {
			System.out.println("Error - cannot assign an int to a string.");
		}
		
		if (expr.expressionType == Token.STRING) {
			// assign.add(lValue.expressionName);
			// stringListAssign.add(expr.expressionName);
			strings.put(lValue.expressionName, expr.expressionName);
			strs.add(lValue.expressionName);
		} else if (expr.expressionType == Expression.TEMPEXPR) { // Expression.LITERALEXPR) {
			//integers.put(lValue.expressionName, expr.expressionName);
			ints.add(lValue.expressionName);
			
			System.out.println("\tMOVL " + expr.expressionName + ", %eax");
			System.out.println("\tMOVL %eax, " + lValue.expressionName);
		} else if (expr.expressionType == Token.INTLITERAL || expr.expressionType == Expression.LITERALEXPR) { // Expression.LITERALEXPR) {
			//integers.put(lValue.expressionName, expr.expressionName);
			ints.add(lValue.expressionName);
			
			System.out.println("\tMOVL " + "$" + expr.expressionIntValue + ", %eax");
			System.out.println("\tMOVL %eax, " + lValue.expressionName);
		} else {
			// Variable
			String val = integers.get(expr.expressionName);
			
			if (val != null) {
				//integers.put(lValue.expressionName, val);
				ints.add(lValue.expressionName);
				
				System.out.println("\tMOVL " + expr.expressionName + ", %eax");
				System.out.println("\tMOVL %eax, " + lValue.expressionName);
			} else {
				strings.put(lValue.expressionName, val);
				strs.add(lValue.expressionName);
			}
		}
	}

	void generateAssignmentOnDeclaration(Expression lValue, Expression expr) {
		if (expr.expressionType == Token.STRING) {
			// String assignment
			
			// assign.add(lValue.expressionName);
			// stringListAssign.add(expr.expressionName);
			
			strings.put(lValue.expressionName, expr.expressionName);
			strs.add(lValue.expressionName);
		} else if (expr.expressionType == Expression.LITERALEXPR || expr.expressionType == Token.INTLITERAL) {
			integers.put(lValue.expressionName, expr.expressionName);
			ints.add(lValue.expressionName);
			
			// variables.add(lValue.expressionName);
			// variablesAssign.add(expr.expressionName);
		} else if (expr.expressionType == Expression.IDEXPR || expr.expressionType  == Token.ID) {
			// Variable
			String val = strings.get(expr.expressionName);
			if (val == null) {
				val = integers.get(expr.expressionName);
				integers.put(lValue.expressionName, val);
			} else {
				strings.put(lValue.expressionName, val);
			}
	
			// variables.add(lValue.expressionName);
			// variablesAssign.add()
		} else {
			System.out.println("\tMOVL " + expr.expressionName + ", %eax");
			System.out.println("\tMOVL %eax, " + lValue.expressionName);
		}
	}
	
	Expression generateConcatenation( Expression left, Expression right , Expression lValue ) {
		Expression concat = new Expression(Token.STRING, left.expressionName + right.expressionName);
		strings.put(lValue.expressionName, concat.expressionName);
		strs.add(lValue.expressionName);

		return concat;
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
//		for (String var : variablesList)
//			System.out.println(var + ":\t.int 0");
//
//		for (int i = 0; i < variables.size(); i ++) {
//			System.out.println(variables.get(i) + ":\t.int " + variablesAssign.get(i));
//		}
//		
//		for (String var : stringList)
//			System.out.println(var + ":\t.zero 256");
//		
//		for(int i = 0; i < assign.size(); i ++) {
//			String var = assign.get(i);
//			System.out.println(var + ":\t.string \"" + stringListAssign.get(i) + "\"");
//		}
		
		HashMap<String, Boolean> seen = new HashMap<>();
		
		for (String i : ints) {
			if (seen.get(i) == null) {
				if (integers.get(i) == null) {
					System.out.println(i + ":\t.int 0");
				} else {
					System.out.println(i + ":\t.int " + integers.get(i));
				}
				seen.put(i, true);
			}
		}
		
		for (String s : strs) {
			if (seen.get(s) == null) {
				if (strings.get(s) == null) {
					System.out.println(s + ":\t.zero 256");
				} else if (strings.get(s) == "") {
					System.out.println(s + ":\t.zero 256");
				} else {
					System.out.println(s + ":\t.string \"" + strings.get(s) + "\"");
				}
				seen.put(s, true);
			}
		}
		
		System.out.println("__minus:  .byte '-'");
		System.out.println("__negOne: .int -1");
		System.out.println("__negFlag: .byte '+'");
	}

	private String createTempName() {
		String tempVar = new String("temp" + tempCount++);
		// variablesList.add(tempVar);
		ints.add(tempVar);
		return tempVar;
	}

}
