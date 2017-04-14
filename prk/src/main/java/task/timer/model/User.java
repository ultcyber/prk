package task.timer.model;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="USERS")

public class User {
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
	 
	 //@OneToMany(mappedBy="user")
	 //private Set<User> users;
	 
	 
	public User(String login, String password, String firstName, String lastName, String permissions) {
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

	public String getLogin() {
		return login;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
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
	public int hashCode(){
		return (int) (id*Math.random()*13*lastName.hashCode());
		
	}
	
}
