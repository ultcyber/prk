package task.timer;

import org.hibernate.SessionFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;
import task.timer.model.FactoryCreator;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.Project;
import task.timer.model.Record;
import task.timer.model.User;


public class Main extends Application{
	
	
	private static SessionFactory sessionFactory;
	private static MEFactory manageEntity;	
	private static ManageEntity<User> MMUser;
	private static ManageEntity<Record> MMRecord;
	private static ManageEntity<Project> MMProject;
	
	public void start(Stage primaryStage) {

		sessionFactory = new FactoryCreator().getFactory();
		manageEntity = new MEFactory(sessionFactory);

		MMUser = manageEntity.getUserEntityManager();
		MMRecord = manageEntity.getRecordEntityManager();
		MMProject = manageEntity.getProjectEntityManager();

		ViewLoader<AnchorPane, Object> viewLoader =	new ViewLoader<AnchorPane, Object>("view/LoginWindow.fxml");

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
	
	/**
	 * @return the MMUser
	 */
	public static ManageEntity<User> getMMUser() {
		return MMUser;
	}

	/**
	 * @return the MMRecord
	 */
	public static ManageEntity<Record> getMMRecord() {
		return MMRecord;
	}
	
	/**
	 * @return the MMProject
	 */
	public static ManageEntity<Project> getMMProject() {
		return MMProject;
	}

	
	
	
}