package task.timer.view;

import org.hibernate.SessionFactory;

import task.timer.model.FactoryCreator;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.Project;
import task.timer.model.Record;
import task.timer.model.User;

public class DAO {
	private static SessionFactory sessionFactory = new FactoryCreator().getFactory();
	
	private static MEFactory manageEntity = new MEFactory(sessionFactory);
	
	protected static ManageEntity<User> MMUser = manageEntity.getUserEntityManager();
	protected static ManageEntity<Record> MMRecord = manageEntity.getRecordEntityManager();
	protected static ManageEntity<Project> MMProject = manageEntity.getProjectEntityManager();
	
	public DAO(){
	}
}
