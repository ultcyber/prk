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
import task.timer.model.User;

public class RecordsTest {
	ManageEntity MR;
	ManageEntity MU;
	ManageEntity MP;
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
		MU = new ManageEntity(sessionFactory, EntityType.User);
		MP = new ManageEntity(sessionFactory, EntityType.Project);
	
	}
	
	/*@Test
	public void when_I_save_a_new_record_of_certain_user_and_project_I_get_its_id_from_database(){
		  
		  List<AbstractEntity> users = MU.list();
		  User testUser = (User) users.get(users.size()-1);
		  
		  List<AbstractEntity> projects = MP.list();
		  Project testProject = (Project) projects.get(projects.size()-1);
		  
		  
		  Integer recordID = MR.add(new Record(testUser, testProject, new Date(23233323), new Date(23232323)));
	      List<AbstractEntity> records = MR.list();
	      int lastIndex = records.size()-1;
	      int lastRecordID = ((Record) records.get(lastIndex)).getId();
	      
		  assertEquals(Integer.valueOf(lastRecordID), recordID);            
	}
	*/

}
