package one;
import java.awt.Color;

public class NumberConversion {

	
	// Convert two bytes (little endian) into a short value
	public int byteShort(int a, int b) {
		
		int c = a + (256*b);
		
		return c;
	}
	
	// Convert eight bytes into a long (little endian)
	public long bytesLong (int[] bytes) {
		
		long finalCount = 0;
		
		for (int i = 0; i < 8; i++) {
			
			int temp = bytes[i];
			finalCount += temp*(Math.pow(256, i));
			
		}
		
		return finalCount;
		
	}
	
	// Convert int gamemode value into the String of their corresponding gamemode
	public String resolveGamemode(int a) {
		
		String[] gamemodes = {"Softcore", "Mediumcore", "Hardcore", "Journey"};
		String game = gamemodes[a];
		return game;
	}
	
	// Converts color to string "{r, g, b}" to be printed out
	public String ColortoString(Color a) {
		
		String draft = "{";
		
		draft += a.getRed();
		draft += ", ";
		draft += a.getGreen();
		draft += ", ";
		draft += a.getBlue();
		draft += "}";
		
		return draft;
	}
	
	public String valToString(int[] values) {
		
		String draft = "";
		char buffer;
		
		
		for (int i = 0; i < values.length; i++) {
			
			buffer = (char) values[i];
			draft += buffer;
			buffer = (char) 0;
			
		}
		
		return draft;
	}
	
	
	// Convert byte array to int, little endian.
	public long bytesInt(int[] bytes) {
		
		// Long because.. signed.
		long finalCount = 0;
		
		for (int i = 0; i < 4; i++) {
			
			int temp = bytes[i];
			finalCount += temp*(Math.pow(256, i));
			
		}
		
		return finalCount;
	}
	
	public String spawnPointToString(SpawnPoint sp) {
		
		long xpos = sp.getX();
		long ypos = sp.getY();
		long id = sp.getID();
		String worldname = sp.getWorldName();
		
		String draft = "Position: (" + xpos + ", " + ypos + "); World ID: " + id + "; " + "Worldname: " + worldname;
		
		return draft;
	}
	
	public String researchItemToString(ResearchItem re) {
		
		int quantity = re.getQuantity();
		String name = re.getName();
		
		String output = "[" + name + "]: " + quantity;
		
		return output;
	}
	
	// Convert gender code to gender and clothes style
	public String genderCodetoString(int genderCode) {
		
		String genderString;
		
		// Decode gender
		if (genderCode <= 3 || genderCode == 8) {
			
			genderString = "Male";
			
		}
		
		else {
			
			genderString = "Female";
			
		}
		
		// Decode clothes style
		int[] clothesNumbers = {1, 3, 2, 4, 1, 3, 2, 4, 5, 5};
		
		int clothesNumber = clothesNumbers[genderCode];
		
		String output = genderString + ", Style " + clothesNumber; 
		
		return output;
	}
	
	public boolean intToBool(int e) {
		
		int y = e;
		boolean ret;
		
		if (y >= 1) {
			
			ret = true;
			
		}
		
		else {
			
			ret = false;
			
		}
		
		return ret;
	
	}
	
	public String inventoryItemToString(InventoryItem item) {
		
		int itemID, quantity, prefixID;
		boolean favorited;
		
		itemID = item.getID();
		quantity = item.getQuantity();
		prefixID = item.getPrefixID();
		favorited = item.getFavorited();
		
		String output = prefixID + " prefix " + quantity + "x " + itemID;
		
		if (favorited == true) {
			
			output += "- favorited";
			
		}
		
		if (quantity == 0) {
			
			output = "none";
			
		}
		
		return output;
		
	}
	
	public String itemToString(Item item) {
		
		int itemID, quantity, prefixID;
		
		itemID = item.getID();
		quantity = item.getQuantity();
		prefixID = item.getPrefixID();
		
		String output = prefixID + " prefix " + quantity + "x " + itemID;
		
		if (quantity == 0) {
			
			output = "none";
			
		}
		
		return output;
		
	}
		
	public String buffToString(Buff buff) {
		
		int buffID, duration;
		
		buffID = buff.getID();
		duration = buff.getDuration();
		
		String output = "Buff " + buffID + " @ " + (duration/60) + " seconds";
		
		if (duration <= 0) {
			
			output = "none";
			
		}
		
		return output;
		
	}
	
}
