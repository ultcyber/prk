package task.timer.view;

import org.hibernate.SessionFactory;

import task.timer.model.FactoryCreator;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;

public class DAO {
	private static SessionFactory sessionFactory = new FactoryCreator().getFactory();
	
	protected static ManageEntity MMUser = new MEFactory(sessionFactory).getUserEntityManager();
	protected static ManageEntity MMRecord = new MEFactory(sessionFactory).getRecordEntityManager();
	protected static ManageEntity MMProject = new MEFactory(sessionFactory).getProjectEntityManager();
	protected static ManageEntity MMTask = new MEFactory(sessionFactory).getTaskEntityManager();
	
	public DAO(){
	}
}
