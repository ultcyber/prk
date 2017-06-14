package task.timer.helper;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;

/**
 * Helper class for presenting the change password dialog for the user.
 * @author Mateusz Trybulec
 */
public class ChangePasswordDialog {
	
	/** The password hash. */
	private String passwordHash;
	
	/**
	 * Instantiates a new change password dialog.
	 */
	public ChangePasswordDialog() {
			
		Dialog<String> dialog = new Dialog<String>();
		dialog.setTitle("Zmiana hasła");
		dialog.setHeaderText("Wpisz nowe hasło oraz potwierdź.");


		// Set the button types.
		ButtonType changePasswordBtnType = new ButtonType("Zmień hasło", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(changePasswordBtnType, ButtonType.CANCEL);

		// Create the password and repeat password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		PasswordField password = new PasswordField();
		password.setPromptText("Hasło");
		PasswordField rpassword = new PasswordField();
		rpassword.setPromptText("Powtórz hasło");

		grid.add(new Label("Wpisz nowe hasło:"), 0, 0);
		grid.add(password, 1, 0);
		grid.add(new Label("Wpisz ponownie nowe hasło:"), 0, 1);
		grid.add(rpassword, 1, 1);

		// Enable/Disable login button depending on whether a password was entered.
		Node loginButton = dialog.getDialogPane().lookupButton(changePasswordBtnType);
		loginButton.setDisable(true);

		password.textProperty().addListener((observable, oldValue, newValue) -> {
		    loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the password field by default.
		Platform.runLater(() -> password.requestFocus());

		// Convert the result to a password-rpassword-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
			String passwordHash = null;
		    if (dialogButton == changePasswordBtnType) {
		    	if (password.getText().equals(rpassword.getText())){
		    		
		    		try {
						passwordHash = new Helper().encryptPassword(password.getText());
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
							
		    	}
		    	else {
		    		new AlertDialog("Błąd", "Różne hasła. Proszę spróbować jeszcze raz.", AlertType.INFORMATION);
		    	}
		        return passwordHash;
		    }
		    return null;
		});

		Optional<String> result = dialog.showAndWait();

		result.ifPresent(hash -> {
		    passwordHash = hash;
		});

	}
	
	/**
	 * Gets the password hash.
	 *
	 * @return the password hash
	 */
	public String getPasswordHash(){
		return passwordHash;
	}

}
