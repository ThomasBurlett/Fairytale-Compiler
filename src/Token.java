

public class Token
{
    private String id;
    private int type;
    public final static int LexERROR = 0;
    public final static int BEGIN = 1;
    public final static int END = 2;
    public final static int READ = 3;
    public final static int WRITE = 4;
    public final static int ID = 5;
    public final static int LPAREN = 6;
    public final static int RPAREN = 7;
    public final static int COMMA = 8;
    public final static int SEMICOLON = 9;
    public final static int ASSIGNOP = 10;
    public final static int PLUS = 11;
    public final static int MINUS = 12;
    public final static int INTLITERAL = 13;
    public final static int EOF = 14;
    
    public final static int MULT = 17;
    public final static int DIV = 18;
    public final static int MOD = 19;
    public final static int BOOL = 20;
    public final static int BOOLDT = 21;
    public final static int INTDT = 22;
    public final static int ERR = 23;
    public final static int AND = 24;
    public final static int OR = 25;
    public final static int NOT = 26;

    public Token( String tokenString, int tokenType)
    {
        id = tokenString;
        type = tokenType;
        if (tokenType == ID)
        {
            String temp = tokenString.toLowerCase();
            if ( temp.compareTo( "onceuponatime") == 0) type = BEGIN;
            else if ( temp.compareTo( "theend") == 0) type = END;
            else if ( temp.compareTo("read") == 0) type = READ;
            else if ( temp.compareTo("write") == 0) type = WRITE;
            else if ( temp.compareTo("true") == 0) type = BOOL;
            else if ( temp.compareTo("false") == 0) type = BOOL;
            else if ( temp.compareTo("not") == 0) type = NOT;
            else if ( temp.compareTo("and") == 0) type = AND;
            else if ( temp.compareTo("or") == 0) type = OR;


        }
    }
    public String getId()
    {
        return id;
    }
    public int getType()
    {
        return type;
    }
    public String toString()
    {
        String str;
        switch (type)
        {
            case LexERROR : str = "Lexical Error"; break;
            case BEGIN : str = "BEGIN"; break;
            case END : str = "END"; break;
            case READ : str = "READ"; break;
            case WRITE : str = "WRITE"; break;
            case ID: str = "ID"; break;
            case LPAREN : str = "LPAREN"; break;
            case RPAREN : str = "RPAREN"; break;
            case COMMA : str = "COMMA"; break;
            case SEMICOLON : str = "SEMICOLON"; break;
            case ASSIGNOP : str = "ASSIGNOP"; break;
            case PLUS : str = "PLUS"; break;
            case MINUS : str = "MINUS"; break;
            case INTLITERAL : str = "INTLITERAL"; break;
            case EOF : str = "EOF"; break;
            case MULT : str = "MULT"; break;
            case DIV : str = "DIV"; break;
            case MOD : str = "MOD"; break;
            case BOOL : str = "BOOL"; break;
            case INTDT : str = "INTDT"; break;
            case BOOLDT : str = "BOOLDT"; break;
            case ERR : str = "ERR"; break;
            default: str = "Lexical Error";
        }
        return str;
    }

}