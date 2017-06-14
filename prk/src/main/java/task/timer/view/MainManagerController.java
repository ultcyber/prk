package task.timer.view;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TabPane;
import javafx.fxml.FXML;
import task.timer.ViewLoader;
import task.timer.helper.Helper;

/**
 * The Class MainManagerController.
 * @author Marcin Zglenicki
 * @since JDK 1.8
 */
public class MainManagerController {
	
	/** The logged user name. */
	@FXML private Label loggedUserName;
	
	/** The project controller. */
	@FXML private ManagerTabAddProjectController projectController;
	
	/** The employeer controller. */
	@FXML private ManagerTabAddEmployeerController employeerController;
	
	/** The search controller. */
	@FXML private ManagerTabSearchController searchController;
	
	/**
	 * Initialize.
	 */
	@FXML private void initialize(){		
	
		loggedUserName.setText(
				LoginWindowController.loggedUser.getFirstName() 
				+ " " 
				+ LoginWindowController.loggedUser.getLastName());
	}
	
	/**
	 * Clear fields in tab projects.
	 */
	@FXML private void clearTabProjects(){
		projectController.clearFields();
	}
	
	/**
	 * Clear fields in tab employeer.
	 */
	@FXML private void clearTabEmployeer(){
		employeerController.clearFields();
		employeerController.hideLackMessages();
		employeerController.hidePassword();
		employeerController.resetSetting();
	}
	
	/**
	 * Read users and projects for tab search.
	 */
	@FXML private void readUsersAndProjects(){
		searchController.refreshChooseUser();
		searchController.refreshChooseProject();	
		searchController.clearFields();
	}
	
	/**
	 * Logout.
	 *
	 * @param event the event
	 */
	@FXML private void logout(MouseEvent event){
		
		ViewLoader<AnchorPane, Object> viewLoader = new ViewLoader<AnchorPane, Object>("view/LoginWindow.fxml");
		Helper.changeStage(viewLoader, event);		
	}	
}
