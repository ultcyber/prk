package task.timer.model;
import java.sql.Date;
import java.util.Set;

import javax.persistence.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name="RECORDS")

public class Record {
	 @Id @GeneratedValue
	 @Column(name = "recordId")
	 private IntegerProperty recordId;
	 
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

	
	 public Record(){
		 recordId.set(1);
	 }
	 
	 public IntegerProperty recordIdProperty(){ 
		 return recordId;
	 }
	 
	 public StringProperty projectNameProperty(){
		 return project.getName();
	 }

	 
}
