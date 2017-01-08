
/* PROGRAM Micro */

/* 	Java version of the Micro compiler from Chapter 2 of "Crafting a Compiler" --
*	for distribution to instructors 
*	Converted to Java by James Kiper in July 2003.
*
*/

/* Micro grammar
   <program>	    -> #Start BEGIN <statement list> END
   <statement list> -> <statement> {<statement>}
   <statement>	    -> <ident> := <expression> #Assign ;
   <statement>	    -> READ ( <id list> ) ;
   <statement>	    -> WRITE ( <expr list> ) ;
   <id list>	    -> <ident> #ReadId {, <ident> #ReadId }
   <expr list>	    -> <expression> #WriteExpr {, <expression> #WriteExpr}
   <expression>	    -> <primary> {<add op> <primary> #GenInfix}
   <primary>	    -> ( <expression> )
   <primary>	    -> <ident>
   <primary>	    -> IntLiteral #ProcessLiteral
   <primary>	    -> MinusOp #ProcessNegative IntLiteral #ProcessLiteral
   <add op>	    	-> PlusOp #ProcessOp
   <add op>	    	-> MinusOp #ProcessOp
   <ident>	    	-> Id #ProcessId
   <system goal>    -> <program> EofSym #Finish
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
        Parser parser = new Parser();		// Create new parser
      //  scanner = new Scanner( args[0]);
        scanner = new Scanner( "test");		// Scan in file (get current and next line)
        codeFactory = new CodeFactory();	// Create new Code Factory
        symbolTable = new SymbolTable();	// Create new Symbol Table
        parser.parse();						// Parse data 
    }
    
    public void parse()
    {
        currentToken = scanner.findNextToken();	// Get next token of currentLine 
        systemGoal();
    }
    
    private void systemGoal()
    {
        program();								// Begin program (begin the CFG)
        codeFactory.generateData();
    }
    
    // <program> := BEGIN <statement list> END
    private void program()
    {
        match( Token.BEGIN );					// Match BEGIN to start //TODO Change beginning and end syntax
        codeFactory.generateStart();			// Start Program
        statementList();
        match( Token.END );						// Match END to end
        codeFactory.generateExit();				// Exit Program
    }
    
    // <statement list> := <statement> { <statement> }
    private void statementList()
    {
        while ( currentToken.getType() == Token.ID || currentToken.getType() == Token.READ || 
                    currentToken.getType() == Token.WRITE)
        {
            statement();
        }
    }
    
    // <statement> -> <ident> := <expression> #Assign ;
    // <statement> -> <ident> := <string> 	  #Assign ;
    // <statement> -> READ ( <id list> ) ;
    // <statement> -> WRITE ( <expr list> ) ;
    // <statement> -> WRITE ( <string list> ) ;
    // <statement> -> <ident> ;
    private void statement()
    {
        Expression lValue;
        Expression expr;
        
        switch ( currentToken.getType() )
        {
            case Token.ID:
            {
                lValue = identifier();
                //TODO: Need it to also work for <statement> -> <ident> ;
                
                match( Token.ASSIGNOP );
                
                //TODO: Currently works for 	 <statement> -> <ident> := <expression> #Assign ;
                //TODO: Need it to also work for <statement> -> <ident> := <string> 	#Assign ;
                expr = expression();
                codeFactory.generateAssignment( lValue, expr );
                match( Token.SEMICOLON );
                break;
            }
            case Token.READ :
            {
            	//TODO: Currently works for  <statement> -> READ ( <id list> ) ;
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
                
                //TODO Currently works for 	 	 <statement> -> WRITE ( <expr list> ) ;
                //TODO: Need it to also work for <statement> -> WRITE ( <string list> ) ;
                expressionList();
                match( Token.RPAREN );
                match( Token.SEMICOLON );
                break;
            }
            default: error(currentToken);
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
    
    //TODO Add <string list> here
    // <string list> -> <string> #WriteString {, <string> #WriteString}
    
    
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
    
    //TODO Add <string> here
    // <string> -> <strPrime> {+  <strPrime> #Concat
    
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
    
    //TODO Add <strPrimary> here
    //TODO <strPrimary> -> " < character list > "
    //TODO <strPrimary> -> < ident >
    
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
    
    //TODO Add <character list> here
    //TODO <character list> -> <character> { <character> }
    
    //TODO Add <character> here
    //TODO <character> -> *insert ascii characters here*
    
    // <ident> -> Id #ProcessId
    private Expression identifier()
    {
        Expression expr;
        match( Token.ID );
        expr = processIdentifier();
        return expr;
    }
    
    private void match( int tokenType)
    {
        previousToken = currentToken;
        if ( currentToken.getType() == tokenType )
            currentToken = scanner.findNextToken();
        else 
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
        else error( previousToken );
        return op;
    }
    
    private Expression processIdentifier()
    {
        Expression expr = new Expression( Expression.IDEXPR, previousToken.getId());
        
        if ( ! symbolTable.checkSTforItem( previousToken.getId() ) )
        {
            symbolTable.addItem( previousToken );
            codeFactory.generateDeclaration( previousToken );
        }
        return expr;
    }
    private void error( Token token )
    {
        System.out.println( "Syntax error! Parsing token type " + token.toString() + " at line number " + 
                scanner.getLineNumber() );
        if (token.getType() == Token.ID )
            System.out.println( "ID name: " + token.getId() );
    }
    private void error( int tokenType )
    {
        System.out.println( "Syntax error! Parsing token type " +tokenType + " at line number " + 
                scanner.getLineNumber() );
    }
}