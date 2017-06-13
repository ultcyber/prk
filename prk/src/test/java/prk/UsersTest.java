package prk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;

import task.timer.model.AbstractEntity;
import task.timer.model.EntityType;
import task.timer.model.FactoryCreator;
import task.timer.model.ManageEntity;
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
		  Integer userID = MM.add(new User("zara89", "testpass", "Zara", "Ali", "pracownik", true, true));
	      List<AbstractEntity> users = MM.list();
	      int lastIndex = users.size()-1;
	      int lastUserID = ((User) users.get(lastIndex)).getId();
	      
		  assertEquals(Integer.valueOf(lastUserID), userID);	            
	}
	
	
	@Test
	public void when_adding_a_user_then_I_can_retrieve_his_data(){
	      User compareUser = new User("zara89", "testpass", "Zara", "Ali", "pracownik", true, true);
		  Integer userID = MM.add(compareUser);
		  List<AbstractEntity> users = MM.list();
		  User userFromDb = (User) users.get(users.size()-1);
		  
		  assertTrue(compareUser.equals(userFromDb));	      
	}
	
	@Test
	public void when_adding_a_user_I_can_delete_him_from_the_database(){
		  User testUser = new User("tommy", "testpass", "Thomas", "Shelby", "pracownik", true, true);
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
		User user1 = new User("tommy", "testpass", "Thomas", "Shelby", "pracownik", true, true);
		User user2 = new User("tommy", "testpass", "Thomas", "Shelby", "pracownik", true, true);
		
		user1.setId(1);
		user2.setId(1);

		assertEquals(user1,user2);
	}
	
	@Test
	public void two_users_with_same_props_but_different_ids_are_different(){
		User user1 = new User("tommy", "testpass", "Thomas", "Shelby", "pracownik", true, true);
		User user2 = new User("tommy", "testpass", "Thomas", "Shelby", "pracownik", true, true);
		
		user1.setId(1);
		user2.setId(2);

		assertFalse(user1.equals(user2));
	}
	
	
	// Database table integrity tests. As dialog window cannot show and standard exceptions are suppressed, expects Throwable.
	
	@Test(expected=Throwable.class)
	public void cannot_add_user_with_a_null_first_name(){
		User user1 = new User();
		user1.setFirstName(null);
		user1.setLastName("tester");
		user1.setLogin("mean_tester");
		user1.setPassword("testpass");
		user1.setPermissions("pracownik");
		user1.setReminder(true);
		user1.setEditing(true);
		
		Integer id = MM.add(user1);
		assertTrue(id == null);
	}
	
	@Test(expected=Throwable.class)
	public void cannot_add_user_with_a_null_last_name(){
		User user1 = new User();
		user1.setFirstName("John");
		user1.setLastName(null);
		user1.setLogin("mean_tester");
		user1.setPassword("testpass");
		user1.setPermissions("pracownik");
		user1.setReminder(true);
		user1.setEditing(true);
		
		Integer id = MM.add(user1);
		assertTrue(id == null);
	}
	
	@Test(expected=Throwable.class)
	public void cannot_add_user_with_a_null_login(){
		User user1 = new User();
		user1.setFirstName("John");
		user1.setLastName("tester");
		user1.setLogin(null);
		user1.setPassword("testpass");
		user1.setPermissions("pracownik");
		user1.setReminder(true);
		user1.setEditing(true);
		
		Integer id = MM.add(user1);
		assertTrue(id == null);
	}
	
	@Test(expected=Throwable.class)
	public void cannot_add_user_with_a_null_password(){
		User user1 = new User();
		user1.setFirstName("John");
		user1.setLastName("tester");
		user1.setLogin("mean_tester");
		user1.setPassword(null);
		user1.setPermissions("pracownik");
		user1.setReminder(true);
		user1.setEditing(true);
		
		Integer id = MM.add(user1);
		assertTrue(id == null);
	}
	
	@Test(expected=Throwable.class)
	public void cannot_add_user_with_a_null_permissions(){
		User user1 = new User();
		user1.setFirstName("John");
		user1.setLastName("tester");
		user1.setLogin("mean_tester");
		user1.setPassword("testpass");
		user1.setPermissions(null);
		user1.setReminder(true);
		user1.setEditing(true);
		
		Integer id = MM.add(user1);
		assertTrue(id == null);
	}
	
	@Test(expected=Throwable.class)
	public void cannot_add_user_with_a_null_reminder(){
		User user1 = new User();
		user1.setFirstName("John");
		user1.setLastName("tester");
		user1.setLogin("mean_tester");
		user1.setPassword("testpass");
		user1.setPermissions("pracownik");
		user1.setEditing(true);
		
		Integer id = MM.add(user1);
		assertTrue(id == null);
	}
	
	@Test(expected=Throwable.class)
	public void cannot_add_user_with_a_null_editing(){
		User user1 = new User();
		user1.setFirstName("John");
		user1.setLastName("tester");
		user1.setLogin("mean_tester");
		user1.setPassword("testpass");
		user1.setPermissions("pracownik");
		user1.setReminder(true);
		
		Integer id = MM.add(user1);
		assertTrue(id == null);
	}
	
}

