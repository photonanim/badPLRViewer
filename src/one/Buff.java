package one;

public class Buff {

	private int buffID, duration;
	
	public Buff(int ID, int dura) {
		
		buffID = ID;
		duration = dura;
		
	}
	
	public int getID() {
		
		return this.buffID;
		
	}
	
	public int getDuration() {
		
		return this.duration;
		
	}
	
	public void setID(int a) {
		
		this.buffID = a;
		
	}
	
	public void getDuration(int a) {
		
		this.duration = a;
		
	}
	
}
