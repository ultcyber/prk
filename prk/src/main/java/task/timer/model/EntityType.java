package task.timer.model;

public enum EntityType {
	
	User("User"),
	Record("Record"),
	Task("Task"),
	Project("Project");
	
	private String type;
	
	EntityType(String type){
		this.type = type;
	}

}
