package task.timer.view;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.hibernate.SessionFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import task.timer.ViewLoader;
import task.timer.helper.Helper;
import task.timer.model.AbstractEntity;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.User;
import task.timer.model.FactoryCreator;

public class LoginWindowController {
	
	public static User loggedUser;
	
	
	@FXML
	private TextField username;	
	@FXML
	private TextField password;
	@FXML
	private Label errorBox;
	
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
	
	@FXML
	public User login(String login) throws NoSuchAlgorithmException{
		User user = DAO.MMUser.getUserForLogin(login);
		if (user != null){
			if (user.getPassword().equals(Helper.encryptPassword(password.getText()))){
				return user;
			}
		}
		return null;
	}
}
