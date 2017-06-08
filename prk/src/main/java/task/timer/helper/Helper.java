package task.timer.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import task.timer.ViewLoader;

// TODO: Auto-generated Javadoc
/**
 * The Class Helper.
 */
public class Helper {
	
	/**
	 * Encrypt password.
	 *
	 * @param pass the pass
	 * @return the string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	// Encrypting
	public static String encryptPassword(String pass) throws NoSuchAlgorithmException{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(pass.getBytes(StandardCharsets.UTF_8));
		return bytesToHex(hash);
	}
	
	/**
	 * Bytes to hex.
	 *
	 * @param bytes the bytes
	 * @return the string
	 */
	private static String bytesToHex(byte[] bytes) {
	    StringBuffer result = new StringBuffer();
	    for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
	    return result.toString();
	 }
	
	
	/**
	 * Change stage.
	 *
	 * @param <P> the generic type
	 * @param <O> the generic type
	 * @param viewLoader the view loader
	 * @param event the event
	 */
	// Utility methods
	public static <P, O> void changeStage(ViewLoader<P, O> viewLoader, Event event){
		
		Scene scene = new Scene((Parent) viewLoader.getLayout());
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		appStage.setScene(scene);
        appStage.show();
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public static void main (String[] args) throws NoSuchAlgorithmException{
		System.out.println(Helper.encryptPassword("password"));
	}
	
}
