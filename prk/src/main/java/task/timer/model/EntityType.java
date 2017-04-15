package task.timer.model;

public enum EntityType {
	
	
	User("task.timer.model.User"),
	Record("task.timer.model.Record"),
	Task("task.timer.model.Task"),
	Project("task.timer.model.Project");
	
	private String classType;
	
	EntityType(String classType){
		this.classType = classType;
	}
	
	public String getClassType(){
		return classType;
	}

}
