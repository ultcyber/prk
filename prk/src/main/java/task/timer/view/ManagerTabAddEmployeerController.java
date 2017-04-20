package task.timer.view;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import arch.task.timer.model.ManageUsers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import task.timer.model.AbstractEntity;
import task.timer.model.ManageEntity;
import task.timer.model.RecordFx;
import task.timer.model.User;

public class ManagerTabAddEmployeerController {
	ManageUsers MM;
    private SessionFactory sessionFactory;
    
	private final ObservableList<User> dataUsers = 
			FXCollections.observableArrayList();
	
	@FXML private void initialize(){
		try {
			sessionFactory=new Configuration().configure().buildSessionFactory();}
		catch(Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
		MM = new ManageUsers(sessionFactory);
		
		List<User> users = MM.listUsers();
		
		User userFromDb = users.get(users.size()-1);
		
		
		dataUsers.add(userFromDb);
	}
}
