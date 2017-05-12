package task.timer.view;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import task.timer.model.AbstractEntity;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.Project;
import task.timer.model.User;

public class ManagerTabAddProjectController {
	ManageEntity MMProject = new MEFactory().getProjectEntityManager();
	ManageEntity MMUser = new MEFactory().getUserEntityManager();
	
	@FXML private TextField newProjectName;
	@FXML private Button addButton;
	
	@FXML private TableView<Project> projectsTable;
	@FXML private TableColumn<Project, String> projectNameColumn;
	
	@FXML private TableView<User> usersTable;
	@FXML private TableColumn<User, String> userNameColumn;
	@FXML private TableColumn<User, String> userLastNameColumn;
	
	@FXML private TableView<User> usersInProjectTable;
	@FXML private TableColumn<User, String> userNameInProjectColumn;
	@FXML private TableColumn<User, String> userLastNameInProjectColumn;
	
	@FXML private TextField projectNameField;
	
	private final ObservableList<Project> dataProjects = 
			FXCollections.observableArrayList();
	
	private final ObservableList<User> dataUsers = 
			FXCollections.observableArrayList();
	
	private final ObservableList<User> dataUsersInProject = 
			FXCollections.observableArrayList();

	@FXML private void initialize(){
		projectNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getNameProjectProperty());		
		projectsTable.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> refreshInformationsFromTableView(newValue));
		
		userNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getFirstNameProperty());
		userLastNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getLastNameProperty());	
		
		userNameInProjectColumn.setCellValueFactory(cellData ->
			cellData.getValue().getFirstNameProperty());
		userLastNameInProjectColumn.setCellValueFactory(cellData ->
			cellData.getValue().getLastNameProperty());	
			
		readAndShowProjectsFromDataBase();	
		
		refreshInformationsFromTableView(null);
	}
	
	private void readAndShowProjectsFromDataBase(){
		List<AbstractEntity> projects; 	
		projects = MMProject.list();			
		dataProjects.clear();
		for (int i=0; i<projects.size(); i++){				
			Project projectFromDb =   (Project) projects.get(i);	
			dataProjects.add(projectFromDb);					
		}		
		projectsTable.setItems(dataProjects);
	}
		
	private void refreshInformationsFromTableView(Project project){
		if (project != null){
			projectNameField.setText(project.getName());			
			dataUsersInProject.clear();
			Set<User> usersOfProject = MMProject.listUsersOfProject(project.getId());			
			if (!usersOfProject.isEmpty()){		
				Iterator<User> wskaznik = usersOfProject.iterator();					
				for (User i : usersOfProject)
					if (wskaznik.hasNext()) 
						dataUsersInProject.add(wskaznik.next());				
			}
			usersInProjectTable.setItems(dataUsersInProject);
			readAndShowAvailableUsersFromDataBase(usersOfProject);
		}
		else {
			projectNameField.setText("");
		}
	}
	
	private void readAndShowAvailableUsersFromDataBase(Set<User> usersOfProject){
		List<AbstractEntity> users = MMUser.list();	
		dataUsers.clear();
		for (int i=0; i<users.size(); i++){
			User userFromDb =   (User) users.get(i);				
			// dodaj do TableView jeśli userFromDb nie jest jeszcze przypięty do projektu
			if (!usersOfProject.contains(userFromDb))
				dataUsers.add(userFromDb);
		}		
		usersTable.setItems(dataUsers);
	}
	
	
	@FXML private void addUserToProject(){
		if (usersTable.getSelectionModel().getSelectedIndex() > -1){	
			User userToAdd = usersTable.getSelectionModel().selectedItemProperty().get();
			dataUsersInProject.add(userToAdd);
			dataUsers.remove(userToAdd);
		}
	}
	
	@FXML private void removeUserFromProject(){
		if (usersInProjectTable.getSelectionModel().getSelectedIndex() > -1){	
			User userToRemove = usersInProjectTable.getSelectionModel().selectedItemProperty().get();
			dataUsersInProject.remove(userToRemove);			
			dataUsers.add(userToRemove);
		}
	}
	
	@FXML private void updateProject() throws ClassNotFoundException{	
		int currentPositionInTableView;
		if (!projectNameField.getText().equals("")) {
			Set<User> userToUpdate = new HashSet<User>();			
			for (int i=0; i < dataUsersInProject.size(); i++)
				userToUpdate.add(dataUsersInProject.get(i));							
			usersInProjectTable.setItems(dataUsersInProject);
			currentPositionInTableView = projectsTable.getSelectionModel().getSelectedIndex(); // zapamiętaj bieżące podświetlenie w tabeli			
			MMProject.update(
					new Project(
						projectsTable.getSelectionModel().getSelectedItem().getId(),
						projectNameField.getText(),
						userToUpdate
						));
			readAndShowProjectsFromDataBase();
			projectsTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na bieżący wiersz		
		}
	}
	
	@FXML private void addProject(){
		int currentPositionInTableView;
		if (!newProjectName.getText().equals("")) {
			Integer projectID = MMProject.add(
					new Project(
							newProjectName.getText()));			
		}	
		currentPositionInTableView = projectsTable.getItems().size();
		readAndShowProjectsFromDataBase();
		projectsTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na ostatni wiersz
	}
	
}
