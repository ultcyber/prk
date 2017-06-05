package task.timer;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;


public class Main extends Application{
	
	public void start(Stage primaryStage){
			
			ViewLoader<AnchorPane, Object> viewLoader = 
					//new ViewLoader<AnchorPane, Object>("view/MainEmployeer.fxml");
					//new ViewLoader<AnchorPane, Object>("view/MainManager.fxml");
					new ViewLoader<AnchorPane, Object>("view/LoginWindow.fxml");

			
			AnchorPane anchorPane = viewLoader.getLayout();
			

		
		Scene scene = new Scene(anchorPane);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
			
	}
	
	public void stop(){
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);

	}
	
}