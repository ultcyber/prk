package task.timer.model;

import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * The Interface IEntityManager.
 */
public interface IEntityManager {
	
	
	/**
	 * Adds the.
	 *
	 * @param entity the entity
	 * @return the integer
	 */
	Integer add(AbstractEntity entity);
	
	
	/**
	 * List.
	 *
	 * @return the list
	 */
	List<AbstractEntity> list();
	
	
	/**
	 * Update.
	 *
	 * @param compareEntity the compare entity
	 * @throws ClassNotFoundException the class not found exception
	 */
	void update(AbstractEntity compareEntity) throws ClassNotFoundException;
	
	
	/**
	 * Delete.
	 *
	 * @param id the id
	 * @throws ClassNotFoundException the class not found exception
	 */
	void delete(int id) throws ClassNotFoundException;
	
	/**
	 * Fail.
	 *
	 * @param e the e
	 */
	void fail(Throwable e);
	
}
