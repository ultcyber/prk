package task.timer.model;
import java.sql.Date;

import javax.persistence.*;

@Entity
@Table(name="TASKS")

public class Task extends AbstractEntity {
	
	private int id;	
	private String description;	
	private Record record;
	
	public Task(Record record, String description) {
		this.record = record;
		this.description = description;
	}
	
	public Task(){
	
	}

	@Id 	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
		
	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

	@ManyToOne
	@JoinColumn(name="record")
	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}
	
	
}
