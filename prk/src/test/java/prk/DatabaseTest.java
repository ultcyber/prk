package prk;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import task.timer.model.ManageUsers;
import task.timer.model.User;

public class DatabaseTest {
	ManageUsers MM;

	@Before
	public void setUp() {	
		MM = new ManageUsers();
	}
	
	@Test
	public void adding_a_user_then_retrieving_him_from_database(){
		  Integer userID1 = MM.addUser("zara89", "Zara", "Ali");
	      List<User> listUsers = MM.listUsers();
	      
	      assertEquals(listUsers.get(0), userID1);	      
	}
}
