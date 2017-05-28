package task.timer.model;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import javax.persistence.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// TODO: Add Task connection

@Entity
@Table(name="RECORDS")


public class Record extends AbstractEntity {
	@Id @GeneratedValue
	@Column(name = "id")
	private int id;

	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="user")
	private User user;	

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="project")
	private Project project;
	
	@Column(name="description")
	private String description;	

	@Column(name="date")
	private LocalDate date;

	@Column(name="timeStart")
	private LocalTime timeStart;

	@Column(name="timeStop")
	private LocalTime timeStop;
	 
	public Record(User user, Project project, String description, LocalDate date, LocalTime timeStart, LocalTime timeStop) {
		this.user = user;
		this.project = project;
		this.date = date;
		this.description = description;
		this.timeStart = timeStart;
		this.timeStop = timeStop;
	}
	
	public Record(){
	
	}



	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public IntegerProperty getIdProperty(){
		SimpleIntegerProperty idProperty = new SimpleIntegerProperty(id);
		return idProperty;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public StringProperty getUserName(){
		SimpleStringProperty userNameProperty = new SimpleStringProperty(user.getFirstName() + " " + user.getLastName());
		return userNameProperty;
	}
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public StringProperty getDescriptionProperty(){
		SimpleStringProperty descriptionProperty = new SimpleStringProperty(description);
		return descriptionProperty;
	}
	
	public StringProperty getProjectName(){
		SimpleStringProperty projectNameProperty = new SimpleStringProperty(project.getName());
		return projectNameProperty;
	}

	public LocalTime getTimeStart() {
		return timeStart;
	}
	
	public void setTimeStart(LocalTime timeStart) {
		this.timeStart = timeStart;
	}
	
	public StringProperty getTimeStartProperty(){
		SimpleStringProperty timeStartProperty = new SimpleStringProperty(timeStart.toString());
		return timeStartProperty;
	}
	
	public LocalTime getTimeStop() {
		return timeStop;
	}

	public void setTimeStop(LocalTime timeStop) {
		this.timeStop = timeStop;
	}
	
	public StringProperty getTimeStopProperty(){
		SimpleStringProperty timeStopProperty = new SimpleStringProperty(timeStop.toString());
		return timeStopProperty;
	}
	
	public LocalDate getDate() {
		return date;
	}

	public StringProperty getDateProperty(){
		SimpleStringProperty dateProperty = new SimpleStringProperty(date.toString());
		return dateProperty;
	}
	 
}
