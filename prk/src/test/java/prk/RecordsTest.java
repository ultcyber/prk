package prk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;

import task.timer.model.AbstractEntity;
import task.timer.model.EntityType;
import task.timer.model.FactoryCreator;
import task.timer.model.ManageEntity;
import task.timer.model.Project;
import task.timer.model.Record;
import task.timer.model.User;

public class RecordsTest {
	ManageEntity<Record> MM;
	ManageEntity<User> MU;
	ManageEntity<Project> MP;
    private SessionFactory sessionFactory;
    
    // Sample data
    private User user;
	private Project project;
	private LocalDate testDate;
	private LocalTime startTime;
	private LocalTime endTime;


	@Before
	public void setUp() {
		
		sessionFactory=new FactoryCreator().getFactory();	
		MM = new ManageEntity<Record>(sessionFactory, EntityType.Record);
		MU = new ManageEntity<User>(sessionFactory, EntityType.User);
		MP = new ManageEntity<Project>(sessionFactory, EntityType.Project);
				
		// Not completely stand-alone - needs some real projects and users to be in the db
		user = (User) MU.list().get(0);
		project = (Project) MP.list().get(0);
		
		testDate = LocalDate.of(1, 1, 1);
		startTime = LocalTime.of(1, 1);
		endTime = LocalTime.of(1, 2);
	}
	
	@Test
	public void when_I_save_a_new_record_I_get_his_id_from_database(){
		  Integer recordID = MM.add(new Record(user, project, "test record", testDate, startTime, endTime));
	      List<AbstractEntity> records = MM.list();
	      int lastIndex = records.size()-1;
	      int lastRecordID = ((Record) records.get(lastIndex)).getId();
	      
		  assertEquals(Integer.valueOf(lastRecordID), recordID);	            
	}
	
	
	@Test
	public void when_adding_a_record_then_I_can_retrieve_his_data(){
	      Record compareRecord = new Record(user, project, "test record", testDate, startTime, endTime);
		  Integer recordID = MM.add(compareRecord);
		  List<AbstractEntity> records = MM.list();
		  Record recordFromDb = (Record) records.get(records.size()-1);
		  
		  assertTrue(compareRecord.equals(recordFromDb));	      
	}
	
	@Test
	public void when_adding_a_record_I_can_delete_him_from_the_database(){
		  Record testRecord = new Record(user, project, "test record", testDate, startTime, endTime);
		  Integer recordID = MM.add(testRecord);
		  try {
			MM.delete(recordID);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		  
		  List<AbstractEntity> records = MM.list();
		  int lastIndex = records.size()-1;
	      int lastRecordId = ((Record) records.get(lastIndex)).getId();
	      
		  assertFalse(records.get(lastIndex).equals(testRecord) && lastRecordId == recordID);
		  
	}
	
	@Test
	public void two_records_with_same_field_values_are_equal(){
		Record record1 = new Record(user, project, "test record", testDate, startTime, endTime);
		Record record2 = new Record(user, project, "test record", testDate, startTime, endTime);
		
		record1.setId(1);
		record2.setId(1);

		assertEquals(record1,record2);
	}
	
	@Test
	public void two_records_with_same_props_but_different_ids_are_different(){
		Record record1 = new Record(user, project, "test record", testDate, startTime, endTime);
		Record record2 = new Record(user, project, "test record", testDate, startTime, endTime);
		
		record1.setId(1);
		record2.setId(2);

		assertFalse(record1.equals(record2));
	}
	
	// Database table integrity tests. As dialog window cannot show and standard exceptions are suppressed, expects Throwable.
	
	@Test(expected=Throwable.class)
	public void cannot_add_record_with_null_user(){
		Record record = new Record();
		record.setProject(project);
		record.setDescription("test description");
		record.setDate(testDate);
		record.setTimeStart(startTime);
		record.setTimeStop(endTime);
		
		Integer id = MM.add(record);
		assertTrue(id == null);
	}
	
	@Test(expected=Throwable.class)
	public void cannot_add_record_with_null_project(){
		Record record = new Record();
		record.setUser(user);
		record.setDescription("test description");
		record.setDate(testDate);
		record.setTimeStart(startTime);
		record.setTimeStop(endTime);
		
		Integer id = MM.add(record);
		assertTrue(id == null);
	}
	
	@Test(expected=Throwable.class)
	public void cannot_add_record_with_null_description(){
		Record record = new Record();
		record.setUser(user);
		record.setProject(project);
		record.setDate(testDate);
		record.setTimeStart(startTime);
		record.setTimeStop(endTime);
		
		Integer id = MM.add(record);
		assertTrue(id == null);
	}
	
	@Test(expected=Throwable.class)
	public void cannot_add_record_with_null_date(){
		Record record = new Record();
		record.setUser(user);
		record.setProject(project);
		record.setDescription("test description");
		record.setTimeStart(startTime);
		record.setTimeStop(endTime);
		
		Integer id = MM.add(record);
		assertTrue(id == null);
	}
	
	@Test(expected=Throwable.class)
	public void cannot_add_record_with_null_timeStart(){
		Record record = new Record();
		record.setUser(user);
		record.setProject(project);
		record.setDescription("test");
		record.setDate(testDate);
		record.setTimeStop(endTime);
		
		Integer id = MM.add(record);
		assertTrue(id == null);
	}
	
	// Case when a task has started but not finished
	@Test
	public void can_set_record_with_null_endTime(){
		Record record = new Record();
		record.setUser(user);
		record.setProject(project);
		record.setDescription("test description");
		record.setDate(testDate);
		record.setTimeStart(startTime);
		
		Integer id = MM.add(record);
		assertTrue(id != null);
	}
	
}
