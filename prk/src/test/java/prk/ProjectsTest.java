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
import task.timer.model.Project;

public class ProjectsTest {
	ManageEntity<Project> MM;
    private SessionFactory sessionFactory;

	@Before
	public void setUp() {
		sessionFactory=new FactoryCreator().getFactory();	
		MM = new ManageEntity<Project>(sessionFactory, EntityType.Project);		
	}
	
	@Test
	public void when_I_save_a_new_project_I_get_his_id_from_database(){
		  Integer projectID = MM.add(new Project("Test project"));
	      List<AbstractEntity> projects = MM.list();
	      int lastIndex = projects.size()-1;
	      int lastProjectID = ((Project) projects.get(lastIndex)).getId();
	      
		  assertEquals(Integer.valueOf(lastProjectID), projectID);	            
	}
	
	
	@Test
	public void when_adding_a_project_then_I_can_retrieve_his_data(){
	      Project compareProject = new Project("Test project 2");
	      Integer projectID = MM.add(compareProject);
		  List<AbstractEntity> projects = MM.list();
		  Project projectFromDb = (Project) projects.get(projects.size()-1);
		  
		  assertTrue(compareProject.equals(projectFromDb));	      
	}
	
	@Test
	public void when_adding_a_project_I_can_delete_him_from_the_database(){
		  Project testProject = new Project("Test project");
		  Integer projectID = MM.add(testProject);
		  try {
			MM.delete(projectID);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		  
		  List<AbstractEntity> projects = MM.list();
		  int lastIndex = projects.size()-1;
	      int lastProjectId = ((Project) projects.get(lastIndex)).getId();
	      
		  assertFalse(projects.get(lastIndex).equals(testProject) && lastProjectId == projectID);		  
	}
	
	@Test
	public void two_projects_with_same_field_values_are_equal(){
		Project project1 = new Project("Test project");
		Project project2 = new Project("Test project");
		
		project1.setId(1);
		project2.setId(1);

		assertEquals(project1,project2);
	}
	
	@Test
	public void two_projects_with_same_props_but_different_ids_are_different(){
		Project project1 = new Project("Test project");
		Project project2 = new Project("Test project");
		
		project1.setId(1);
		project2.setId(2);

		assertFalse(project1.equals(project2));
	}

}