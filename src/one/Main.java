package one;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.io.*;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.awt.Color;

public class Main {

	public static void main(String[] args) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, FileNotFoundException, IOException {
		
		
		/*
		 * Extracts the raw player data.
		 * The player data must be decrypted with AES-128 before being used.
		 */
		
		
		// Class Instances
		ExtractPlayerData epd = new ExtractPlayerData();
		ReadPlayerData rpd = new ReadPlayerData();
		NumberConversion nc = new NumberConversion();
		
		// AES Password from Terraria, decode to UTF16LE. (Done externally here because I'm stupid)
		String password = "h3y_gUyZ";
		
		// Big Endian Decryption Key (incorrect)
        //byte[] pass = {0, 104, 0, 51, 0, 121, 0, 95, 0, 103, 0, 85, 0, 121, 0, 90};
		
		// Little Endian Decryption Key (C# uses UTF16LE)
        byte[] pass = {104, 0, 51, 0, 121, 0, 95, 0, 103, 0, 85, 0, 121, 0, 90, 0};
        
        // Debug file file path.
		// String debugpath = "";
		
		// Decrypted file file path.
		String outputpath = "";
		
		// Input file file path.
		byte[] data = epd.ExtractRawData("");
		
		
		// DEBUG
		// File that has not attempted decryption.
		//try (FileOutputStream stream = new FileOutputStream(debugpath)) {
		//	
		//    stream.write(data);
		//}
		
		// Decryption
		byte[] decrypted = epd.DecryptRawData(data, pass);
		
		// Decrypted File
		try (FileOutputStream stream = new FileOutputStream(outputpath)) {
		    stream.write(decrypted);
		}
		
		System.out.println("Decryption successful.");
		
		
		// Converting signed byte array into an int array of unsigned byte (int) values.
		int[] finalData = new int[decrypted.length];
		for (int i = 0; i < decrypted.length; i++) {
		    finalData[i] = decrypted[i] & 0xFF;
		}
		
		/*
		 * Reading actual player data (decrypted)
		 */
		
		// Find the Great Terminator
		int greatTerminatorOffset = rpd.findTheGreatTerminator(finalData);
		
		// Read version
		int versionNumber = rpd.readVersion(finalData);
		// Read name
		String playerName = rpd.readPlayerName(finalData);
		// Read gamemode
		int gamemode = rpd.readGamemode(finalData, 25+playerName.length());
		// Read HP/Mana
		int[] hpManaData = rpd.resolveHPMana(finalData, 43+playerName.length());
		// Read Hairstyle Data
		int hairstyleID = rpd.readHairstyle(finalData, 34+playerName.length());
		// Read Colors Data
		Color[] playerColors = new Color[7];
		playerColors = rpd.readPlayerColor(finalData, 67+playerName.length());
		// Read Playtime Data
		long playtime = rpd.readPlaytime(finalData, 26+playerName.length());
		// Find Fishing Quests Data
		int fishingQuests = rpd.readFishingQuests(finalData, greatTerminatorOffset-4);
		// Find "bartender quests" (what OOA was completed)
		int bartenderQuests = rpd.readBartenderQuests(finalData, greatTerminatorOffset+64);
		// Find gender code
		int genderCode = rpd.readGenderCode(finalData, 42+playerName.length());
		// Decode gender code
		String genderClothes = nc.genderCodetoString(genderCode);
		// Find Inventory data
		InventoryItem[] inventory = new InventoryItem[50];
		inventory = rpd.readInventory(finalData, 238+playerName.length());
		// Find Piggybank data
		Item[] piggybank = new Item[40];
		piggybank = rpd.readItems(finalData, 868+playerName.length(), 40);
		// Find Safe data
		Item[] safe = new Item[40];
		safe = rpd.readItems(finalData, 1228+playerName.length(), 40);
		// Find forge data
		Item[] forge = new Item[40];
		forge = rpd.readItems(finalData, 1588+playerName.length(), 40);
		// Read void bag data
		Item[] voidBag = new Item[40];
		voidBag = rpd.readItems(finalData, 1948+playerName.length(), 40);
		// Read buff data
		Buff[] buffs = new Buff[22];
		buffs = rpd.readBuffs(finalData, 2309+playerName.length());
		// Read coins/ammo data
		InventoryItem[] coins = new InventoryItem[8];
		coins = rpd.readCoins(finalData, 738+playerName.length());
		// Read equipment, part 1
		Item[] equip = new Item[30];
		equip = rpd.readEquipment(finalData, 88+playerName.length(), 30);
		// Read equipment, part 2
		Item[] equip_cont = new Item[10];
		equip_cont = rpd.readEquipment(finalData, 818+playerName.length(), 10);
		
		// Version check
		if (versionNumber < 230) {
			throw new IllegalArgumentException("Outdated or invalid version code. This program will not work!");
		}
		
		// Print basic data
		System.out.println("Version Number: " + versionNumber);
		System.out.println("Likely version: " + rpd.getVersion(versionNumber));
		System.out.println("Player Name: " + playerName);
		System.out.println("Gamemode: " + nc.resolveGamemode(gamemode));
		System.out.println("HP: " + hpManaData[0] + " out of " + hpManaData[1]);
		System.out.println("Mana: " + hpManaData[2] + " out of " + hpManaData[3]);
		System.out.println("Hairstyle ID: " + hairstyleID);
		System.out.println("Playtime: " + (playtime/10000000) + " seconds");
		System.out.println("Fishing Quests Complete: " + fishingQuests);
		System.out.println("Tier of OOA Event Completed: " + bartenderQuests);
		System.out.println("Gender/Clothes: " + genderClothes);
		
		// Print Color data.
		String[] playerColorTypes = {"Hair Color: ", "Skin Color: ", "Eye Color: ", "Shirt Color: ",  "Undershirt Color: ", "Pants Color: ", "Shoe Color: "};
		
		for (int i = 0; i < 7; i ++) {
			
			System.out.println(playerColorTypes[i] + nc.ColortoString(playerColors[i]));
		
		}
		
		// Find spawn points.
		int[] offsets = rpd.findSpawnPointOffsets(finalData, playerName.length());
		String[] spawnPointStrings = new String[offsets[200]];
		
		for (int i = 0; i < offsets[200]; i++) {
			
			int offset = offsets[i];
			
			SpawnPoint s = rpd.readSpawnpoint(finalData, offset);
			
			spawnPointStrings[i] = nc.spawnPointToString(s);
			
		}
		
		System.out.println();
		
		// Print spawn points.
		System.out.println("Spawn Points:");
		for (int i = 0; i < spawnPointStrings.length; i++) {
			
			System.out.println(spawnPointStrings[i]);
			
		}
		
		// Find research data
		ResearchItem[] researchList = rpd.readResearch(finalData, greatTerminatorOffset+81);
		String[] researchStrings = new String[researchList.length];
		
		
		for (int i = 0; i < researchList.length; i++) {
			
			researchStrings[i] = nc.researchItemToString(researchList[i]);
			
		}
		
		System.out.println();
		
		// Print out research data.
		System.out.println("Researched items (" + researchList.length + "):");
		
		for (int i = 0; i < researchList.length; i++) {
			
			System.out.println(researchStrings[i]);
			
		}

		printInventoryItem("Inventory: ", inventory, 0, 50, 1, 1, 1, 1);

		printItem("Piggy Bank: ", piggybank, 0, 40, 1, 1, 1, 1);
		
		printItem("Safe: ", safe, 0, 40, 1, 1, 1, 1);
		
		printItem("Defender's Forge: ", forge, 0, 40, 1, 1, 1, 1);

		printItem("Void Bag: ", voidBag, 0, 40, 1, 1, 1, 1);
		
		// Print out buffs, separate loop because there's only one, so not worth making a method
		
		System.out.println();
		System.out.print("Buffs: ");
		
		for (int i = 0; i < 22; i++) {
			
			String temp = "(" + (i+1) + ") " + nc.buffToString(buffs[i]);
			
			System.out.print(temp);
			
			if (i < 21) {
				
				System.out.print(", ");
				
			}
			
		}

		printInventoryItem("Coins: ", coins, 0, 4, 1, 1, 1, 1);
		
		printInventoryItem("Ammo: ", coins, 4, 8, 1, 1, -3, 1);
		
		printItem("Armor: ", equip, 0, 3, 1, 1, 1, 1);
		
		printItem("Accessories: ", equip, 3, 10, 1, 1, -2, 1);
		
		printItem("Vanity: ", equip, 10, 20, 1, 1, -9, 1);
		
		printItem("Dyes: ", equip, 20, 30, 1, 1, -19, 1);
		
		printItem("Equipment (Pet/Light/Minecart/Mount/Hook): ", equip_cont, 0, 10, 0.5, 2, 1, 3);
		
		printItem("Equipment Dyes (Pet/Light/Minecart/Mount/Hook): ", equip_cont, 1, 11, 0.5, 2, 1, 2);
	
	}

	public static void printItem(String header, Item[] list, int startCount, int endCount, double xFactor, int xIncrement, int tempInc, int finalDec) {
		
		System.out.println();
		System.out.print(header);
		
		NumberConversion nc = new NumberConversion();
		
		for (int i = startCount; i < endCount; i += xIncrement) {
			
			String temp = "(" + ((int)(i*xFactor)+tempInc) + ") " + nc.itemToString(list[i]);
			
			System.out.print(temp);
			
			if (i < endCount-finalDec) {
				
				System.out.print(", ");
				
			}
			
		}
		
	}
	
	public static void printInventoryItem(String header, InventoryItem[] list, int startCount, int endCount, double xFactor, int xIncrement, int tempInc, int finalDec) {
		
		System.out.println();
		System.out.print(header);
		
		NumberConversion nc = new NumberConversion();
		
		for (int i = startCount; i < endCount; i += xIncrement) {
			
			String temp = "(" + ((int)(i*xFactor)+tempInc) + ") " + nc.inventoryItemToString(list[i]);
			
			System.out.print(temp);
			
			if (i < endCount-finalDec) {
				
				System.out.print(", ");
				
			}
			
		}
		
	}

}
