//import java.io.FileInputStream;          
import java.io.IOException;         
import java.io.FileReader;       
import java.io.BufferedReader;

public class Scanner
{    
    public static FileReader fileIn;
    public static BufferedReader bufReader;
    public String fileName;
    
    private int currentLineNumber;
    private String currentLine;
    private String nextLine;
    private int currentLocation;
    
    public Scanner(String fname)
    {
        currentLineNumber = 1;
        fileName = fname;
        try
        {
            fileIn = new FileReader(fileName);
            bufReader = new BufferedReader(fileIn);
            
            currentLine = bufReader.readLine();
            currentLocation = 0;
            if (currentLine == null )
            {
                nextLine = null;
            } else
            {
                nextLine = bufReader.readLine();
            }
        }
        catch (IOException e)
        {
            System.out.println(e);
            return;
        }
    }        
    
    public int getLineNumber()
    {
        return currentLineNumber;
    }
    
    public Token findNextToken()
    {
        int len = currentLine.length();
        String tokenStr = new String();
        int tokenType;
        if ( currentLocation >= len && nextLine == null) 
        {
            Token token = new Token( "", Token.EOF );
            return token;
        }
        if ( currentLocation >= len ) // all characters of currentLine used
        {
            currentLine = nextLine;
            currentLineNumber++;
            try
            {
                nextLine = bufReader.readLine();
            }
            catch (IOException e)
            {
                System.out.println(e);
                Token token = new Token( "", Token.EOF );
                return token;
            } 
            currentLocation = 0;
        }
        while ( Character.isWhitespace( currentLine.charAt(currentLocation)))
            currentLocation++;
        int i = currentLocation;
        if (currentLine.charAt(i) == ';')
        {
            tokenStr = ";";
            tokenType = Token.SEMICOLON;
            i++;
        } else if (currentLine.charAt(i) == '(')
        {
            tokenStr = "(";
            tokenType = Token.LPAREN;
            i++;
        } else if (currentLine.charAt(i) == ')')
        {
            tokenStr = ")";
            tokenType = Token.RPAREN;
            i++;
        } else if(currentLine.charAt(i) == '+')
        {
            tokenStr = "+";
            tokenType = Token.PLUS;
            i++;
        } else if(currentLine.charAt(i) == '-')
        {
            tokenStr = "-";
            tokenType = Token.MINUS;
            i++;
        } else if(currentLine.charAt(i) == ',')
        {
            tokenStr = ",";
            tokenType = Token.COMMA;
            i++;
        } else if (currentLine.charAt(i) == ':'  && i+1 < len && currentLine.charAt(i+1) == '=')
        {
            tokenStr = ":=";
            tokenType = Token.ASSIGNOP;
            i+=2;
        } else if (currentLine.charAt(i) == '*')					// Multiplication
        {
            tokenStr = "*";
            tokenType = Token.MULT;
            i++;
        } else if (currentLine.charAt(i) == '/')					// Division
        {
            tokenStr = "/";
            tokenType = Token.DIV;
            i++;
        } else if (currentLine.charAt(i) == '%')					// Modulo
        {
            tokenStr = "%";
            tokenType = Token.MOD;
            i++;
        } else if (currentLine.charAt(i) == '~'  && i+1 < len)		// Data Types
        {
        	if (currentLine.charAt(i+1) == 'i') {
	            tokenStr = "~i";
	            tokenType = Token.INTDT;
        	} else if (currentLine.charAt(i+1) == 'b') {
	            tokenStr = "~b";
	            tokenType = Token.BOOLDT;
        	} else {
        		// Error - Invalid data type
        		tokenStr = "~" + currentLine.charAt(i+1);
	            tokenType = Token.ERR;
        		System.out.println("ERROR! " +  tokenStr + " is not a valid data type.");	
        	}

            i+=2;
        } else  if ( Character.isDigit((currentLine.charAt(i))) )// find literals
        {
            while ( i < len && Character.isDigit(currentLine.charAt(i)) )
            {
                i++;
            }
            tokenStr = currentLine.substring(currentLocation, i);
            tokenType = Token.INTLITERAL;
            
        } else if (currentLine.charAt(i) == '!'  && i+1 < len && currentLine.charAt(i+1) == '=')
        {
            tokenStr = "!=";
            tokenType = Token.NE;
            i+=2;
            
        }  else if (currentLine.charAt(i) == '=')
        {
            tokenStr = "=";
            tokenType = Token.EE;
            i++;
            
        } else if (currentLine.charAt(i) == '<'  && i+1 < len && currentLine.charAt(i+1) == '=')
        {
            tokenStr = "<=";
            tokenType = Token.LE;
            i+=2;
            
        }  else if (currentLine.charAt(i) == '<')
        {
            tokenStr = "<";
            tokenType = Token.LT;
            i++;
            
        } else if (currentLine.charAt(i) == '>'  && i+1 < len && currentLine.charAt(i+1) == '=')
        {
            tokenStr = ">=";
            tokenType = Token.GE;
            i+=2;
            
        } else if (currentLine.charAt(i) == '>')
        {
            tokenStr = ">";
            tokenType = Token.GT;
            i++;
            
        } else if (currentLine.charAt(i) == '.')
        {
            tokenStr = ".";
            tokenType = Token.DOT;
            i++;
            
        } else // find identifiers and reserved words
        {
            while ( i < len && ! isReservedSymbol(currentLine.charAt(i)) )
            {
                i++;
            }
            tokenStr = currentLine.substring(currentLocation, i);
            tokenType = Token.ID;
        }
       
        currentLocation = i;
        Token token = new Token(tokenStr, tokenType);
        if ( i == len )// characters on currentLine used up
        {
            currentLine = nextLine;
            currentLineNumber++;
            try
            {
                nextLine = bufReader.readLine();
            }
            catch (IOException e)
            {
                System.out.println(e);
                return null;
            }
            currentLocation = 0;
        }

        return token;
    }
 
    boolean isReservedSymbol( char ch)
    {
        return( ch == ' ' || ch == '\n' || ch == '\t' || ch == ';' | ch == '+' ||
                ch == '-' || ch == '(' || ch == ')' || ch == ','  || ch == ':' || 
                ch == '~' || ch == '<' || ch == '>' || ch == '!' || ch == '=' );
    }

}