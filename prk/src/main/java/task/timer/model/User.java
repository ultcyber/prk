package task.timer.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name="USERS")

public class User extends AbstractEntity {
	 @Id @GeneratedValue(strategy = GenerationType.AUTO)
	 @Column(name = "id")
	 private int id;
	 
	 @Column (name = "login")
	 private String login;
	 
	 @Column (name="firstName")
	 private String firstName;
	 
	 @Column (name="lastName")
	 private String lastName;
	 
	 @Column (name="password")
	 private String password;
	 
	 @Column (name="permissions")
	 private String permissions;
	 
	 @ManyToMany(fetch = FetchType.EAGER, mappedBy="users", cascade = CascadeType.ALL)  
	 private Set<Project> projects = new HashSet<Project>();
	 
	 	
	public User(String login, String password, String firstName, String lastName, String permissions) {
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.permissions = permissions;
	}
	
	public User(int id, String login, String password, String firstName, String lastName, String permissions) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.permissions = permissions;
	}
	
	public User(){
	
	}
	

	public int getId() {
		return id;
	}
	
	public IntegerProperty getIdProperty(){
		SimpleIntegerProperty idProperty = new SimpleIntegerProperty(id);
		return idProperty;
	}

	public String getLogin() {
		return login;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public StringProperty getFirstNameProperty(){
		SimpleStringProperty firstNameProperty = new SimpleStringProperty(firstName);
		return firstNameProperty;
	}

	public String getLastName() {
		return lastName;
	}
	
	public StringProperty getLastNameProperty(){
		SimpleStringProperty lastNameProperty = new SimpleStringProperty(lastName);
		return lastNameProperty;
	}

	public String getPassword() {
		return password;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	 
	public Set<Project> getProjects() {
	    return projects;
	}
	
	
	
@Override
	public boolean equals(Object other){
		if (other == this) return true;
		if (other == null) return false;
		if (getClass() != other.getClass()) return false;
		User user = (User) other;
		
		return 
				getLogin().equals(user.getLogin()) &&
				getPassword().equals(user.getPassword()) &&
				getFirstName().equals(user.getFirstName()) &&
				getLastName().equals(user.getLastName()) &&
				getPermissions().equals(user.getPermissions());

				
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((permissions == null) ? 0 : permissions.hashCode());
		return result;
	}


	@Override
	public String toString(){
		return firstName + " " + lastName;
	}
	
}