package task.timer.model;
import java.sql.Date;
import java.util.Set;

import javax.persistence.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

// TODO: Add Task connection

@Entity
@Table(name="RECORDS")

public class Record {
	 @Id @GeneratedValue
	 @Column(name = "id")
	 private int id;
	 
	 private User user;	 

	 @Column(name="project")
	 private Project project;
	 
	 @Column(name="timeStart")
	 private Date timeStart;
	 
	 @Column(name="timeStop")
	 private Date timeStop;
	 
	 
	 
	 public IntegerProperty getIntegerPropertyId(){
		 SimpleIntegerProperty prop = new SimpleIntegerProperty();
		 prop.setValue(id);
		 return prop;
	 }

	public Record(User user, Project project, Date timeStart, Date timeStop) {
		this.user = user;
		this.project = project;
		this.timeStart = timeStart;
		this.timeStop = timeStop;
	}

	public int getId() {
		return id;
	}
	
	@ManyToOne(cascade=CascadeType.ALL)
	public User getUser() {
		return user;
	}
	
	@ManyToOne(cascade=CascadeType.ALL)
	public Project getProject() {
		return project;
	}

	public Date getTimeStart() {
		return timeStart;
	}

	public Date getTimeStop() {
		return timeStop;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public void setTimeStop(Date timeStop) {
		this.timeStop = timeStop;
	}
	 
}
