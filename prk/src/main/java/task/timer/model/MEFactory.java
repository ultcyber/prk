package task.timer.model;
import org.hibernate.SessionFactory;

public class MEFactory {
	
	private SessionFactory sessionFactory = null;
	
	public MEFactory(){
	}
	
	public MEFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	public ManageEntity getUserEntityManager(){
		if (sessionFactory != null)
			return new ManageEntity(sessionFactory, EntityType.User);
		else
			return new ManageEntity(EntityType.User);
	}
	
	public ManageEntity getProjectEntityManager(){
		if (sessionFactory != null)
			return new ManageEntity(sessionFactory, EntityType.Project);
		else
			return new ManageEntity(EntityType.Project);
	}
	
	public ManageEntity getRecordEntityManager(){
		if (sessionFactory != null)
			return new ManageEntity(sessionFactory, EntityType.Record);
		else
			return new ManageEntity(EntityType.Record);
	}
	
	public ManageEntity getTaskEntityManager(){
		if (sessionFactory != null)
			return new ManageEntity(sessionFactory, EntityType.Task);
		else
			return new ManageEntity(EntityType.Task);
	}
	
}
