package task.timer.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Helper {
	
	public static String encryptPassword(String pass) throws NoSuchAlgorithmException{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(pass.getBytes(StandardCharsets.UTF_8));
		return bytesToHex(hash);
	}
	
	private static String bytesToHex(byte[] bytes) {
	    StringBuffer result = new StringBuffer();
	    for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
	    return result.toString();
	 }
	
	public static void main (String[] args) throws NoSuchAlgorithmException{
		System.out.println(Helper.encryptPassword("password"));
	}
	
}
