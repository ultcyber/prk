package task.timer.model;
import java.time.LocalDate;
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

public Set<User> listUsersOfProject(int idProject){
    Session session = factory.openSession();
    Transaction tx = null;
    Set<User> entities = null;
    try{
       tx = session.beginTransaction();
       Project project = (Project) session.get(Project.class, idProject);
      
       entities = project.getUsers(); 
       Hibernate.initialize(entities);
       
       tx.commit();
       return entities;
    }catch (HibernateException e) {
       if (tx!=null) tx.rollback();
       e.printStackTrace(); 
    }
    finally{
    	session.close();
    }
    
    return entities;
 }

public List<AbstractEntity> listRecords(User user, Project project, LocalDate date){
    Session session = factory.openSession();
    Transaction tx = null;
    List<AbstractEntity> entities = null;
    try{
       tx = session.beginTransaction();
       String hql = "from Record R where R.user = " + user;
       Query query = session.createQuery(hql); 
       entities = query.list();
      
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

}