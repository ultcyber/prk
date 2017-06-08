/*
 * A FXML controller for the Login Window stage. Exposes methods for controlling the login process.
 * 
 */
package task.timer.view;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import task.timer.Main;
import task.timer.ViewLoader;
import task.timer.helper.Helper;
import task.timer.model.AbstractEntity;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.User;
import task.timer.model.FactoryCreator;


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
		
		
		loggedUser = login(username.getText());
		// Not null means user has successfully logged in
		if (loggedUser != null){
			
			ViewLoader<AnchorPane, Object> viewLoader = null;
			
			String permissions = loggedUser.getPermissions();
			if (permissions.equals("manager")){
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
		// If incorrect login, display error message
		else {
			errorBox.setText("Nieprawidłowe hasło i/lub nazwa użytkownika");
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
	public User login(String login) throws NoSuchAlgorithmException{
		
		User user = Main.getMMUser().getUserForLogin(login);
		if (user != null){
			if (user.getPassword().equals(Helper.encryptPassword(password.getText()))){
				return user;
			}
		}
		return null;
		}

	}
