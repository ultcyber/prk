package prk;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.List;

import task.timer.model.AbstractEntity;
import task.timer.model.EntityType;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.Project;
import task.timer.model.Record;
import task.timer.model.Task;
import task.timer.model.User;

public class TasksTest {
	ManageEntity MR;
	ManageEntity MT;
    private SessionFactory sessionFactory;

	@Before
	public void setUp() {
		try {
			sessionFactory=new Configuration().configure().buildSessionFactory();}
		catch(Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
		MR = new ManageEntity(sessionFactory, EntityType.Record);
		MT = new ManageEntity(sessionFactory, EntityType.Task);
	
	}
	
	@Test
	public void when_I_save_a_new_task_for_certain_record_I_get_its_id_from_database(){
		  
		  List<AbstractEntity> records = MR.list();
		  Record testRecord = (Record) records.get(records.size()-1);
		  		  
		  Integer taskID = MT.add(new Task(testRecord, "some description"));
	      List<AbstractEntity> tasks = MT.list();
	      int lastIndex = tasks.size()-1;
	      int lastTaskID = ((Task) tasks.get(lastIndex)).getId();
	      
		  assertEquals(Integer.valueOf(lastTaskID), taskID);            
	}
	

}
