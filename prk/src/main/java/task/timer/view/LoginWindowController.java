/*
 * A FXML controller for the Login Window stage. Exposes methods for controlling the login process.
 * 
 */
package task.timer.view;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import task.timer.Main;
import task.timer.ViewLoader;
import task.timer.helper.Helper;
import task.timer.model.User;


/**
 * Class LoginWindowController.
 * @author Mateusz Trybulec
 */
public class LoginWindowController {
	
	/** Currently logged in	user. Exposed statically for other controllers. */
	public static User loggedUser;
		
	/** Username field. */
	@FXML
	private TextField username;	
	
	/** Password field. */
	@FXML
	private TextField password;
	
	/** Error box. */
	@FXML
	private Label errorBox;
	
	/**
	 * Process login. Log user or display appropriate text in the error box.
	 *
	 * @param ActionEvent on the clicked button.
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws NoSuchAlgorithmException signals the no such algorithm exception.
	 */
	@FXML
	private void processLogin(ActionEvent event) throws IOException, NoSuchAlgorithmException {
		
		Pair<User,String> pair = login(username.getText());
		loggedUser = pair.getKey();
		// Not null means user has successfully logged in
		if (loggedUser != null){
			
			ViewLoader<AnchorPane, Object> viewLoader = null;
			
			String permissions = loggedUser.getPermissions();
			if (permissions.equals("manager") || permissions.equals("administrator")){
				viewLoader = new ViewLoader<AnchorPane, Object>("view/MainManager.fxml");

			}
			else if (permissions.equals("pracownik")) {
				viewLoader = new ViewLoader<AnchorPane, Object>("view/MainEmployeer.fxml");

			}
			else {
				errorBox.setText("Nie masz odpowiednich uprawnień do zalogowania się do aplikacji.\n Skontaktuj się z administratorem.");
				return;
			}
			
			Helper.changeStage(viewLoader, event);
		
		}
		// If incorrect login or database error, display error message
		else {
			errorBox.setText(pair.getValue());
		}
	}
	
	/**
	 * Gets user for given login, checks password match and returns a User object
	 *
	 * @param login name
	 * @return User object
	 * @throws NoSuchAlgorithmException the no such algorithm exception for encryption handler.
	 */
	@FXML
	public Pair<User, String> login(String login) throws NoSuchAlgorithmException {

		if (!Main.getMMUser().ping())
			return new Pair<User, String>(null, "Błąd połączenia z bazą danych");

		User user = Main.getMMUser().getUserForLogin(login);
		if (user != null) {
			if (user.getPassword().equals(Helper.encryptPassword(password.getText()))) {
				return new Pair<User, String>(user, "Zalogowano");
			}

		}
		return new Pair<User, String>(null, "Nieprawidłowa nazwa użytkownika lub hasło");
	}
}
