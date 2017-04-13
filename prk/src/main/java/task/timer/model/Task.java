package task.timer.model;
import java.sql.Date;

import javax.persistence.*;

@Entity
@Table(name="TASKS")

public class Task {
	@Id @GeneratedValue
	@Column(name = "id")
	 private int id;
	
	@Column(name="description")
	private String description;
	
	@ManyToOne
	@JoinColumn(name="record_id")
	private Record record;
}
