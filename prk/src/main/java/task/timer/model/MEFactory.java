package task.timer.model;
import org.hibernate.SessionFactory;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating ME objects.
 */
public class MEFactory {
	
	/** The session factory. */
	private SessionFactory sessionFactory = null;
	
	/**
	 * Instantiates a new ME factory.
	 */
	public MEFactory(){
	}
	
	/**
	 * Instantiates a new ME factory.
	 *
	 * @param sessionFactory the session factory
	 */
	public MEFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * Gets the user entity manager.
	 *
	 * @return the user entity manager
	 */
	public ManageEntity getUserEntityManager(){
		if (sessionFactory != null)
			return new ManageEntity(sessionFactory, EntityType.User);
		else
			return new ManageEntity(EntityType.User);
	}
	
	/**
	 * Gets the project entity manager.
	 *
	 * @return the project entity manager
	 */
	public ManageEntity getProjectEntityManager(){
		if (sessionFactory != null)
			return new ManageEntity(sessionFactory, EntityType.Project);
		else
			return new ManageEntity(EntityType.Project);
	}
	
	/**
	 * Gets the record entity manager.
	 *
	 * @return the record entity manager
	 */
	public ManageEntity getRecordEntityManager(){
		if (sessionFactory != null)
			return new ManageEntity(sessionFactory, EntityType.Record);
		else
			return new ManageEntity(EntityType.Record);
	}
	
	/**
	 * Gets the task entity manager.
	 *
	 * @return the task entity manager
	 */
	public ManageEntity getTaskEntityManager(){
		if (sessionFactory != null)
			return new ManageEntity(sessionFactory, EntityType.Task);
		else
			return new ManageEntity(EntityType.Task);
	}
	
}
