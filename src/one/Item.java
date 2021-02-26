package one;

public class Item {
	
	private int id, quantity, prefixID;

	// Constructor
	public Item(int itemID, int quant, int prefix) {
		
		id = itemID;
		quantity = quant;
		prefixID = prefix;
		
	}
	
	public int getID() {
		
		return this.id;
		
	}
	
	public int getQuantity() {
		
		return this.quantity;
		
	}
	
	public int getPrefixID() {
		
		return this.prefixID;
	}
	
	public void setID(int set) {
		
		this.id = set;
		
	}
	
	public void setQuantity (int set) {
		
		this.quantity = set;
	}
	
	public void setPrefixID(int set) {
		
		this.prefixID = set;
		
	}
	
}
