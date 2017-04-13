package task.timer.model;
import java.util.List; 
import java.util.Iterator; 
 
import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ManageUsers {
   private static SessionFactory factory; 
   public static void main(String[] args) {
      try{
         factory = new Configuration().configure().buildSessionFactory();
      }catch (Throwable ex) { 
         System.err.println("Failed to create sessionFactory object." + ex);
         throw new ExceptionInInitializerError(ex); 
      }
      ManageUsers MM = new ManageUsers();

      /* Add few user records in database */
      Integer empID1 = MM.addUser("zara89", "Zara", "Ali");
      Integer empID2 = MM.addUser("ddTest", "Daisy", "Das" );
      Integer empID3 = MM.addUser("jp", "John", "Paul");

      /* List down all the users */
      MM.listUsers();

      /* Update user's records */
      MM.updateUser(empID1, "TestFistName", "TestLastName");

      /* Delete a user from the database */
      MM.deleteUser(empID2);

      /* List down new list of the users */
      MM.listUsers();
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
   public void listUsers( ){
      Session session = factory.openSession();
      Transaction tx = null;
      try{
         tx = session.beginTransaction();
         List users = session.createQuery("FROM USERS").list(); 
         for (Iterator iterator = 
                           users.iterator(); iterator.hasNext();){
            User user = (User) iterator.next(); 
            System.out.print("First Name: " + user.getFirstName()); 
            System.out.print("  Last Name: " + user.getLastName()); 
            System.out.println("  Login: " + user.getLogin()); 
         }
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
         session.close(); 
      }
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