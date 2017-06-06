package task.timer.view;

import javafx.scene.control.Label;

import javafx.fxml.FXML;
import task.timer.model.User;

public class MainManagerController {
/*	public static boolean changedUsersData;
	public static boolean changedProjectsData;*/
	
	
	@FXML private Label loggedUserName;
	
	@FXML private ManagerTabAddProjectController projectController;
	@FXML private ManagerTabAddEmployeerController employeerController;
	@FXML private ManagerTabSearchController searchController;
	
	@FXML private void initialize(){
		loggedUserName.setText(
				LoginWindowController.loggedUser.getFirstName() 
				+ " " 
				+ LoginWindowController.loggedUser.getLastName());
	}
	
	@FXML private void clearTabProjects(){
		projectController.clearFields();
	}
	
	@FXML private void clearTabEmployeer(){
		employeerController.clearFields();
		employeerController.hidePassword();
	}
	
	@FXML private void readUsersAndProjects(){
		searchController.refreshChooseUser();
		searchController.refreshChooseProject();	
		searchController.clearFields();
	}

	
	
	
}
