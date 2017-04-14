package task.timer.model;
import java.util.List;
 
import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;

public class ManageUsers {
   private SessionFactory factory;
   
   public ManageUsers(){
	   try {
			factory=new Configuration().configure().buildSessionFactory();}
		catch(Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
   }
   
   /* Method to CREATE an employee in the database */
   public Integer addUser(String login, String fname, String lname){
      Session session = factory.openSession();
      Transaction tx = null;
      Integer userID = null;
      try{
         tx = session.beginTransaction();
         User user = new User(login, fname, lname);
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
   /* Method to  READ all the employees */
   public List<User> listUsers( ){
      Session session = factory.openSession();
      Transaction tx = null;
      List<User> users = null;
      try{
         tx = session.beginTransaction();
         users = session.createQuery("FROM USERS").list(); 
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
   /* Method to DELETE an employee from the records */
   public void deleteUser(Integer UserID){
      Session session = factory.openSession();
      Transaction tx = null;
      try{
         tx = session.beginTransaction();
         User employee = 
                   (User)session.get(User.class, UserID); 
         session.delete(employee); 
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
         session.close(); 
      }
   }
}