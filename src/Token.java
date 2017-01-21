

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
    public final static int SPACE = 15;
    public final static int ENTER = 16;
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
    public final static int LOOP = 27;
    public final static int DELOOP = 28;
    public final static int IF = 29;
    public final static int IFE = 30;
    public final static int ENDIF = 31;
    public final static int DOT = 32;
    public final static int LT = 33;
    public final static int LE = 34;
    public final static int GT = 35;
    public final static int GE = 36;
    public final static int EE = 37;
    public final static int NE = 38;
    public final static int ELSE = 39;
    public final static int DEF = 40;
    public final static int METHOD = 41;
    public final static int DEFEND = 42;
    public final static int CALL = 43;

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
            else if ( temp.compareTo("loop") == 0) type = LOOP;
            else if ( temp.compareTo("deloop") == 0) type = DELOOP;
            else if ( temp.compareTo("if") == 0) type = IF;
            else if ( temp.compareTo("ife") == 0) type = IFE;
            else if ( temp.compareTo("endif") == 0) type = ENDIF;
            else if ( temp.compareTo("else") == 0) type = ELSE;
            else if ( temp.compareTo("space") == 0) type = SPACE;
            else if ( temp.compareTo("enter") == 0) type = ENTER;
            else if ( temp.compareTo("def") == 0) type = DEF;
            else if ( temp.compareTo("ined") == 0) type = DEFEND;
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
            case LOOP : str = "LOOP"; break;
            case DELOOP : str = "DELOOP"; break;
            case IF : str = "IF"; break;
            case IFE : str = "IFE"; break;
            case ENDIF : str = "ENDIF"; break;
            case ELSE : str = "ELSE"; break;
            case DOT : str = "DOT"; break;
            case LT : str = "LT"; break;
            case LE : str = "LE"; break;
            case GT : str = "GT"; break;
            case GE : str = "GE"; break;
            case EE : str = "EE"; break;
            case NE : str = "NE"; break;
            case SPACE : str = "SPACE"; break;
            case ENTER : str = "ENTER"; break;
            case DEF : str = "DEF"; break;
            case DEFEND : str = "DEFENd"; break;
            case METHOD : str = "METHOD"; break;
            case CALL : str = "CALL"; break;
            default: str = "Lexical Error";
        }
        return str;
    }

}