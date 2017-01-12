import java.util.Vector; 

class SymbolTable
{
    private Vector<Token> tableDetails;        
    private Vector<String> st;
    
    public SymbolTable()
    {
        st = new Vector<>();	// Create new symbol table
        tableDetails = new Vector<>();
    }
    
    public void addItem( Token token )
    {
        st.add( token.getId() );
        tableDetails.add( token );
    }
    
    public boolean checkSTforItem( String id )
    {
       return st.contains( id );
    }

    public Token getToken( String id )
    {
    	for (int i = 0; i < st.size(); i++) {
    		if (tableDetails.get(i).getId() == id) {
    			return tableDetails.get(i);
    		}
    	}
    	
    	return null;
    }
}