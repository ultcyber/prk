package task.timer.model;

import java.util.Set;

import javax.persistence.*;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;


@Entity
@Table(name = "PROJECTS")
public class Project {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private String name;
	
	@OneToMany(mappedBy="project")
	private Set<Project> projects;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public StringProperty getStringPropertyName(){
		StringProperty prop = new SimpleStringProperty();
		prop.setValue(name);
		return prop;
	}

}
