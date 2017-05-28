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
	public static ManageEntity MMUser = new MEFactory().getUserEntityManager();
	public static ManageEntity MMRecord = new MEFactory().getRecordEntityManager();
	public static ManageEntity MMProject = new MEFactory().getProjectEntityManager();
	
	public static User loggedUser;
	
	
	@FXML
	private TextField username;	
	@FXML
	private TextField password;
	@FXML
	private Label errorBox;
	
	@FXML
	private void processLogin(ActionEvent event) throws IOException {
		
		loggedUser = login();
		// Not null means user has successfully logged in
		if (loggedUser != null){
			ViewLoader<AnchorPane, Object> viewLoader = new ViewLoader<AnchorPane, Object>("view/MainManager.fxml");
					
			/*MainEmployeerController controller = (MainEmployeerController) viewLoader.getController();
			controller.setLoggedUser(loggedUser);*/
			
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
	public User login(){
		List<AbstractEntity> users = MMUser.list();
		for (AbstractEntity user : users){
			User thisUser = (User) user;
			
			if (thisUser.getLogin().equals(username.getText()) && thisUser.getPassword().equals(password.getText())){
				return thisUser;
			}
		}
		return null;
	}
}
