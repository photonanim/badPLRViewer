package one;

public class InventoryItem extends Item{

	private boolean favorited;

	// Constructor
	public InventoryItem(int itemID, int quant, int prefix, boolean fav) {
		
		super(itemID, quant, prefix);
		favorited = fav;
		
	}
	
	public boolean getFavorited() {
		
		return this.favorited;
		
	}
	
	public void setFavorited(boolean set) {
		
		this.favorited = set;
		
	}
	
}
