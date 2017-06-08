package task.timer.model;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// TODO: Auto-generated Javadoc
/**
 * Record hibernate model.
 */
@Entity
@Table(name="RECORDS")


public class Record extends AbstractEntity {
	
	/** The id. */
	@Id @GeneratedValue
	@Column(name = "id")
	private int id;

	
	/** The user. */
	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="user")
	
	private User user;	

	/** The project. */
	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="project")
	private Project project;
	
	/** The description. */
	@Column(name="description")
	private String description;	

	/** The date. */
	@Column(name="date")
	private LocalDate date;

	/** The time start. */
	@Column(name="timeStart")
	private LocalTime timeStart;

	/** The time stop. */
	@Column(name="timeStop")
	private LocalTime timeStop;
	 
	
	/**
	 * Instantiates a new record.
	 *
	 * @param user the user
	 * @param project the project
	 * @param description the description
	 * @param date the date
	 * @param timeStart the time start
	 * @param timeStop the time stop
	 */
	public Record(User user, Project project, String description, LocalDate date, LocalTime timeStart, LocalTime timeStop) {
		this.user = user;
		this.project = project;
		this.date = date;
		this.description = description;
		this.timeStart = timeStart;
		this.timeStop = timeStop;
	}

	
	/**
	 * Instantiates a new record.
	 */
	public Record(){
	
	}



	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the id property.
	 *
	 * @return the id property
	 */
	public IntegerProperty getIdProperty(){
		SimpleIntegerProperty idProperty = new SimpleIntegerProperty(id);
		return idProperty;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public StringProperty getUserName(){
		SimpleStringProperty userNameProperty = new SimpleStringProperty(user.getFirstName() + " " + user.getLastName());
		return userNameProperty;
	}
	
	/**
	 * Gets the project.
	 *
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * Sets the project.
	 *
	 * @param project the new project
	 */
	public void setProject(Project project) {
		this.project = project;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the description property.
	 *
	 * @return the description property
	 */
	public StringProperty getDescriptionProperty(){
		SimpleStringProperty descriptionProperty = new SimpleStringProperty(description);
		return descriptionProperty;
	}
	
	/**
	 * Gets the project name.
	 *
	 * @return the project name
	 */
	public StringProperty getProjectName(){
		SimpleStringProperty projectNameProperty = new SimpleStringProperty(project.getName());
		return projectNameProperty;
	}

	/**
	 * Gets the time start.
	 *
	 * @return the time start
	 */
	public LocalTime getTimeStart() {
		return timeStart;
	}
	
	/**
	 * Sets the time start.
	 *
	 * @param timeStart the new time start
	 */
	public void setTimeStart(LocalTime timeStart) {
		this.timeStart = timeStart;
	}
	
	/**
	 * Gets the time start property.
	 *
	 * @return the time start property
	 */
	public StringProperty getTimeStartProperty(){
		SimpleStringProperty timeStartProperty = new SimpleStringProperty(String.format("%02d:%02d:%02d", timeStart.getHour(), timeStart.getMinute(), timeStart.getSecond()));
		return timeStartProperty;
	}
	
	/**
	 * Gets the time stop.
	 *
	 * @return the time stop
	 */
	public LocalTime getTimeStop() {
		return timeStop;
	}

	/**
	 * Sets the time stop.
	 *
	 * @param timeStop the new time stop
	 */
	public void setTimeStop(LocalTime timeStop) {
		this.timeStop = timeStop;
	}
	
	/**
	 * Gets the time stop property.
	 *
	 * @return the time stop property
	 */
	public StringProperty getTimeStopProperty(){
		SimpleStringProperty timeStopProperty = null;
		if (timeStop != null)
			timeStopProperty = new SimpleStringProperty(String.format("%02d:%02d:%02d", timeStop.getHour(), timeStop.getMinute(), timeStop.getSecond()));
		return timeStopProperty;
	}
	
	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Gets the date property.
	 *
	 * @return the date property
	 */
	public StringProperty getDateProperty(){
		SimpleStringProperty dateProperty = new SimpleStringProperty(date.toString());
		return dateProperty;
	}
	 
}
