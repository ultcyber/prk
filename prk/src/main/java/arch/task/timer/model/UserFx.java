package task.timer.model;

import javafx.beans.property.SimpleStringProperty;

public class UserFx {
	 private final SimpleStringProperty userName;
	 
	 public UserFx(String userName){
		 this.userName = new SimpleStringProperty(userName);
	 }

	public SimpleStringProperty getUserName() {
		return userName;
	}
	 
	 

}
