package task.timer;

import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application { 
	
	public void start(Stage primaryStage){
		
		ViewLoader<AnchorPane, Object> viewLoader = 
				new ViewLoader<AnchorPane, Object>("EmployeeData.fxml");
		
		AnchorPane anchorPane = viewLoader.getLayout();
		
		Scene scena = new Scene(anchorPane);
		primaryStage.setScene(scena);
		
		
		primaryStage.setOnCloseRequest(	e -> primaryStage_CloseRequest(e));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);

	}
	
	void primaryStage_CloseRequest(WindowEvent e) {
		Optional<ButtonType> result = AlertBox.showAndWait(
		AlertType.CONFIRMATION,
		"Kończenie pracy",
		"Czy chcesz zamknąć aplikację?");
		if (result.orElse(ButtonType.CANCEL) != ButtonType.OK)
		e.consume();
		}

}
