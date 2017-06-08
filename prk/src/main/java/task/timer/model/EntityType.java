package task.timer.model;

// TODO: Auto-generated Javadoc
/**
 * The Enum EntityType.
 */
public enum EntityType {
	
	
	/** The User. */
	User("task.timer.model.User"),
	
	/** The Record. */
	Record("task.timer.model.Record"),
	
	/** The Task. */
	Task("task.timer.model.Task"),
	
	/** The Project. */
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
