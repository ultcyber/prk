package task.timer.model;
import org.hibernate.SessionFactory;

/**
 * A factory for creating ME objects.
 * @author Mateusz Trybulec
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
	public ManageEntity<User> getUserEntityManager(){
		if (sessionFactory != null)
			return new ManageEntity<User>(sessionFactory, EntityType.User);
		else
			return new ManageEntity<User>(EntityType.User);
	}
	
	/**
	 * Gets the project entity manager.
	 *
	 * @return the project entity manager
	 */
	public ManageEntity<Project> getProjectEntityManager(){
		if (sessionFactory != null)
			return new ManageEntity<Project>(sessionFactory, EntityType.Project);
		else
			return new ManageEntity<Project>(EntityType.Project);
	}
	
	/**
	 * Gets the record entity manager.
	 *
	 * @return the record entity manager
	 */
	public ManageEntity<Record> getRecordEntityManager(){
		if (sessionFactory != null)
			return new ManageEntity<Record>(sessionFactory, EntityType.Record);
		else
			return new ManageEntity<Record>(EntityType.Record);
	}
	
	
}
