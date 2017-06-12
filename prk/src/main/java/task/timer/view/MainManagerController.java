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

public class MainManagerController {
	
	@FXML private Label loggedUserName;
	@FXML private AnchorPane project;
	@FXML private GridPane gridpane;
	@FXML private TabPane tabpane;
	@FXML private VBox vbox;
	
	@FXML private ManagerTabAddProjectController projectController;
	@FXML private ManagerTabAddEmployeerController employeerController;
	@FXML private ManagerTabSearchController searchController;
	
	@FXML private void initialize(){		
		//System.out.println(anchorpane.isResizable());
		//tabpane.requestLayout();
		//tabpane.prefWidthProperty().bind(gridpane.prefWidthProperty());
		//tabpane.prefHeightProperty().bind(vbox.prefHeightProperty());
	
	
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
		employeerController.hideLackMessages();
		employeerController.hidePassword();
		employeerController.resetSetting();
	}
	
	@FXML private void readUsersAndProjects(){
		searchController.refreshChooseUser();
		searchController.refreshChooseProject();	
		searchController.clearFields();
	}
	
	@FXML private void logout(MouseEvent event){
		
		ViewLoader<AnchorPane, Object> viewLoader = new ViewLoader<AnchorPane, Object>("view/LoginWindow.fxml");
		Helper.changeStage(viewLoader, event);
		
	}

	
	
	
}
