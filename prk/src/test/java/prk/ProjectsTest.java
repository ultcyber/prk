package prk;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

import task.timer.model.AbstractEntity;
import task.timer.model.EntityType;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.Project;
import task.timer.model.User;

public class ProjectsTest {
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
		MM = new ManageEntity(sessionFactory, EntityType.Project);
		
	}
	
	@Test
	public void when_I_save_a_new_project_I_get_its_id_from_database(){
		  Integer projectID = MM.add(new Project("TestProject"));
	      List<AbstractEntity> projects = MM.list();
	      int lastIndex = projects.size()-1;
	      int lastProjectID = ((Project) projects.get(lastIndex)).getId();
	      
		  assertEquals(Integer.valueOf(lastProjectID), projectID);            
	}
	

}
