package prk;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

import task.timer.model.AbstractEntity;
import task.timer.model.EntityType;
import task.timer.model.FactoryCreator;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.Project;
import task.timer.model.User;

public class UsersTest {
	ManageEntity<User> MM;
    private SessionFactory sessionFactory;

	@Before
	public void setUp() {
		sessionFactory=new FactoryCreator().getFactory();	
		MM = new ManageEntity<User>(sessionFactory, EntityType.User);		
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
		  Integer userID = MM.add(new User("zara89", "testpass", "Zara", "Ali", "rw", true, true));
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
		  User testUser = new User("tommy", "testpass", "Thomas", "Shelby", "r", true, true);
		  Integer userID = MM.add(testUser);
		  try {
			MM.delete(userID);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		  
		  List<AbstractEntity> users = MM.list();
		  int lastIndex = users.size()-1;
	      int lastUserId = ((User) users.get(lastIndex)).getId();
	      
		  assertFalse(users.get(lastIndex).equals(testUser) && lastUserId == userID);
		  
		  

	}

}
