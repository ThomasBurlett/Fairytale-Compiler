public class Expression    
{
    public final static int IDEXPR = 0;
    public final static int LITERALEXPR = 1;
    public final static int TEMPEXPR = 2;
    
    public int expressionType;		// For integers, see above (to decide $ or not)
    public String expressionName;	// Variable name
    public int expressionIntValue;	// Value assigned to the variable
    public Token expressionToken;	// Token, containing variable
    public int expressionValueType;
    public boolean NOTflag;
        
    public Expression( )
    {
        expressionType = 0;
        expressionName = "";
        NOTflag = false;
    }
        
    public Expression( int type, int value)
    {
        expressionType = type;
        expressionIntValue = value;
        NOTflag = false;
    }

    public Expression( int type, String name)
    {
        expressionType = type;
        expressionName = name;
        NOTflag = false;
    }

    public Expression( int type, Token token)
    {
        expressionType = type;
        expressionToken = token;
        NOTflag = false;
    }
    
    public Expression( int type, String name, int val)
    {
        expressionType = type;
        expressionName = name;
        expressionIntValue = val;
        NOTflag = false;
    }

    public Expression( int type, String name, Token token)
    {
        expressionType = type;
        expressionName = name;
        expressionToken = token;
        NOTflag = false;
    }
    
    public Expression( String name, int val, Token token)
    {
        expressionName = name;
        expressionIntValue = val;
        expressionToken = token;
        NOTflag = false;
    }
    
    public Expression( int type, String name, int val, Token token)
    {
        expressionType = type;
        expressionName = name;
        expressionIntValue = val;
        expressionToken = token;
        NOTflag = false;
    }
}
