package arch.task.timer.model;
import java.sql.Date;
import java.util.List;
 
import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import task.timer.model.Project;
import task.timer.model.Record;
import task.timer.model.User;

import org.hibernate.SessionFactory;

public class ManageRecords {
   private SessionFactory factory;
   
   public ManageRecords(){
	   try {
			factory=new Configuration().configure().buildSessionFactory();}
		catch(Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
   }
   
   public ManageRecords(SessionFactory factory){
	   this.factory = factory;
   }
   
   /* Method to CREATE a record in the database */
   /*public Integer addRecord(User user, Project project, Date timeStart, Date timeStop){
      Session session = factory.openSession();
      Transaction tx = null;
      Integer recordID = null;
      try{
         tx = session.beginTransaction();
         Record record = new Record(user, project, timeStart, timeStop);
         recordID = (Integer) session.save(user); 
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
    	 session.close();
      }
      return recordID;
   }*/
   /* Method to  READ all the records */
   public List<Record> listRecords( ){
      Session session = factory.openSession();
      Transaction tx = null;
      List<Record> records = null;
      try{
         tx = session.beginTransaction();
         records = session.createQuery("from Record").list(); 
         tx.commit();
         return records;
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
    	  session.close(); 
      }
      return records;
   }
   /* Method to UPDATE timeStart and timeStop for a record */
   /*public void updateRecord(Integer RecordID, Date timeStart, Date timeStop ){
      Session session = factory.openSession();
      Transaction tx = null;
      try{
         tx = session.beginTransaction();
         Record record = 
                    (Record)session.get(Record.class, RecordID); 
         record.setTimeStart(timeStart);
         record.setTimeStop(timeStop);

		 session.update(record); 
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
     	 session.close(); 
      }
   }*/
   /* Method to DELETE a record from the database */
   public void deleteRecord(Integer RecordID){
      Session session = factory.openSession();
      Transaction tx = null;
      try{
         tx = session.beginTransaction();
         Record record = 
                   (Record)session.get(Record.class, RecordID); 
         session.delete(record); 
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
     	session.close(); 
      }
   }
   
}