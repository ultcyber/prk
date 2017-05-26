package task.timer.view;

import javafx.fxml.FXML;

public class MainManagerController {
	public static boolean changedUsersData;
	public static boolean changedProjectsData;
	
	@FXML private ManagerTabAddProjectController projectController;
	@FXML private ManagerTabSearchController searchController;
	
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
