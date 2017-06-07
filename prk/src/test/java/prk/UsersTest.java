package prk;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import arch.task.timer.model.ManageUsers;

import static org.junit.Assert.*;

import java.util.List;

import task.timer.model.AbstractEntity;
import task.timer.model.EntityType;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.User;

public class UsersTest {
	ManageEntity MM;
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
		MM = new ManageEntity(sessionFactory, EntityType.User);
		
	}
	
	@Test
	public void when_I_save_a_new_user_I_get_his_id_from_database(){
		  Integer userID = MM.add(new User("zara89", "testpass", "Zara", "Ali", "rw", true, true));
	      List<AbstractEntity> users = MM.list();
	      int lastIndex = users.size()-1;
	      int lastUserID = ((User) users.get(lastIndex)).getId();
	      
		  assertEquals(Integer.valueOf(lastUserID), userID);	            
	}
	
	@Test
	public void MEFactory_save_a_user_to_a_database(){
		  ManageEntity UEM = new MEFactory(sessionFactory).getUserEntityManager();
		  Integer userID = UEM.add(new User("zara89", "testpass", "Zara", "Ali", "rw", true, true));
		  List<AbstractEntity> users = MM.list();
	      int lastIndex = users.size()-1;
	      int lastUserID = ((User) users.get(lastIndex)).getId();
	      
		  assertEquals(Integer.valueOf(lastUserID), userID);	           
	}
	
	@Test
	public void when_adding_a_user_then_I_can_retrieve_his_data(){
	      User compareUser = new User("zara89", "testpass", "Zara", "Ali", "rw", true, true);
		  List<AbstractEntity> users = MM.list();
		  User userFromDb = (User) users.get(users.size()-1);
		  
		  assertTrue(compareUser.equals(userFromDb));	      
	}
	
	@Test
	public void when_adding_a_user_I_can_delete_him_from_the_database(){
		  ManageEntity UEM = new MEFactory(sessionFactory).getUserEntityManager();
		  User testUser = new User("tommy", "testpass", "Thomas", "Shelby", "r", true, true);
		  Integer userID = UEM.add(testUser);
		  try {
			UEM.delete(userID);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		  
		  List<AbstractEntity> users = UEM.list();
		  int lastIndex = users.size()-1;
	      User lastUser = (User) users.get(lastIndex);
	      int lastUserId = ((User) users.get(lastIndex)).getId();
	      
		  assertFalse(users.get(lastIndex).equals(testUser) && lastUserId == userID);
		  
		  

	}

}
