import java.io.FileInputStream;          
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
    // private boolean done;
    private int currentLocation;
    
    public Scanner(String fname)
    {
        currentLineNumber = 0;						// Start on line 1 **changed
        fileName = fname;
        try
        {
            fileIn = new FileReader(fileName);		// Read in file
            bufReader = new BufferedReader(fileIn);	
            
            currentLine = bufReader.readLine();		// Read in first line
            currentLocation = 0;					// Start at position 0
            if (currentLine == null )				// Quit if end of file
            {
                // done = true;					
                nextLine = null;
            } else									// Read in next line
            {
                // done = false;				
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
        
        if ( currentLocation >= len && nextLine == null) 	// End of line, end of file
        {
            Token token = new Token( "", Token.EOF );
            return token;
        }
        
        if ( currentLocation >= len ) 	// All characters of currentLine used, not end of file
        {
            currentLine = nextLine;		// Move to next line
            currentLineNumber++;
            try
            {
                nextLine = bufReader.readLine();	// Read in next line if it exists
            }
            catch (IOException e)					// Else, end of file
            {
                System.out.println(e);
                Token token = new Token( "", Token.EOF );
                return token;
            } 
            currentLocation = 0;		// Set current position to 0
        }
        
        // Skip whitespaces
        while ( Character.isWhitespace( currentLine.charAt(currentLocation)))
            currentLocation++;
        
        int i = currentLocation;
        if (currentLine.charAt(i) == ';')			// Declare token as ';'
        {
            tokenStr = ";";
            tokenType = Token.SEMICOLON;
            i++;
        } else if (currentLine.charAt(i) == '(')	// Declare token as '('
        {
            tokenStr = "(";
            tokenType = Token.LPAREN;
            i++;
        } else if (currentLine.charAt(i) == ')')	// Declare token as ')'
        {
            tokenStr = ")";
            tokenType = Token.RPAREN;
            i++;
        } else if(currentLine.charAt(i) == '+')		// Declare token as '+'
        {
            tokenStr = "+";
            tokenType = Token.PLUS;
            i++;
        } else if(currentLine.charAt(i) == '-')		// Declare token as '-'
        {
            tokenStr = "-";
            tokenType = Token.MINUS;
            i++;
        } else if(currentLine.charAt(i) == ',')		// Declare token as ','
        {
            tokenStr = ",";
            tokenType = Token.COMMA;
            i++;
        } else if (currentLine.charAt(i) == ':'  && i+1 < len && currentLine.charAt(i+1) == '=')	// Declare token as ':='
        {
            tokenStr = ":=";
            tokenType = Token.ASSIGNOP;
            i+=2;
        } else  if ( Character.isDigit((currentLine.charAt(i))) ) 	// Find and Declare int literals
        {
            while ( i < len && Character.isDigit(currentLine.charAt(i)) )
            {
                i++;
            }
            tokenStr = currentLine.substring(currentLocation, i);		
            tokenType = Token.INTLITERAL;
        } else if(currentLine.charAt(i) == '"')						// Declare token as " asci characters "
        {
        	i++;
        	
        	while ( i < len && currentLine.charAt(i) != '"' )
            {
                i++;
            }
        	        	
        	tokenStr = currentLine.substring(currentLocation + 1, i);	// Do not include beginning nor end quote	
            tokenType = Token.STRING;
            i++;
        } 
        else if(currentLine.charAt(i) == '~')							// Declare token as a data type
        {
        	while (i + 1 < len && currentLine.charAt(i + 1) == ' ') {
        		i++;
        	}
        	
        	if (i + 1 < len && currentLine.charAt(i + 1) == 's') {
        		i+=2;
	        	tokenStr = "~s";
	            tokenType = Token.STRINGDT;
        	} else if (i + 1 < len && currentLine.charAt(i + 1) == 'i') {
        		i+=2;
	        	tokenStr = "~i";
	            tokenType = Token.INTDT;
        	} else {
        		// Error: Not a corrent data type
        		tokenStr = currentLine.substring(currentLocation, i);
                tokenType = Token.ID;	
        		i++;
        	}
        } else 														// Find identifiers and reserved words
        {
            while ( i < len && ! isReservedSymbol(currentLine.charAt(i)) )
            {
                i++;
            }
            tokenStr = currentLine.substring(currentLocation, i);
            tokenType = Token.ID;
        }
       
        currentLocation = i;							// Start at position after everything above
        Token token = new Token(tokenStr, tokenType);	// Create new token
        if ( i == len )									// Characters on currentLine used up
        {
            currentLine = nextLine;						// End of line, go to next line
            currentLineNumber++;
            try
            {
                nextLine = bufReader.readLine();		// Try reading in new next line
            }
            catch (IOException e)						// End of file
            {
                System.out.println(e);
                return null;
            }
            currentLocation = 0;
        }
        // if (currentLine == null) { done = true; } // reached EOF 
        return token;
    }
 
    boolean isReservedSymbol( char ch)
    {
        return( ch == ' ' || ch == '\n' || ch == '\t' || ch == ';' | ch == '+' ||
                ch == '-' || ch == '(' || ch == ')' || ch == ','  || ch == ':' || ch == '~');
    }

}