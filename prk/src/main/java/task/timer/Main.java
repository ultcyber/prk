package task.timer;

import org.hibernate.SessionFactory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import task.timer.model.FactoryCreator;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.Project;
import task.timer.model.Record;
import task.timer.model.User;


/**
 * Main class of TaskTimer.
 * @author Mateusz Trybulec & Marcin Zglenicki
 */
public class Main extends Application{
	
	
	/** The session factory. */
	private static SessionFactory sessionFactory;
	
	/** The ManageEntity Factory. */
	private static MEFactory manageEntity;	
	
	/** The ManageEntity for User. */
	private static ManageEntity<User> MMUser;
	
	/** The ManageEntity for Record. */
	private static ManageEntity<Record> MMRecord;
	
	/** The ManageEntity for Project. */
	private static ManageEntity<Project> MMProject;
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
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
		primaryStage.show();

	}
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#stop()
	 */
	public void stop(){
		System.exit(0);
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		launch(args);

	}
	
	/**
	 * Gets the ManageEntity object for User.
	 *
	 * @return the MMUser
	 */
	public static ManageEntity<User> getMMUser() {
		return MMUser;
	}

	/**
	 * Gets the ManageEntity object for Record.
	 *
	 * @return the MMRecord
	 */
	public static ManageEntity<Record> getMMRecord() {
		return MMRecord;
	}
	
	/**
	 * Gets the ManageEntity object for Project.
	 *
	 * @return the MMProject
	 */
	public static ManageEntity<Project> getMMProject() {
		return MMProject;
	}

	
	
	
}