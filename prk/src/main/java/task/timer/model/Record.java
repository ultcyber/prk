package task.timer.model;
import java.sql.Date;
import java.util.Set;

import javax.persistence.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

// TODO: Add Task connection

@Entity
@Table(name="RECORDS")

public class Record extends AbstractEntity {
	
	 private int id;
	 private User user;	
	 private Project project;
	 private Date timeStart;
	 private Date timeStop;
	 
	public Record(User user, Project project, Date timeStart, Date timeStop) {
		this.user = user;
		this.project = project;
		this.timeStart = timeStart;
		this.timeStop = timeStop;
	}
	
	public Record(){
	
	}

	@Id @GeneratedValue
	@Column(name = "id")
	public int getId() {
		return id;
	}
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="user")
	public User getUser() {
		return user;
	}
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="project")
	public Project getProject() {
		return project;
	}

	@Column(name="timeStart")
	public Date getTimeStart() {
		return timeStart;
	}

	@Column(name="timeStop")
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
