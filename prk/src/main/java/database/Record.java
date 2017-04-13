package database;
import java.sql.Date;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="RECORDS")

public class Record {
	 @Id @GeneratedValue
	 @Column(name = "id")
	 private int id;
	 
	 @ManyToOne
	 @JoinColumn(name="user_id")
	 private User user;
	 
	 @ManyToOne
	 @JoinColumn(name="project_id")
	 private Project project;
	 
	 @Column(name="timeStart")
	 private Date timeStart;
	 
	 @Column(name="timeStop")
	 private Date timeStop;
	 
	 @OneToMany(mappedBy="record")
	 private Set<Record> records;
	 
}
