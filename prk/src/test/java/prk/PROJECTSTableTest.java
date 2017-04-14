package prk;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import task.timer.model.ManageProjects;
import task.timer.model.ManageUsers;
import task.timer.model.Project;
import task.timer.model.User;

public class PROJECTSTableTest {
	ManageProjects MP;
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
		MP = new ManageProjects(sessionFactory);
		
	}
	
	@Test
	public void when_I_save_a_new_user_I_get_his_id_from_database(){
		  Integer project = MP.addProject("FirstProject");
	      List<Project> projects = MP.listProjects();
		  assertEquals(Integer.valueOf(projects.size()), project);	            
	}
	
	

}
