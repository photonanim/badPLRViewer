package one;

public class SpawnPoint {
	
	private long xpos, ypos, worldID;
	private String name;

	// Constructor
	public SpawnPoint(long x, long y, long ID, String worldname) {
		xpos = x;
		ypos = y;
		worldID = ID;
		name = worldname;
	}
	
	// Constructor overload
	public SpawnPoint() {
		xpos = 0;
		ypos = 0;
		worldID = 0;
		name = "test";
	}
	
	public long getX() {
		
		return this.xpos;
		
	}
	
	public long getY() {
		
		return this.ypos;
		
	}
	
	public long getID() {
		
		return this.worldID;
		
	}
	
	public String getWorldName() {
		
		return this.name;
		
	}
	
	public void setX(long x) {
		
		this.xpos = x;
		
	}
	
	public void setY(long y) {
		
		this.ypos = y;
		
	}
	
	public void setID(long id) {
		
		this.worldID = id;
		
	}
	
	public void setWorldName(String worldname) {
		
		this.name = worldname;
		
	}
	
}
