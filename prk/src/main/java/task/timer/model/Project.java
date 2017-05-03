package task.timer.model;

import javax.persistence.*;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;


@Entity
@Table(name = "PROJECTS")
public class Project extends AbstractEntity {
	
	private int id;
	private String name;
	
	public Project(String name){
		this.name = name;
	}
	
	public Project(){
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public int getId() {
		return id;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
