package prk;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import task.timer.model.ManageUsers;
import task.timer.model.User;

public class USERSTableTest {
	ManageUsers MM;
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
		MM = new ManageUsers(sessionFactory);
		
	}
	
	@Test
	public void when_I_save_a_new_user_I_get_his_id_from_database(){
		  Integer user = MM.addUser("zara89", "testpass", "Zara", "Ali", "rw");
	      List<User> users = MM.listUsers();
		  assertEquals(Integer.valueOf(users.size()), user);	            
	}
	
	@Test
	public void when_adding_a_user_then_I_can_retrieve_his_data(){
	      User compareUser = new User("zara89", "testpass", "Zara", "Ali", "rw");
		  List<User> users = MM.listUsers();
		  User userFromDb = users.get(users.size()-1);
		  
		  assertTrue(compareUser.equals(userFromDb));	      
	}

}
