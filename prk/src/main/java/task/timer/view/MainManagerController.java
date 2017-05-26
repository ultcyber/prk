package task.timer.view;

import javafx.fxml.FXML;

public class MainManagerController {
	public static boolean changedData;
	
	@FXML private ManagerTabAddProjectController projectController;
	
	@FXML private void change(){
		if (changedData) {
			projectController.refreshAvailableUsersOnInterface();
			changedData = false;
		}
	}
	
}
