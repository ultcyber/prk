package task.timer.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Project hibernate model.
 * @author Mateusz Trybulec
 */
@Entity
@Table(name = "PROJECTS")
public class Project extends AbstractEntity {
	
	/** The id. */
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	/** The records. */
	@OneToMany(fetch = FetchType.EAGER, mappedBy="project", cascade = CascadeType.REMOVE)
	private Set<Record> records;
	
	/** The name. */
	@Column(name = "name")
	private String name;

	
	/** The users. */
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "PROJECT_USER", 
			joinColumns = { @JoinColumn(name = "PROJECT_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "USER_ID") })	
	private Set<User> users = new HashSet<User>();
	

	/**
	 * Instantiates a new project.
	 *
	 * @param id the id
	 * @param name the name
	 * @param users the users
	 */
	public Project(int id, String name, Set<User> users){
		this.id = id;
		this.name = name;
		this.users = users;
	}
	
	/**
	 * Instantiates a new project.
	 *
	 * @param id the id
	 * @param name the name
	 */
	public Project(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Instantiates a new project.
	 *
	 * @param name the name
	 */
	public Project(String name){
		this.name = name;
	}
	
	/**
	 * Instantiates a new project.
	 */
	public Project(){
		
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
	 * Gets the project id converted to IntegerProperty.
	 *
	 * @return the project id property
	 */
	public IntegerProperty getIdProjectProperty(){
		SimpleIntegerProperty idProperty = new SimpleIntegerProperty(id);
		return idProperty;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the project name converted to StringProperty.
	 *
	 * @return project name property
	 */
	public StringProperty getNameProjectProperty(){
		SimpleStringProperty nameProjectProperty = new SimpleStringProperty(name);
		return nameProjectProperty;
	}
		
	/**
	 * Gets the users.
	 *
	 * @return the users
	 */
	public Set<User> getUsers() {
		return this.users;
	}

	/**
	 * Sets the users.
	 *
	 * @param users the new users
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 1000000007;
		int result = super.hashCode();
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Project other = (Project) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
	
}
