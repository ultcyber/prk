package task.timer.model;

/**
 * The Enum EntityType. Exposes the limited number of classes to be used by @{link ManageEntity}.
 * @author Mateusz Trybulec
 */
public enum EntityType {
	
	
	/** User and its persistent class. */
	User("task.timer.model.User"),
	
	/** Record and its persistent class. */
	Record("task.timer.model.Record"),
	
	/** Project its persistent class.. */
	Project("task.timer.model.Project");
	
	/** The class type. */
	private String classType;
	
	/**
	 * Instantiates a new entity type.
	 *
	 * @param classType the class type
	 */
	EntityType(String classType){
		this.classType = classType;
	}
	
	/**
	 * Gets the class type.
	 *
	 * @return the class type
	 */
	public String getClassType(){
		return classType;
	}

}
