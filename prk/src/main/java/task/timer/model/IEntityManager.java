package task.timer.model;

import java.util.List;

public interface IEntityManager {
	
	Integer add(AbstractEntity entity);
	
	List<AbstractEntity> list();
	
	void update(int id, AbstractEntity compareEntity) throws ClassNotFoundException;
	
	void delete(int id) throws ClassNotFoundException;
	
}
