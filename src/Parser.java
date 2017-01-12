
/* PROGRAM Micro */

/* 	Java version of the Micro compiler from Chapter 2 of "Crafting a Compiler" --
*	for distribution to instructors 
*	Converted to Java by James Kiper in July 2003.
*
*/

/* Micro grammar

 < program > 		-> #Start OnceUponATime < statement list > TheEnd 
 < statement list > -> < statement > 	{ < statement > } 
 < statement >  	-> < ident > := < expression > ; 		#Assign 
 < statement >      -> < ident > := < string > ; 			#Assign 
 ** Below recently added **
 < statement >  	-> <dataType> < ident > := < IntLiteral > ; 	#Assign 
 < statement >  	-> <dataType> < ident > := < ident > ; 			#Assign 
 < statement >      -> <dataType> < ident > := < stringLiteral > ; 	#Assign 
 < statement > 		-> <dataType> < ident > ; 						#DeclareNotAssign  
 ** Above recently added **
 < statement >     	-> READ ( < id list > ) ; 
 < statement >      -> WRITE ( < expr list > ) ; 
 < statement >      -> WRITE ( < string > ) ; 
 < id list >       	-> < ident > 		{, < ident > } 		#ReadId 
 < expr list >      -> < expression >  	{, < expression > } #WriteExpr
 < string list >	-> <string> 		{, <string> } 		#WriteString
 < string >      	-> someString  		{ + someString 		#Concat } 
 < expression >  	-> < primary >  { < add op > < primary > #GenInfix } 
 < primary >       	-> ( < expression > ) 
 < primary >     	-> < ident > 
 < primary >      	-> IntLiteral      						#ProcessLiteral 
 < add op >       	-> PlusOp      							#ProcessOp 
 < add op >       	-> MinusOp      						#ProcessOp 
 < ident >          -> < data type > Id     				#ProcessId 
 < ident >          -> Id     					#ProcessId  #MustBeInSymbolTable 
 < data type >  	-> ~i      					#Int  
 < data type >  	-> ~s      					#String 
 < system goal > 	-> < program > EofSym    	#Finish 
 
 
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

    public Parser()
    {
        
    }

    static public void main (String args[])
    {
        Parser parser = new Parser();						// Create new parser
        //scanner = new Scanner( args[0]);
        scanner = new Scanner("testcases/testcase15.txt");	// Scan in file (get current and next line)
        codeFactory = new CodeFactory();					// Create new Code Factory
        symbolTable = new SymbolTable();					// Create new Symbol Table
        parser.parse();										// Parse data 
    }
    
    public void parse()
    {
        currentToken = scanner.findNextToken();				// Get next token of currentLine 
        systemGoal();
    }
    
    private void systemGoal()
    {
        program();											// Begin program (begin the CFG)
        codeFactory.generateData();
    }
    
    // <program> := BEGIN <statement list> END
    private void program()
    {
        match( Token.BEGIN );					// Match OnceUponATime to start
        codeFactory.generateStart();			// Start Program
        statementList();
        match( Token.END );						// Match TheEnd to end
        codeFactory.generateExit();				// Exit Program
    }
    
    // <statement list> := <statement> { <statement> }
    private void statementList()
    {
        while ( currentToken.getType() == Token.ID || currentToken.getType() == Token.STRINGDT 
        		|| currentToken.getType() == Token.INTDT || currentToken.getType() == Token.READ 
        		|| currentToken.getType() == Token.WRITE) 
        {
            statement();
        }
    }
    
    // < statement > -> <ident> := <expression> #Assign ;
    // < statement > -> <ident> := <string> 	  #Assign ;
    // < statement > -> READ ( <id list> ) ;
    // < statement > -> WRITE ( <expr list> ) ;
    // < statement > -> WRITE ( <string list> ) ;
    // < statement > -> <dataType> < ident > := < primary > ; 			#Assign #DoNotAllow ( expression )
    // < statement > -> <dataType> < ident > := < ident > ; 			#Assign 
    // < statement > -> <dataType> < ident > := < stringLiteral > ; 	#Assign 
    // < statement > -> <dataType> < ident > ; 							#DeclareNotAssign  
    
    private void statement()
    {
        Expression lValue = new Expression();
        Expression expr;
        
        // If the variable is being declared via a data type
        if ( currentToken.getType() == Token.STRINGDT || currentToken.getType() == Token.INTDT ) {
        	
        	// Check the data type
        	int dt = dataType();
        	
        	// Check the variable name
        	lValue = identifier();
            
        	// Declaration, Assignment, or Error
            switch(currentToken.getType()) {
            	case Token.SEMICOLON : {	
            		// <statement> -> <dataType> <ident> ;
					match(Token.SEMICOLON);
					codeFactory.generateDeclaration(dt, new Token(lValue.expressionName, Token.ID));
					return;
				}
				case Token.ASSIGNOP: {
					// Assignment
					match(Token.ASSIGNOP);
					break;
				}
				default: {
					error(currentToken);
					return;
				}
			}         
            
            // Assignment
            if (currentToken.getType() == Token.STRING) { // && lValue.expressionType == Token.STRINGDT) {
            	// < statement > -> <dataType> < ident > := < stringLiteral > ; #Assign 
            	expr = stringLiteral();
            } else if ( ( currentToken.getType() == Token.INTLITERAL || currentToken.getType() == Token.MINUS 
            		|| currentToken.getType() == Token.PLUS )) { // && lValue.expressionType == Token.INTDT) {
            	// < statement > -> <dataType> < ident > := < primary > ; #Assign (not an expression)
            	expr = primary();
            } else if (currentToken.getType() == Token.ID){
            	// < statement > -> <dataType> < ident > := < ident > ; #Assign 
            	expr = identifier(); 
            } else {
            	error(currentToken);
            	return;
            }
            
            codeFactory.generateAssignmentOnDeclaration( lValue, expr );
            match( Token.SEMICOLON );
        }
        else if ( currentToken.getType() == Token.ID ) {
            // Not declaration, just assignment
        	
        	// Check the variable name
        	lValue = identifier();
            
            switch(currentToken.getType()) {
				case Token.ASSIGNOP: {
					// Assignment
					match(Token.ASSIGNOP);
					break;
				}
				default: {
					error(currentToken);
					return;
				}
			}
			
            if (currentToken.getType() == Token.STRING) {		
            	// <statement> -> <ident> := <string> #Assign ;
            	// match( Token.STRING );
            	expr = string();
            } else {
            	// <statement> -> <ident> := <expression> #Assign ;
            	expr = expression(); 
            }
            
            codeFactory.generateAssignment( lValue, expr );
            match( Token.SEMICOLON );
        } else if ( currentToken.getType() == Token.READ )
        {
        	// <statement> -> READ ( <id list> ) ;
            match( Token.READ );
            match( Token.LPAREN );
            idList();
            match( Token.RPAREN );
            match( Token.SEMICOLON );
        } else if ( currentToken.getType() == Token.WRITE )
        {
            match( Token.WRITE );
            match( Token.LPAREN );
            
            if (currentToken.getType() == Token.STRING) {		
            	// <statement> -> WRITE ( <stringList> ) ;
            	stringList();
            } else {
                // <statement> -> WRITE ( <expr list> ) ;
                expressionList();
            }
            
            match( Token.RPAREN );
            match( Token.SEMICOLON );
        } else {
        	error(currentToken);	// Illegal start to statement
        }
    }
    
    // <id list> -> <ident> #ReadId {, <ident> #ReadId }
    private void idList()
    {
        Expression idExpr;
        idExpr = identifier();
        codeFactory.generateRead(idExpr);
        while ( currentToken.getType() == Token.COMMA )	// {, <ident> #ReadId }
        {
            match(Token.COMMA);
            idExpr = identifier();
            codeFactory.generateRead(idExpr);
        }
    }
    
    // <expr list> -> <expression> #WriteExpr {, <expression> #WriteExpr}
    private void expressionList()
    {
        Expression expr;
        expr = expression();
        codeFactory.generateWrite(expr);
        while ( currentToken.getType() == Token.COMMA ) // {, <expression> #WriteExpr}
        {
            match( Token.COMMA );
            expr = expression();
            codeFactory.generateWrite(expr);
        }
    }

    // <string list> -> <string> #WriteString {, <string> #WriteString}
    private void stringList()
    {
        Expression str = new Expression();
        str = string();
        codeFactory.generateStringWrite(str);
        while ( currentToken.getType() == Token.COMMA ) // {, <expression> #WriteExpr}
        {
            match( Token.COMMA );
            str = string();
            codeFactory.generateStringWrite(str);
        }
    }
    
    // <expression>	-> <primary> {<add op> <primary> #GenInfix}
    private Expression expression()
    {
        Expression result;
        Expression leftOperand;
        Expression rightOperand;
        Operation op;
        
        result = primary();
        while ( currentToken.getType() == Token.PLUS || currentToken.getType() == Token.MINUS ) // {<add op> <primary> #GenInfix}
        {
            leftOperand = result;
            op = addOperation();
            rightOperand = primary();
            result = codeFactory.generateArithExpr( leftOperand, rightOperand, op );
        }
        
        return result;
    }
    
    // <string> -> <strLiteral> {+  <strLiteral> #Concat }
    // Scanner removes double quotes
    private Expression string()
    {
        Expression str;
        Expression leftOperand;
        Expression rightOperand;
        
        // Get the current string
        str = stringLiteral();

        while ( currentToken.getType() == Token.PLUS ) // {, <expression> #WriteExpr}
        {
            leftOperand = str;
        	match( Token.PLUS );
            rightOperand =  stringLiteral();
            
            //TODO 
            //str = codeFactory.generateConcatination( leftOperand, rightOperand, '+' );
        }
        
        return str;
    }
    
    // <strLiteral> -> "characters"
    // Scanner removes double quotes
    private Expression stringLiteral()
    {
        Expression str;
        
        // Get the current string
        str = new Expression(Token.STRING, currentToken.getId());
        match(Token.STRING);

        return str;
    }
    
    // <primary> -> ( <expression> )
    // <primary> -> <ident>
    // <primary> -> IntLiteral #ProcessLiteral
    private Expression primary()
    {
        Expression result = new Expression();
        switch ( currentToken.getType() )
        {
            case Token.LPAREN :			// <primary> -> ( <expression> )
            {
                match( Token.LPAREN );
                result = expression();
                match( Token.RPAREN );
                break;
            } 
            case Token.ID:				// <primary> -> <ident>
            {
                result = identifier();
                break;
            }
            case Token.INTLITERAL:		// <primary> -> IntLiteral #ProcessLiteral
            {
                match(Token.INTLITERAL);
                result = processLiteral();
                break;
            }
            case Token.MINUS:			// <primary> -> *signed* IntLiteral #ProcessLiteral
            {
                match(Token.MINUS);
                processSign();
                match(Token.INTLITERAL);
                result = processLiteral();
                break;
            }
            case Token.PLUS:			// <primary> -> *signed* IntLiteral #ProcessLiteral
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


	// <add op>	-> PlusOp #ProcessOp
    // <add op>	-> MinusOp #ProcessOp
    private Operation addOperation()
    {
        Operation op = new Operation();
        switch ( currentToken.getType() )
        {
            case Token.PLUS:			// <add op>	-> PlusOp #ProcessOp
            {
                match( Token.PLUS ); 
                op = processOperation();
                break;
            }
            case Token.MINUS:			// <add op>	-> MinusOp #ProcessOp
            {
                match( Token.MINUS ); 
                op = processOperation();
                break;
            }
            default: error( currentToken );
        }
        return op;
    }        
 
    // <ident> -> Id #ProcessId
	// <ident> -> Id #ProcessId	#Note: Must be in symbol table
    private Expression identifier()
    {
		Expression expr = new Expression();

    	if ( previousToken.getType() == Token.STRINGDT ) { 
    		// If it was just declared, add to symbol table
			match( Token.ID );
	        expr = processIdentifier(Token.STRINGDT);
		}
    	else if ( previousToken.getType() == Token.INTDT ) { 
    		// If it was just declared, add to symbol table
			match( Token.ID );
	        expr = processIdentifier(Token.INTDT);
		}
    	else if ( currentToken.getType() ==  Token.ID ) { 
    		// If it was not declared, check the symbol table for it
	        match( Token.ID );
	        expr = processIdNoType();
		}
    	else { 
    		error( currentToken );
    	}
    	
//    	// Changed to add data type in statement function
//    	switch ( currentToken.getType() ) {
//    		case Token.STRINGDT: { 
//    			dataType();
//    			match( Token.ID );
//    	        expr = processIdentifier();
//    		}
//    		case Token.INTDT: { 
//    			dataType();
//    			match( Token.ID );
//    	        expr = processIdentifier();
//    		}
//    		case Token.ID: { 
//    	        match( Token.ID );
//    	        expr = processIdNoType();
//    		}
//    		default: error( currentToken );
//    	}

        return expr;
    }
    
    // < data type >  	-> ~i  	#Int  
    // < data type >  	-> ~s 	#String 
    private int dataType() {
	    switch(currentToken.getType()) {
		    case Token.STRINGDT: { 
		    	match( Token.STRINGDT ); 
		    	return Token.STRINGDT;
		    }
			case Token.INTDT: { 
				match( Token.INTDT ); 
				return Token.INTDT;
			}
			default: error(currentToken);
	    }
	    
	    return -1;
    }
    
    private void match( int tokenType )
    {   	
        previousToken = currentToken;
        if ( currentToken.getType() == tokenType ) {
        	if ( Token.END == tokenType )
        		return;
        	
           	currentToken = scanner.findNextToken();
        } else 
        {
            error( tokenType );
        	System.out.println("Error from token " + currentToken.getId() + 
        			", \nToken type " + currentToken.getType() + 
        			", but looked for type: " + tokenType + "\n");
        	
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
        else error( previousToken );
        return op;
    }
    
    /* Called when identifier has a type and needs to be added to the symbol table */
    private Expression processIdentifier(int dataType)
    {
        Expression expr = new Expression( previousToken.getType(), previousToken.getId());
        
        if (!symbolTable.checkSTforItem( previousToken.getId() ) )
        {
            symbolTable.addItem( previousToken );
            //codeFactory.generateDeclaration(dataType, previousToken );
        } else {
        	// Declaring variable as different type than previous declaration
        	if ( previousToken.getType() == symbolTable.getToken( previousToken.getId() ).getType() ) {
        		error(previousToken);
        	}
        }
        
        return expr;
    }
    
    private Expression processIdNoType()
    {
        //Expression expr = new Expression( Expression.IDEXPR, previousToken.getId());
        Expression expr = new Expression( previousToken.getType(), previousToken.getId());

        if ( ! symbolTable.checkSTforItem( previousToken.getId() ) )
        {
        	// Type never declared
        	error(previousToken);
//            symbolTable.addItem( previousToken );
//            codeFactory.generateDeclaration( previousToken );
        }
        
        return expr;
    }
    
    private void error( Token token )
    {
        System.out.println( "Syntax error! (Token) Parsing token type " + token.toString() + " at line number " + 
                scanner.getLineNumber() );
        
    	System.out.println("\nError from token " + currentToken.getId() + 
    			", \nToken type " + currentToken.getType() + "\n");
    }
    
    private void error( int tokenType )
    {
        System.out.println( "Syntax error! (tokenType) Parsing token type " +tokenType + " at line number " + 
                scanner.getLineNumber() );
        

//    	assert(true == false);
    }
}