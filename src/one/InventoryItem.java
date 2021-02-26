package one;

public class InventoryItem {

	private int id, quantity, prefixID;
	private boolean favorited;

	// Constructor
	public InventoryItem(int itemID, int quant, int prefix, boolean fav) {
		
		id = itemID;
		quantity = quant;
		prefixID = prefix;
		favorited = fav;
		
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
	
	public boolean getFavorited() {
		
		return this.favorited;
		
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
	
	public void setFavorited(boolean set) {
		
		this.favorited = set;
		
	}
	
}
