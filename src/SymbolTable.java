import java.util.HashMap;
import java.util.Vector; 

class SymbolTable
{
	private HashMap<String, Integer> symbolTable;
	private Vector<String> st;
    
	public SymbolTable() {
        st = new Vector<>();
        symbolTable = new HashMap<>();
    }
	
	public void addItem( Token token, Token preToken ) {
		// Add item to vector for a quick lookup
        st.add(token.getId());
        
        if (preToken.getType() == Token.BOOLDT || preToken.getType() == Token.INTDT) {
        	// Save the variables data type with it
        	symbolTable.put(token.getId(), preToken.getType());
        } else {
        	// Error - undeclared type
        	System.out.println("ERROR - Variable " + token.getId() + " has an undeclared type.");
        	System.out.println("Variable " + token.getId() + " has not been added to the symbol table.");
        }
    }
    
	public void addItem( String var, int datatype ) {
    	// Save the variables data type with it
    	symbolTable.put(var, datatype);
    }
	
    public boolean checkSTforItem( String id ) {
       return st.contains( id );
    }

    public int checkSTforType( Token token ) {
        return symbolTable.get(token.getId());
     }
}