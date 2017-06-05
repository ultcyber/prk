package task.timer.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javafx.scene.control.Alert.AlertType;
import task.timer.helper.AlertDialog;

import org.hibernate.SessionFactory;

public class ManageEntity implements IEntityManager {
   private SessionFactory factory;
   private EntityType entityType;
   
   public ManageEntity(EntityType entityType){
	   this.entityType = entityType;
	   
	   factory = new FactoryCreator().getFactory();
   }
   
   public ManageEntity(SessionFactory factory){
	   this.factory = factory;
   }
   
   public ManageEntity(SessionFactory factory, EntityType entityType){
	   this.factory = factory;
	   this.entityType = entityType;
   }
   

@Override
public Integer add(AbstractEntity entity) {
	Session session = factory.openSession();
    Transaction tx = null;
    Integer entityID = null;
    try{
       tx = session.beginTransaction();      
       entityID = (Integer) session.save(entity); 
       tx.commit();
    }catch (HibernateException e) {
       if (tx!=null) tx.rollback();
       e.printStackTrace(); 
    }finally {
  	 session.close();
    }
    return entityID;
}

@Override
public void update(AbstractEntity newEntity) throws ClassNotFoundException {
	Session session = factory.openSession();
    Transaction tx = null;
    try{
       tx = session.beginTransaction();          
       session.update(newEntity); 
       tx.commit();
    }catch (HibernateException e) {
       if (tx!=null) tx.rollback();
       e.printStackTrace(); 
    }finally {
   	 session.close(); 
    }	
}

@Override
public void delete(int id) throws ClassNotFoundException {
	Session session = factory.openSession();
    Transaction tx = null;
    try{
       tx = session.beginTransaction();
       AbstractEntity existingEntity = 
               (AbstractEntity)session.get(Class.forName(entityType.getClassType()), id); 
       session.delete(existingEntity); 
       tx.commit();
    }catch (HibernateException e) {
       if (tx!=null) tx.rollback();
       e.printStackTrace(); 
    }finally {
   	session.close(); 
    }	
}

@Override
public List<AbstractEntity> list(){
    Session session = factory.openSession();
    Transaction tx = null;
    List<AbstractEntity> entities = null;
    try{
       tx = session.beginTransaction();
       entities = session.createQuery("from " + entityType).list(); 
      
       tx.commit();
       return entities;
    }catch (HibernateException e) {
       if (tx!=null) tx.rollback();
       e.printStackTrace(); 
    }finally {
  	  session.close(); 
    }
    return entities;
 }

public User getUserForLogin(String login){
	
    Session session = factory.openSession();
    
    try {
    	String hql = "from User U where U.login = :user_login";
    	Query<User> query = session.createQuery(hql);
    	query.setParameter("user_login", login);
    	List<User> results = query.list();
    	
    	if (results.size() == 1){
    		return (User) results.get(0);
    	}
    }
    finally{
    	session.close();
    }
	
	return null;
	
}

public List<Record> listRecordsForDateRange(String dateStart, String dateEnd){
	Session session = factory.openSession();
	
	try {
    	String hql = "from Records R where R.date between :date_start and :date_end";
    	Query<Record> query = session.createQuery(hql);
    	query.setParameter("date_start", dateStart);
    	query.setParameter("date_end", dateEnd);
    	List<Record> results = query.list();
    	
    	return results;
    }
    finally{
    	session.close();
    }
		
}

public List<Record> listRecords(User user, Project project, LocalDate date){
    Session session = factory.openSession();
    List<Record> entities = null;
    
    // Creating a string and removing all null values
    String[] data = new String[3];
    
    data[0] = user != null ? "R.user = " + String.valueOf(user.getId()) : null;
    data[1] = project != null ? "R.project = " + String.valueOf(project.getId()) : null;
    data[2] = date != null ? "R.date = " + String.format("'%s'", date) : null;
   
    data = Arrays.stream(data).filter(x -> x != null && x != "null").toArray(String[]::new);
    
    try{
			String sql;
			if (data.length > 0) {
				sql = "from Record R where " + String.join(" and ", data);
			} else {
				sql = "from Record";
			}	
		Query<Record> query = session.createQuery(sql, Record.class);
    	entities = query.list();
       
       return entities;
       
    }catch (HibernateException e) {
		new AlertDialog("Error", "Błąd połączenia z bazą danych.", "Wystąpił błąd z połączeniem z bazą danych. Spróbuj ponownie uruchomić aplikację. \n\nW razie częstego występowania błędu, skontaktuj się z administratorem podając poniższe szczegóły błędu.", AlertType.ERROR, e);
       e.printStackTrace(); 
    }finally {
  	  session.close(); 
    }
    return entities;
 }

public List<Record> listRecords(User user, Project project, LocalDate dateStart, LocalDate dateEnd){
    if (dateEnd == null){
    	return listRecords(user, project, dateStart);
    }
	
	Session session = factory.openSession();
    List<Record> entities = null;
    
    // Creating a string and removing all null values
    String[] data = new String[4];
    
    data[0] = user != null ? "R.user = " + String.valueOf(user.getId()) : null;
    data[1] = project != null ? "R.project = " + String.valueOf(project.getId()) : null;
    data[2] = dateStart != null ? "R.date between " + String.format("'%s'", dateStart) : null;
    data[3] = dateEnd != null ? String.format("'%s'", dateEnd) : null;
   
    data = Arrays.stream(data).filter(x -> x != null && x != "null").toArray(String[]::new);
    
    try{
			String sql;
			if (data.length > 0) {
				sql = "from Record R where " + String.join(" and ", data);
			} else {
				sql = "from Record";
			}	
		Query<Record> query = session.createQuery(sql, Record.class);
    	entities = query.list();
       
       return entities;
       
    }catch (HibernateException e) {
		new AlertDialog("Error", "Błąd połączenia z bazą danych.", "Wystąpił błąd z połączeniem z bazą danych. Spróbuj ponownie uruchomić aplikację. \n\nW razie częstego występowania błędu, skontaktuj się z administratorem podając poniższe szczegóły błędu.", AlertType.ERROR, e);
       e.printStackTrace(); 
    }finally {
  	  session.close(); 
    }
    return entities;
 }


}