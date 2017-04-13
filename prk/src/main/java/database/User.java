package database;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="USERS")

public class User {
	 @Id @GeneratedValue
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
	 
	 @Column (name="privileges")
	 private String privileges;
	 
	 @OneToMany(mappedBy="user")
	 private Set<User> users;

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

	public String getPrivileges() {
		return privileges;
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

	public void setPrivileges(String privileges) {
		this.privileges = privileges;
	}
	
}
