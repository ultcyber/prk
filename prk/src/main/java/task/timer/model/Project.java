package task.timer.model;

import java.util.Set;

import javax.persistence.*;

import javafx.beans.property.StringProperty;

@Entity
@Table(name = "PROJECTS")
public class Project {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private StringProperty name;
	
	@OneToMany(mappedBy="project")
	private Set<Project> projects;

	public Project(){
		name.set("Pierwsza próbna nazwa projektu");
	}
	
	
	public int getId() {
		return id;
	}

	public StringProperty getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(StringProperty name) {
		this.name = name;
	}

}
