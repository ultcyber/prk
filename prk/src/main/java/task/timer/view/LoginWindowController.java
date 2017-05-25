package task.timer.view;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import task.timer.ViewLoader;
import task.timer.model.AbstractEntity;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.User;

public class LoginWindowController {
	ManageEntity MM = new MEFactory().getUserEntityManager();
	
	@FXML
	private TextField username;	
	@FXML
	private TextField password;
	@FXML
	private Label errorBox;
	
	@FXML
	private void processLogin(ActionEvent event) throws IOException {
		
		User loggedUser = login(username.getText(), password.getText());
		// Not null means user has successfully logged in
		if (loggedUser != null){
			ViewLoader<AnchorPane, Object> viewLoader = new ViewLoader<AnchorPane, Object>("view/MainEmployeer.fxml");
			MainEmployeerController controller = (MainEmployeerController) viewLoader.getController();
			controller.setLoggedUser(loggedUser);
			
			Scene scene = new Scene(viewLoader.getLayout());
			Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();            
			appStage.setScene(scene);
            appStage.show();
		
		}
		// If incorrect login, display error message
		else {
			errorBox.setText("Nieprawidłowe hasło i/lub nazwa użytkownika");
		}
	}
	
	@FXML
	public User login(String username, String password ){
		List<AbstractEntity> users = MM.list();
		for (AbstractEntity user : users){
			User thisUser = (User) user;
			
			if (thisUser.getLogin().equals(username) && thisUser.getPassword().equals(password)){
				return thisUser;
			}
		}
		return null;
	}
}
