import java.io.PrintStream;

/* PROGRAM Micro */

/* 	Java version of the Micro compiler from Chapter 2 of "Crafting a Compiler" --
*	for distribution to instructors 
*	Converted to Java by James Kiper in July 2003.
*/


public class Parser
{
    private static Scanner scanner;
    private static SymbolTable symbolTable;
    private static CodeFactory codeFactory;
    private Token currentToken;
    private Token previousToken;
    private static boolean signSet = false;
    private static String signFlag = "+";
    
    public static String filename; // = "test_write_01.txt";
    public static PrintStream stdout;
    public Parser()
    {

    }

    // static public void main (String args[])
    static public void main (String test)
    {
    	filename = test;
    	stdout = System.out;
        Parser parser = new Parser();
        //scanner = new Scanner("testcases-2/test_write_01.txt");
        scanner = new Scanner("testcases-2/" + test);
        codeFactory = new CodeFactory();
        symbolTable = new SymbolTable();
        parser.parse();
        
        // Set output back to console
        System.setOut(stdout);
    }
    
    public void parse()
    {
        currentToken = scanner.findNextToken();
        systemGoal();
    }
    
    private void systemGoal()
    {
        program();
        codeFactory.generateData();
    }
    
    private void program()
    {
        match( Token.BEGIN );
        codeFactory.generateStart();
        statementList();
        match( Token.END );
        codeFactory.generateExit();
    }
    
    private void statementList()
    {
        while ( currentToken.getType() == Token.ID || currentToken.getType() == Token.READ 
        		|| currentToken.getType() == Token.WRITE || currentToken.getType() == Token.INTDT
        		|| currentToken.getType() == Token.BOOLDT )
        {
            statement();
        }
    }
    
    private void statement()
    {
        Expression lValue;
        Expression expr;
        
        switch ( currentToken.getType() )
        {
            case Token.ID:
            {
                lValue = identifier();
                match( Token.ASSIGNOP );
                
                int dataType = currentToken.getType();
                if (currentToken.getType() == Token.ID) {
                	dataType = symbolTable.checkSTforType(currentToken);
                }
                
                
                if ( dataType == Token.NOT || dataType == Token.BOOL || dataType == Token.BOOLDT ) {
                	expr = boolExpression();  
                	lValue.expressionValueType = Token.BOOL;
                } else {
                    expr = expression();
                	lValue.expressionValueType = Token.INTLITERAL;
                }

                codeFactory.generateAssignment( lValue, expr );
                match( Token.SEMICOLON );
                break;
            } 
            case Token.BOOLDT:
            {
            	match(Token.BOOLDT);
                lValue = identifier();
                lValue.expressionValueType = Token.BOOL;
                symbolTable.addItem( lValue.expressionName, Token.BOOLDT );

                switch ( currentToken.getType() )
                {
	                case Token.ASSIGNOP: {
	                	match( Token.ASSIGNOP );
	                	break;
	                }
	                case Token.SEMICOLON: {
	                	match( Token.SEMICOLON );
	                	codeFactory.generateDeclaration( lValue );
	                	return;
	                }
	                default: {
	                	// Error - What error is this?
	                	System.out.println("ERROR - Expected a semi-colon or an assignment.");
	                	error(currentToken);
	                }
                }
                                
                if (currentToken.getType() == Token.BOOL) {
                	expr = bool();
                    expr.expressionValueType = Token.BOOL;
                } else {
                	expr = identifier();
                    expr.expressionValueType = Token.ID;
                }

                codeFactory.generateInitAssignment( lValue, expr );
                match( Token.SEMICOLON );
                break;
            }
            case Token.INTDT:
            {
            	match(Token.INTDT);
                lValue = identifier();
                lValue.expressionValueType = Token.INTLITERAL;
                symbolTable.addItem( lValue.expressionName, Token.INTDT );
                
                switch ( currentToken.getType() )
                {
	                case Token.ASSIGNOP: {
	                	match( Token.ASSIGNOP );
	                	break;
	                }
	                case Token.SEMICOLON: {
	                	match( Token.SEMICOLON );
	                	codeFactory.generateDeclaration( lValue );
	                	return;
	                }
	                default: {
	                	// Error - What error is this?
	                	System.out.println("ERROR - Expected a semi-colon or an assignment.");
	                	error(currentToken);
	                }
                }
                                
                if (currentToken.getType() == Token.INTLITERAL) {
                	match(Token.INTLITERAL);
                    expr = processLiteral();
                    expr.expressionValueType = Token.INTLITERAL;
                } else {
                	expr = identifier();
                    //expr.expressionValueType = Token.ID;
                }
                
                codeFactory.generateInitAssignment( lValue, expr );
                match( Token.SEMICOLON );
                break;
            }
            case Token.READ :
            {
                match( Token.READ );
                match( Token.LPAREN );
                idList();
                match( Token.RPAREN );
                match( Token.SEMICOLON );
                break;
            }
            case Token.WRITE :
            {
                match( Token.WRITE );
                match( Token.LPAREN );
                expressionList();
                match( Token.RPAREN );
                match( Token.SEMICOLON );
                break;
            }

            default: error(currentToken);
        }
    }
    
    private void idList()
    {
        Expression idExpr;
        idExpr = identifier();
        codeFactory.generateRead(idExpr);
        while ( currentToken.getType() == Token.COMMA )
        {
            match(Token.COMMA);
            idExpr = identifier();
            codeFactory.generateRead(idExpr);
        }
    }
    
    private void expressionList()
    {
        Expression expr;
        expr = expression();
        codeFactory.generateWrite(expr);
        while ( currentToken.getType() == Token.COMMA )
        {
            match( Token.COMMA );
            expr = expression();
            codeFactory.generateWrite(expr);
        }
    }
    
//    private void boolExpressionList()
//    {
//        Expression expr;
//        expr = boolExpression();
//        codeFactory.generateWrite(expr);
//        while ( currentToken.getType() == Token.COMMA )
//        {
//            match( Token.COMMA );
//            expr = boolExpression();
//            codeFactory.generateWrite(expr);
//        }
//    }
    
    private Expression expression()
    {
        Expression result;
        Expression leftOperand;
        Expression rightOperand;
        Operation op;
        
        result = factor();
        while ( currentToken.getType() == Token.PLUS || currentToken.getType() == Token.MINUS )
        {
            leftOperand = result;
            op = anyOperation();
            rightOperand = expression();
            result = codeFactory.generateMathExpr( leftOperand, rightOperand, op );
        }
        
        result.expressionValueType = Token.INTLITERAL;
        return result;
    }
    
    private Expression factor() {
        Expression result;
        Expression leftOperand;
        Expression rightOperand;
        Operation op;
        
        result = primary();

        while ( currentToken.getType() == Token.MULT || currentToken.getType() == Token.DIV  
        		|| currentToken.getType() == Token.MOD)
        {
            leftOperand = result;
            op = anyOperation();
//            op.opType = currentToken.getType();
//            match(op.opType);
            rightOperand = factor();
            result = codeFactory.generateMathExpr( leftOperand, rightOperand, op );
        }
        
        result.expressionValueType = Token.INTLITERAL;
        return result;
    }
    
    private Expression primary()
    {
        Expression result = new Expression();
        switch ( currentToken.getType() )
        {
            case Token.LPAREN :
            {
                match( Token.LPAREN );
                result = expression();
                match( Token.RPAREN );
                break;
            }
            case Token.ID:
            {
                result = identifier();               
                break;
            }
            case Token.INTLITERAL:
            {
                match(Token.INTLITERAL);
                result = processLiteral();
                break;
            }
            case Token.MINUS:
            {
                match(Token.MINUS);
                processSign();
                match(Token.INTLITERAL);
                result = processLiteral();
                break;
            }
            case Token.PLUS:
            {
                match(Token.PLUS);
                processSign();
                match(Token.INTLITERAL);
                result = processLiteral();
                break;
            }
            default: error( currentToken );
        }
        return result;
    }
    
  
    private Operation anyOperation()
    {
        Operation op = new Operation();
        switch ( currentToken.getType() )
        {
            case Token.PLUS:
            {
                match( Token.PLUS ); 
                op = processOperation();
                break;
            }
            case Token.MINUS:
            {
                match( Token.MINUS ); 
                op = processOperation();
                break;
            }
            case Token.MULT:
            {
                match( Token.MULT ); 
                op = processOperation();
                break;
            }
            case Token.DIV:
            {
                match( Token.DIV ); 
                op = processOperation();
                break;
            }
            case Token.MOD:
            {
                match( Token.MOD ); 
                op = processOperation();
                break;
            }
            default: {
            	// Error - Invalid addition or subtraction operation.
            	System.out.println("ERROR - Invalid operator.");
            	error( currentToken );
            }
        }
        return op;
    }
    
    
    /* <boolExpr> -> <boolFactor> { OR <boolExpr> } */
    private Expression boolExpression()
    {
        Expression result;
        Expression leftOperand;
        Expression rightOperand;
        Operation op;
        
        result = boolFactor();
        while ( currentToken.getType() == Token.OR ) {
            leftOperand = result;
            op = new Operation();
            op.opType = Token.OR;
            match(op.opType);
            rightOperand = boolExpression();
            result = codeFactory.generateBoolExpr( leftOperand, rightOperand, op );
        }
        
        return result;
    }
    
    /* <boolFactor> -> <bool> { AND <boolFactor> } */
    private Expression boolFactor() {
        Expression result;
        Expression leftOperand;
        Expression rightOperand;
        Operation op;
        
        result = bool();
        while ( currentToken.getType() == Token.AND ) {
            leftOperand = result;
            
            op = new Operation();
            op.opType = Token.AND;  
            match(op.opType);
            
            rightOperand = boolFactor();
            result = codeFactory.generateBoolExpr( leftOperand, rightOperand, op );
        }
        
        return result;
    }    
    
    
    /*  <bool> -> <boolLiteral>
     *  <bool> -> <ident>
     *  <bool> -> NOT <boolLiteral>
     *  <bool> -> ( <boolExpr> ) 
     */
    private Expression bool() {
        Expression result = new Expression();
        switch ( currentToken.getType() )
        {
            case Token.LPAREN :
            {
                match( Token.LPAREN );
                result = boolExpression();
                match( Token.RPAREN );
                break;
            }
            case Token.ID:
            {
                result = identifier();
                
                if ( !validateType(Token.BOOLDT) ) {
                	// Error - assigning a bool to the incorrect variable type
                	System.out.println("Error - Assigning value to incorrect data type.");
                }
                
                match(Token.ID);
                break;
            }
            case Token.BOOL:
            {
                match(Token.BOOL);
                result = processBoolean();
                break;
            }
            case Token.NOT:
            {
                match(Token.NOT);

                if (currentToken.getType() == Token.BOOL) {
                	match(Token.BOOL);
                    result = processBoolean();
                } else {                	
                	result = new Expression( Expression.IDEXPR, currentToken.getId(), currentToken );
                	result.expressionValueType = Token.BOOL;
                	match(Token.ID);
                	
                }
                
                result.NOTflag = true;
                break;
            }
            default: {
            	// Error - Invalid boolean type
            	System.out.println("ERROR - Invalid boolean type");
            	error( currentToken );
            	result = null;
            }
        }
        
        return result;
    }
    
    
    
    
    
    
    
    
    /* Modified for more detailed symbol table */
    private Expression identifier()
    {
        Expression expr;
        if (currentToken.getType() == Token.ID) {
        	expr = processIdentifier();
        	match( Token.ID );
        } else {
        	// Error - Invalid variable
        	System.out.println("ERROR - Invalid variable.");
        	error(currentToken);
        	expr = null;
        }
        
        return expr;
    }
    
    
    
    
    private void match( int tokenType ) {
        previousToken = currentToken;

        //System.out.println("Want: " + currentToken.getType());
        if ( currentToken.getType() == tokenType ) {
        	if ( tokenType == Token.END ) {
        		return;
        	}
        
            currentToken = scanner.findNextToken();
        } else 
        {
            error( tokenType );
            currentToken = scanner.findNextToken();
        }
    }

    
    
    
    private void processSign()
    {
    	Parser.signSet = true;
    	if ( previousToken.getType() == Token.PLUS ) 
    	{
    		Parser.signFlag = "+";
    	} else
    	{
    		Parser.signFlag = "-";
    	}
    }
    
    private Expression processLiteral()
    {
    	Expression expr;
        int value = ( new Integer( previousToken.getId() )).intValue();
        if (Parser.signSet && Parser.signFlag.equals("-"))
        {
        	 expr = new Expression( Expression.LITERALEXPR, "-"+previousToken.getId(), value*-1 );
        } else
        {
        	 expr = new Expression( Expression.LITERALEXPR, previousToken.getId(), value ); 
        }
        Parser.signSet = false;
        return expr;
    }
    
    private Operation processOperation()
    {
        Operation op = new Operation();
        if ( previousToken.getType() == Token.PLUS ) op.opType = Token.PLUS;
        else if ( previousToken.getType() == Token.MINUS ) op.opType = Token.MINUS;
        else if ( previousToken.getType() == Token.MULT ) op.opType = Token.MULT;
        else if ( previousToken.getType() == Token.DIV ) op.opType = Token.DIV;
        else if ( previousToken.getType() == Token.MOD ) op.opType = Token.MOD;
        else {
        	// Error - Invalid operation
        	System.out.println("ERROR - Invalid operator.");
        	error( previousToken );
        }
        return op;
    }
    
    /* Modified for more detailed symbol table */
    private Expression processIdentifier() {
        Expression expr = new Expression( Expression.IDEXPR, currentToken.getId());
        expr.expressionName = currentToken.getId();
        
        if ( !symbolTable.checkSTforItem(currentToken.getId()) ) {
            symbolTable.addItem( currentToken, previousToken );
        } else {
        	if ( symbolTable.checkSTforType(currentToken) != previousToken.getType() ) {
        		// Error - Assigning value of incorrect type
        		//System.out.println("Error - Assigning value of incorrect type.");
        		//error(currentToken);
        		
        		expr.expressionValueType = symbolTable.checkSTforType(currentToken);

        	}
        }
        
        return expr;
    }
    
    /* Method to check if the type assigned to a variable is that variables type */
    private boolean validateType(int type) {        
        if ( symbolTable.checkSTforItem(currentToken.getId()) ) {
        	int t = symbolTable.checkSTforType(currentToken);
        	
        	if (t != type) {
        		// Error - assignment to variable of incorrect type
        		System.out.println("ERROR - Cannot assign value of type " + type + " to a variable of type " + t + ".");
        		return false;
        	}
        }
        
        return true;
    }    
    
    
    private Expression processBoolean()
    {
    	Expression expr;
    	String strValue = previousToken.getId();
    	int intVal = 1;
    	
    	if (strValue.toLowerCase().equals("true")) {
    		intVal = 1;
    	} else {
    		intVal = 0;	
    	}        
        
    	expr = new Expression( Expression.LITERALEXPR, previousToken.getId() );
    	expr.expressionIntValue = intVal;
        return expr;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void error( Token token )
    {
        System.out.println( "Syntax error! Parsing token type " + token.toString() + " at line number " + 
                scanner.getLineNumber() );
        
        //if (token.getType() == Token.ID )
        //    System.out.println( "ID name: " + token.getId() );
    }
    
    private void error( int tokenType )
    {
        System.out.println( "Syntax error! Parsing token type " + tokenType + " at line number " + 
                scanner.getLineNumber() );
    }
}