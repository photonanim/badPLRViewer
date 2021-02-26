package one;

import java.util.Arrays;
import java.awt.Color;

public class ReadPlayerData {
	
	// Class Instance
	NumberConversion nc = new NumberConversion();
	
	// Reads the version number of the file, stored in the first two bytes. Little Endian.
	public int readVersion(int[] data) {
		
		int a = data[0];
		int b = data[1];
		
		int c = nc.byteShort(a, b);
		
		return c;
	}
	
	// Reads the player name, starting at offset 24 (dec)
	public String readPlayerName(int[] data) {
		
		int offset = 24;
		int nameLength = data[offset];
		
		String name = "";
		
		// Loop through and store the name as a character array.
		for (int i = 25; i < 25+nameLength; i++) {
			name = (name+(char) data[i]);
		}
		
		return name;
	}
	
	// Reads the gamemode data- the byte after the name data ends.
	public int readGamemode(int[] data, int offset) {
		
		int gamemode = data[offset];
		
		return gamemode;
		
	}
	
	// Reads the health/mana data, starting 18 bytes after the name data ends.
	public int[] resolveHPMana(int[] data, int offset) {
		
		int startingOffset = offset;
		
		int[] temp = new int[4];
		
		int[] dataShorts = new int[8];
		
		for (int j = 0; j < 4; j++) {
		
			for (int i = 0; i < 4; i++) {
			
				temp[i] = data[startingOffset+i];
			
			}
			
		startingOffset += 4;
		dataShorts[j] = nc.byteShort(temp[0],temp[1]);
		Arrays.fill(temp, 0);
		
		}
		
		return dataShorts;
	}
	
	// Reads the hairstyle ID, 9 bytes after the end of the name data.
	public int readHairstyle(int[] data, int offset) {
		
		int hairstyleID = data[offset];
		
		return hairstyleID;
		
	}
	
	// Reads the 7 color values used in the character selection
	public Color[] readPlayerColor(int[] data, int offset) {
		
		int leOffset = offset;
		int[] temp = new int[4];
		Color[] colors = new Color[7];
		
		for (int j = 0; j < 7; j++) {
		
			for (int i = 0; i < 3; i++) {
			
				temp[i] = data[leOffset+i];
			
			}
			
			leOffset += 3;
			Color tempcolor = new Color(temp[0], temp[1], temp[2]);
			colors[j] = tempcolor;
			Arrays.fill(temp, 0);
			
		}
		
		return colors;
	}
	
	// Reads 64-bit playtime value, located 2 bytes after end of name data
	public long readPlaytime(int[] data, int offset) {
		
		int localOffset = offset;
		int[] bytes = new int[8];
		
		for (int i = 0; i < 8; i++) {
			bytes[i] = data[localOffset+i];
		}

		long ret = nc.bytesLong(bytes);
		
		return ret;
	}
	
	public String getVersion(int versionID) {
		
		String output = "";
		
		if (versionID >= 231) {
			output = "1.4.1.x";
		}
		if (versionID == 230) {
			output = "1.4.0.5";
		}
		if (versionID < 229) {
			throw new IllegalArgumentException("Player file too old to work correctly!");
		}
		
		return output;
	}
	
	// Reads spawn point data (reads min. 13 bytes)
	public SpawnPoint readSpawnpoint(int[] data, int offset) {
		
		int internalOffset = offset;
		
		int[] xBytes = new int[4];
		int[] yBytes = new int[4];
		int[] idBytes = new int[4];
		
		for (int i = 0; i < 4; i++) {
			
			xBytes[i] = data[internalOffset+i];
			
		}
		
		internalOffset += 4;
		
		for (int i = 0; i < 4; i++) {
			
			yBytes[i] = data[internalOffset+i];
			
		}
		
		internalOffset += 4;
		
		for (int i = 0; i < 4; i++) {
			
			idBytes[i] = data[internalOffset+i];
			
		}
		
		internalOffset += 4;
		
		int nameLength = data[internalOffset];
		
		internalOffset += 1;
		
		int[] nameBytes = new int[nameLength];
		
		for (int i = 0; i < nameLength; i++) {
			
			nameBytes[i] = data[internalOffset+i];
			
		}
		
		// Data extracted in bytes
		
		// World name becomes string
		String worldName = nc.valToString(nameBytes);
		
		// Byte arrays to int.
		long x = nc.bytesInt(xBytes);
		long y = nc.bytesInt(yBytes);
		long id = nc.bytesInt(idBytes);
		
		SpawnPoint spawn = new SpawnPoint(x, y, id, worldName);
		
		return spawn;
	}
	
	// Find the spawn point data offsets
	public int[] findSpawnPointOffsets(int[] data, int playerNameLength) {
		
		final int playerNameDataEnd = 24+playerNameLength;
		
		int currentOffset = 2461+playerNameDataEnd;
		
		int[] offsetList = new int[201];
		
		Arrays.fill(offsetList, 0);
		
		int[] terminatorCheckTemp = new int[4];
		
		boolean bool = false;
		
		int counter = 0;
		
		while (bool == false) {
		
			// Checks for 0xFFFFFFFF terminator value
			for (int i = 0; i < 4; i++) {
			
				terminatorCheckTemp[i] = data[currentOffset+i];
			
			}
		
			long terminatorCheckValue = nc.bytesInt(terminatorCheckTemp);
		
			// Coulda checked for the terminator itself but Java is fucking retarded and won't let me compare it to the 32 bit integer limit because it's "out of range for an int" when I'm trying to compare with a long
			// Anyways 10,000,000 is way above what the value in the spot the terminator would be could hit without being the terminator anyways.
			if (terminatorCheckValue < 10000000) {
			
				offsetList[counter] = currentOffset;
				counter += 1;
				currentOffset += 13+data[currentOffset+12];
			
			}
			
			else {
				
				bool = true;
				
			}
		
		}
		
		// Bundle amount of offsets at the end of the list
		offsetList[200] = counter;
		
		return offsetList;
	}
	
	
	// Fishing quests, stored as a 32-bit value little endian, 4 bytes before the Great Terminator
	public int readFishingQuests(int[] data, int offset) {
		
		int[] internalList = new int[4];
		int output = 0;
		
		for (int i = 0; i < 4; i++) {
			
			internalList[i] = data[offset+i];
			
		}
		
		for (int i = 0; i < 4; i++) {
			
			int temp = internalList[i];
			output += temp*(Math.pow(256, i));
			
		}
		
		return output;
		
	}
	
	// Returns starting offset of the Great Terminator, a 16-byte wall of 0xFF values near the end of the file.
	public int findTheGreatTerminator(int[] data) {
		
		int counter = 0;
		int lePosition = 0;
		boolean found = false;
		
		for (int i = 0; i < data.length; i++) {
			
			if (data[i] == 255 && found == false) {
				
				counter += 1;
				lePosition = i;
				
			}
			
			else if (data[i] != 255 && found == false) {
				
				counter = 0;
				
			}
			
			if (counter == 16 && found == false) {
				
				lePosition = i-15;
				found = true;
				
			}
			
		}
		
		return lePosition;
		
	}
	
	
	// Returns tier of OOA event completed, stored as 32-bit number 64 bits after the Great Terminator
	public int readBartenderQuests(int[] data, int offset) {
		
		int internalOffset = offset;
		
		int[] internalList = new int[4];
		
		for (int i = 0; i < 4; i++) {
			
			internalList[i] = data[internalOffset+i];
			
		}
		
		int output = (int) nc.bytesInt(internalList);
		
		return output;
		
	}
	
	// Returns list of researched items
	public ResearchItem[] readResearch(int[] data, int offset) {
		
		int startingOffset = offset;
		
		int[] elementsBytes = new int[4];
		
		for (int i = 0; i < 4; i++) {
			
			elementsBytes[i] = data[startingOffset+i];
			
		}
		
		startingOffset += 4;
		
		int totalElements = (int) nc.bytesInt(elementsBytes);
		
		ResearchItem[] finalList = new ResearchItem[totalElements];
		
		for (int i = 0; i < totalElements; i++) {
			
			int itemLength = data[startingOffset];
			startingOffset += 1;
			
			int[] temp = new int[itemLength];
			
			for (int j = 0; j < itemLength; j++) {
				
				temp[j] = data[startingOffset+j];
				
			}
			
			startingOffset += itemLength;
			
			int[] quantityBytes = new int[4];
			
			for (int j = 0; j < 4; j++) {
				
				quantityBytes[j] = data[startingOffset+j];
				
			}
			
			int quantity = (int) nc.bytesInt(quantityBytes);
			
			String itemName = nc.valToString(temp);
			
			startingOffset += 4;
			
			ResearchItem temporary = new ResearchItem(quantity, itemName);
			
			finalList[i] = temporary;
			
		}
		
		return finalList;
	}
	
	public int readGenderCode(int[] data, int offset) {
		
		int genderCode = data[offset];
		
		return genderCode;
		
	}
	
	// Reads inventory data.
	public InventoryItem[] readInventory(int[] data, int offset) {
		
		int startingOffset = offset;
		
		InventoryItem[] finalList = new InventoryItem[50];
		
		for (int i = 0; i < 50; i++) {
			
			int itemID, quantity, prefixID;
			boolean favorited;
			
			itemID = nc.byteShort(data[startingOffset], data[startingOffset+1]);
			quantity = nc.byteShort(data[startingOffset+4], data[startingOffset+5]);
			prefixID = data[startingOffset+8];
			favorited = nc.intToBool(data[startingOffset+9]);
			
			InventoryItem tempItem = new InventoryItem(itemID, quantity, prefixID, favorited);
			
			finalList[i] = tempItem;
			
			startingOffset += 10;
			
		}
		
		return finalList;
		
	}
	
	// Reads general item data. Difference from inventory is that there is no boolean for favoriting
	public Item[] readItems(int[] data, int offset, int length) {
		
		int startingOffset = offset;
		
		Item[] finalList = new Item[length];
		
		for (int i = 0; i < length; i++) {
			
			int itemID, quantity, prefixID;
			
			itemID = nc.byteShort(data[startingOffset], data[startingOffset+1]);
			
			quantity = nc.byteShort(data[startingOffset+4], data[startingOffset+5]);
			
			prefixID = data[startingOffset+8];
			
			Item tempItem = new Item(itemID, quantity, prefixID);
			
			finalList[i] = tempItem;
			
			startingOffset += 9;
			
		}
		
		return finalList;
	}
	
	public Buff[] readBuffs(int[] data, int offset) {
		
		int startingOffset = offset;
		
		Buff[] finalList = new Buff[22];
		
		for (int i = 0; i < 22; i++) {
			
			int ID, dura;
			
			ID = nc.byteShort(data[startingOffset], data[startingOffset+1]);
			
			dura = data[startingOffset+4] + ((data[startingOffset+5])*256) + ((data[startingOffset+6])*65536);
			
			Buff tempBuff = new Buff(ID, dura);
			
			finalList[i] = tempBuff;
			
			startingOffset += 8;
			
		}
		
		return finalList;
		
	}
	
	// Reads coins/ammo data.
	public InventoryItem[] readCoins(int[] data, int offset) {
		
		int startingOffset = offset;
		
		InventoryItem[] finalList = new InventoryItem[8];
		
		for (int i = 0; i < 8; i++) {
			
			int itemID, quantity, prefixID;
			boolean favorited;
			
			itemID = nc.byteShort(data[startingOffset], data[startingOffset+1]);
			quantity = nc.byteShort(data[startingOffset+4], data[startingOffset+5]);
			prefixID = data[startingOffset+8];
			favorited = nc.intToBool(data[startingOffset+9]);
			
			InventoryItem tempItem = new InventoryItem(itemID, quantity, prefixID, favorited);
			
			finalList[i] = tempItem;
			
			startingOffset += 10;
			
		}
		
		return finalList;
		
	}
	
	// Reads equipment
	public Item[] readEquipment(int[] data, int offset, int length) {
		
		int startingOffset = offset;
		
		Item[] finalList = new Item[length];
		
		for (int i = 0; i < length; i++) {
			
			int itemID, quantity, prefixID;
			
			itemID = nc.byteShort(data[startingOffset], data[startingOffset+1]);
			
			if (itemID > 0) {
				quantity = 1;
			}
			
			else {
				
				quantity = 0;
				
			}
			
			prefixID = data[startingOffset+4];
			
			Item tempItem = new Item(itemID, quantity, prefixID);
			
			finalList[i] = tempItem;
			
			startingOffset += 5;
			
		}
		
		return finalList;
		
	}
	
}
