package task.timer.view;

import org.hibernate.SessionFactory;

import javafx.scene.control.Alert.AlertType;
import task.timer.helper.AlertDialog;
import task.timer.model.FactoryCreator;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.Project;
import task.timer.model.Record;
import task.timer.model.User;

public final class DAO {
	private static SessionFactory sessionFactory;
	
	private static MEFactory manageEntity;
	
	protected static ManageEntity<User> MMUser;
	protected static ManageEntity<Record> MMRecord;
	protected static ManageEntity<Project> MMProject;
	
	private static final DAO instance = new DAO();
	
	private DAO(){

		sessionFactory = new FactoryCreator().getFactory();
		manageEntity = new MEFactory(sessionFactory);

		MMUser = manageEntity.getUserEntityManager();
		MMRecord = manageEntity.getRecordEntityManager();
		MMProject = manageEntity.getProjectEntityManager();
	}
	
	public static DAO getInstance(){
		return instance;
	}
	
}
