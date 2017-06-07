package arch.task.timer.model;
import java.util.List;
 
import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import task.timer.model.User;

import org.hibernate.SessionFactory;

public class ManageUsers {
   private SessionFactory factory;
   
   public ManageUsers(){
	   try {
			factory=new Configuration().configure().buildSessionFactory();}
		catch(Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
   }
   
   public ManageUsers(SessionFactory factory){
	   this.factory = factory;
   }
   
   /* Method to CREATE an employee in the database */
   public Integer addUser(String login, String password, String fname, String lname, String permissions, boolean editing, boolean reminder){
      Session session = factory.openSession();
      Transaction tx = null;
      Integer userID = null;
      try{
         tx = session.beginTransaction();
         User user = new User(login, password, fname, lname, permissions, editing, reminder);
         userID = (Integer) session.save(user); 
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
    	 session.close();
      }
      return userID;
   }
   /* Method to  READ all the users */
   public List<User> listUsers(){
      Session session = factory.openSession();
      Transaction tx = null;
      List<User> users = null;
      try{
         tx = session.beginTransaction();
         users = session.createQuery("from User").list(); 
         tx.commit();
         return users;
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
    	  session.close(); 
      }
      return users;
   }
   /* Method to UPDATE first and last name for a user */
   public void updateUser(Integer UserID, String firstName, String lastName ){
      Session session = factory.openSession();
      Transaction tx = null;
      try{
         tx = session.beginTransaction();
         User user = 
                    (User)session.get(User.class, UserID); 
         user.setFirstName( firstName );
         user.setLastName( lastName );

		 session.update(user); 
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
     	 session.close(); 
      }
   }
   /* Method to DELETE a user from the records */
   public void deleteUser(Integer UserID){
      Session session = factory.openSession();
      Transaction tx = null;
      try{
         tx = session.beginTransaction();
         User user = 
                   (User)session.get(User.class, UserID); 
         session.delete(user); 
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
     	session.close(); 
      }
   }
   
}