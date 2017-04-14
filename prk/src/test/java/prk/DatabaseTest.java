package prk;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import task.timer.model.ManageUsers;
import task.timer.model.User;

public class DatabaseTest {
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
		  Integer userID1 = MM.addUser("zara89", "testpass", "Zara", "Ali", "rw");
	      List<User> users = MM.listUsers();
		  assertEquals(Integer.valueOf(users.size()), userID1);	            
	}
	
	
	
}
