package one;

public class ResearchItem {
	
	private int quantity;
	private String itemName;
	
	public ResearchItem(int quant, String name) {
		
		quantity = quant;
		itemName = name;
		
	}
	
	public int getQuantity() {
		
		return this.quantity;
		
	}
	
	public String getName() {
		
		return this.itemName;
	}
	
	public void setQuantity(int quant) {
		
		this.quantity = quant;
		
	}
	
	public void setName(String name) {
		
		this.itemName = name;
		
	}
	
}
