package task.timer.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


@Entity
@Table(name = "PROJECTS")
public class Project extends AbstractEntity {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToMany()
	@JoinTable(name = "PROJECT_USER", 
			joinColumns = { @JoinColumn(name = "PROJECT_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "USER_ID") })	
	private Set<User> users = new HashSet<User>();

	
	public Project(String name){
		this.name = name;
	}
	
	public Project(){
		
	}
	

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public IntegerProperty getIdProjectProperty(){
		SimpleIntegerProperty idProperty = new SimpleIntegerProperty(id);
		return idProperty;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public StringProperty getNameProjectProperty(){
		SimpleStringProperty nameProjectProperty = new SimpleStringProperty(name);
		return nameProjectProperty;
	}
		
	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

}
