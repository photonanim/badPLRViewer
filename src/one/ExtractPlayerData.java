package one;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.crypto.*;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class ExtractPlayerData 
{
	
	public byte[] ExtractRawData(String filePath)
	{
		byte[] data;
		try {
			
			// Convert file into bytes.
			
			data = Files.readAllBytes(Paths.get(filePath));
			return data;
		} catch (IOException e) {
			
			// Error checking.
			
			e.printStackTrace();
			throw new IllegalArgumentException("Something wrong happened : probably something with the file path.");
		}
	}
		
    
	public byte[] DecryptRawData(byte[] rawData, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		// Class instance
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		
		// Set IV to the same 128 bits as the key (byte[])
		byte[] iv = key;
		
		// Set decryption parameters
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
		
		// Decrypt
		return cipher.doFinal(rawData);

	}
	
}
