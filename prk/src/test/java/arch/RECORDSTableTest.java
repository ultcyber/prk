package arch;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import arch.task.timer.model.ManageRecords;
import arch.task.timer.model.ManageUsers;

import static org.junit.Assert.*;

import java.util.List;

import task.timer.model.User;

public class RECORDSTableTest {
	ManageUsers MM;
	ManageRecords MR;
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
		MR = new ManageRecords(sessionFactory);
		
	}
	
	@Test
	public void when_creating_a_new_project_for_existing_user_and_project_I_get_id(){
	      List<User> users = MM.listUsers();
	      // TODO: finish the test
	}
}
