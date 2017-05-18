package task.timer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;


public class Main extends Application{
	
	public void start(Stage primaryStage){
	

			
			ViewLoader<AnchorPane, Object> viewLoader = 
					//new ViewLoader<AnchorPane, Object>("view/MainEmployeer.fxml");
					new ViewLoader<AnchorPane, Object>("view/MainManager.fxml");
			
			AnchorPane anchorPane = viewLoader.getLayout();
			

		
		Scene scene = new Scene(anchorPane);
		primaryStage.setScene(scene);

		primaryStage.show();
			
	}

	public static void main(String[] args) {
		launch(args);

	}

}