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

/**
 * Utility functions for some operations.
 * @author Mateusz Trybulec
 */
public class Helper {
	
	/**
	 * Encrypt the password with SHA-256.
	 *
	 * @param pass the password to encrypt.
	 * @return the SHA-256 encrypted hex.
	 * @throws NoSuchAlgorithmException if SHA-256 algorithm is not found.
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
	 * @param bytes the bytes to convert to hex.
	 * @return the hex representation of bytes.
	 */
	private static String bytesToHex(byte[] bytes) {
	    StringBuffer result = new StringBuffer();
	    for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
	    return result.toString();
	 }
	
	
	/**
	 * A utility method for changing the stage to a different controller.
	 *
	 * @param <P> the Pane type.
	 * @param <O> the Controller class.
	 * @param viewLoader the {@link ViewLoader} instance to apply.
	 * @param event the action triggering event.
	 */
	// Utility methods
	public static <P, O> void changeStage(ViewLoader<P, O> viewLoader, Event event){
		
		Scene scene = new Scene((Parent) viewLoader.getLayout());
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		appStage.setScene(scene);
        appStage.show();
	}
	
}
