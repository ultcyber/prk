package task.timer.model;

import java.util.List;


/**
 * The Interface IEntityManager. Provides an abstract layer for implementing a DAO class.
 * @author Mateusz Trybulec
 */
public interface IEntityManager {
	
	
	/**
	 * Adds the entities.
	 *
	 * @param entity the entity
	 * @return the unique id of the added AbstractEntity.
	 */
	Integer add(AbstractEntity entity);
	
	
	/**
	 * Lists all entities.
	 *
	 * @return the list of AbstractEntity from the database.
	 */
	List<AbstractEntity> list();
	
	
	/**
	 * Update the given entity.
	 *
	 * @param newEntity the new entity to be updated.
	 * @throws ClassNotFoundException the class not found exception
	 */
	void update(AbstractEntity newEntity) throws ClassNotFoundException;
	
	
	/**
	 * Delete.
	 *
	 * @param id the id
	 * @throws ClassNotFoundException the class not found exception
	 */
	void delete(int id) throws ClassNotFoundException;
	
	/**
	 * The standard fail handler.
	 *
	 * @param e the e
	 */
	void fail(Throwable e);
	
}
