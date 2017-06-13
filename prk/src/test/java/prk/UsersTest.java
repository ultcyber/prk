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
import task.timer.model.Record;
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
	public void when_adding_a_user_then_I_can_retrieve_his_data(){
	      User compareUser = new User("zara89", "testpass", "Zara", "Ali", "rw", true, true);
		  Integer userID = MM.add(compareUser);
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
	
	@Test
	public void two_users_with_same_field_values_are_equal(){
		User user1 = new User("tommy", "testpass", "Thomas", "Shelby", "r", true, true);
		User user2 = new User("tommy", "testpass", "Thomas", "Shelby", "r", true, true);
		
		user1.setId(1);
		user2.setId(1);

		assertEquals(user1,user2);
	}
	
	@Test
	public void two_users_with_same_props_but_different_ids_are_different(){
		User user1 = new User("tommy", "testpass", "Thomas", "Shelby", "r", true, true);
		User user2 = new User("tommy", "testpass", "Thomas", "Shelby", "r", true, true);
		
		user1.setId(1);
		user2.setId(2);

		assertFalse(user1.equals(user2));
	}
}

