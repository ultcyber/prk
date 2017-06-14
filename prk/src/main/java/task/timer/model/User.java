package task.timer.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * User hibernate model.
 * @author Mateusz Trybulec
 */
@Entity
@Table(name="USERS")

public class User extends AbstractEntity {
	 

	/** The id. */
 	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	 @Column(name = "id")
	 private int id;
	 
	 /** The login. */
 	@Column (name = "login")
	 private String login;
	 
	 /** The first name. */
 	@Column (name="firstName")
	 private String firstName;
	 
	 /** The last name. */
 	@Column (name="lastName")
	 private String lastName;
	 
	 /** The password. */
 	@Column (name="password")
	 private String password;
	 
	 /** The permissions. */
 	@Column (name="permissions")
	 private String permissions;
	 
	 /** The editing flag. */
 	@Column (name="editing")
	 private boolean editing;
	 
	 /** The reminder flag. */
 	@Column (name="reminder")
	 private boolean reminder;
	 
	 /** The projects. */
 	@ManyToMany(fetch = FetchType.EAGER, mappedBy="users", cascade = CascadeType.MERGE)  
	 private Set<Project> projects = new HashSet<Project>();
	 
	 /** The records. */
 	@OneToMany(fetch = FetchType.EAGER, mappedBy="user", cascade = CascadeType.REMOVE)
		private Set<Record> records;
	 	
	/**
	 * Instantiates a new user.
	 *
	 * @param login the login
	 * @param password the password
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param permissions the permissions
	 * @param editing the editing flag.
	 * @param reminder the reminder flag.
	 */
	public User(String login, String password, String firstName, String lastName, String permissions, boolean editing, boolean reminder) {
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.permissions = permissions;
		this.editing = editing;
		this.reminder = reminder;
	}
	
	/**
	 * Instantiates a new user.
	 *
	 * @param id the id
	 * @param login the login
	 * @param password the password
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param permissions the permissions
	 * @param editing the editing flag.
	 * @param reminder the reminder flag.
	 */
	public User(int id, String login, String password, String firstName, String lastName, String permissions, boolean editing, boolean reminder) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.permissions = permissions;
		this.editing = editing;
		this.reminder = reminder;
	}
	
	/**
	 * Instantiates a new user.
	 */
	public User(){
	
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
	 * Gets the login.
	 *
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the permissions.
	 *
	 * @return the permissions
	 */
	public String getPermissions() {
		return permissions;
	}
	
	/**
	 * Gets the editing flag.
	 *
	 * @return the editing flag
	 */
	public boolean getEditing(){
		return editing;
	}
	
	/**
	 * Gets the reminder lag.
	 *
	 * @return the reminder flag
	 */
	public boolean getReminder(){
		return reminder;
	}
	
	/**
	 * Gets the id converted to IntegerProperty.
	 *
	 * @return the id property
	 */
	public IntegerProperty getIdProperty(){
		SimpleIntegerProperty idProperty = new SimpleIntegerProperty(id);
		return idProperty;
	}

	/**
	 * Gets the first name converted to IntegerProperty.
	 *
	 * @return the first name property
	 */
	public StringProperty getFirstNameProperty(){
		SimpleStringProperty firstNameProperty = new SimpleStringProperty(firstName);
		return firstNameProperty;
	}

	/**
	 * Gets the last name converted to StringProperty.
	 *
	 * @return the last name property
	 */
	public StringProperty getLastNameProperty(){
		SimpleStringProperty lastNameProperty = new SimpleStringProperty(lastName);
		return lastNameProperty;
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
	 * Sets the login.
	 *
	 * @param login the new login
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Sets the permissions.
	 *
	 * @param permissions the new permissions
	 */
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	
	/**
	 * Sets the editing flag.
	 *
	 * @param editing the new editing fag
	 */
	public void setEditing(boolean editing) {
		this.editing = editing;
	}
	
	/**
	 * Sets the reminder flag.
	 *
	 * @param reminder the new reminder flag
	 */
	public void setReminder(boolean reminder){
		this.reminder = reminder;
	}
	 
	/**
	 * Gets the projects assigned to the User.
	 *
	 * @return the projects
	 */
	public Set<Project> getProjects() {
	    return projects;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return firstName + " " + lastName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 1000000007;
		int result = super.hashCode();
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + id;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (editing != other.editing)
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id != other.id)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (permissions == null) {
			if (other.permissions != null)
				return false;
		} else if (!permissions.equals(other.permissions))
			return false;
		if (reminder != other.reminder)
			return false;
		return true;
	}

	
	
	
}