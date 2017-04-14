package task.timer.model;
import java.sql.Date;
import java.util.List;
 
import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import org.hibernate.SessionFactory;

public class ManageProjects {
   private SessionFactory factory;
   
   public ManageProjects(){
	   try {
			factory=new Configuration().configure().buildSessionFactory();}
		catch(Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
   }
   
   public ManageProjects(SessionFactory factory){
	   this.factory = factory;
   }
   
   /* Method to CREATE a project in the database */
   public Integer addProject(String name){
      Session session = factory.openSession();
      Transaction tx = null;
      Integer projectID = null;
      try{
         tx = session.beginTransaction();
         Project project = new Project(name);
         projectID = (Integer) session.save(project); 
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
    	 session.close();
      }
      return projectID;
   }
   /* Method to  READ all the records */
   public List<Project> listProjects( ){
      Session session = factory.openSession();
      Transaction tx = null;
      List<Project> projects = null;
      try{
         tx = session.beginTransaction();
         projects = session.createQuery("from Project").list(); 
         tx.commit();
         return projects;
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
    	  session.close(); 
      }
      return projects;
   }
   /* Method to UPDATE timeStart and timeStop for a record */
   public void updateProject(Integer ProjectID, String name ){
      Session session = factory.openSession();
      Transaction tx = null;
      try{
         tx = session.beginTransaction();
         Project project = 
                    (Project)session.get(Project.class, ProjectID); 
         project.setName(name);

		 session.update(project); 
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
     	 session.close(); 
      }
   }
   /* Method to DELETE a record from the database */
   public void deleteUser(Integer ProjectID){
      Session session = factory.openSession();
      Transaction tx = null;
      try{
         tx = session.beginTransaction();
         Project project = 
                   (Project)session.get(Project.class, ProjectID); 
         session.delete(project); 
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
     	session.close(); 
      }
   }
   
}