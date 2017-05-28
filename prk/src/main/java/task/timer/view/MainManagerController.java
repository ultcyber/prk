package task.timer.view;

import javafx.scene.control.Label;

import javafx.fxml.FXML;
import task.timer.model.User;

public class MainManagerController {
	public static boolean changedUsersData;
	public static boolean changedProjectsData;
	
	
	@FXML private Label loggedUserName;
	
	@FXML private ManagerTabAddProjectController projectController;
	@FXML private ManagerTabSearchController searchController;
	
	@FXML private void initialize(){
		loggedUserName.setText(
				LoginWindowController.loggedUser.getFirstName() 
				+ " " 
				+ LoginWindowController.loggedUser.getLastName());
	}
	
	@FXML private void changedUsers(){
		if (changedUsersData) {
			projectController.refreshAvailableUsersOnInterface();
			changedUsersData = false;
		}
	}
	
	@FXML private void changedUsersAndProjects(){
		if (changedUsersData) {
			projectController.refreshAvailableUsersOnInterface();
			searchController.refreshChooseUser();
			changedUsersData = false;
		}
		if (changedProjectsData) {
			searchController.refreshChooseProject();
			changedProjectsData = false;
		}
		
	}

	
	
	
}
