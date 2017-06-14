package task.timer.model;

/**
 * An abstract layer for interactions with the database.
 * 
 * Keeps the consistency of types returned by {@link ManageEntity}
 * @author Mateusz Trybulec
 * 
 */
public abstract class AbstractEntity {
	
	/** The entity id. */
	private int id;

	@Override
	public int hashCode() {
		final int prime = 1000000007;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity other = (AbstractEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}

