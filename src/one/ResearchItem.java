package one;

public class ResearchItem extends Item{
	private String itemName;
	
	public ResearchItem(int quant, String name) {
		super(ItemsCsv.idByItems.get(name), quant, 0);
		itemName = name;
	}
	
	public String getName() {
		
		return this.itemName;
	}
	
	public void setName(String name) {
		
		this.itemName = name;
		
	}
	
}
