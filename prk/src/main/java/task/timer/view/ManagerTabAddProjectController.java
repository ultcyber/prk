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

	List<AbstractEntity> projects;
	
	@FXML private TableView<Project> projectsTable;
	@FXML private TableColumn<Project, String> projectNameColumn;
	
	@FXML private TableView<User> usersOutOfProjectTable;
	@FXML private TableColumn<User, String> userNameColumn;
	@FXML private TableColumn<User, String> userLastNameColumn;
	
	@FXML private TableView<User> usersInProjectTable;
	@FXML private TableColumn<User, String> userNameInProjectColumn;
	@FXML private TableColumn<User, String> userLastNameInProjectColumn;
	
	@FXML private TextField projectNameField;
	@FXML private TextField newProjectNameField;
	
	@FXML private Label lackProjectNameLabel1;
	@FXML private Label lackProjectNameLabel2;
	
	private final ObservableList<Project> dataProjects = 
			FXCollections.observableArrayList();
	
	private final ObservableList<User> dataUsersOutOfProject = 
			FXCollections.observableArrayList();
	
	private final ObservableList<User> dataUsersInProject = 
			FXCollections.observableArrayList();

	@FXML private void initialize(){
		usersInProjectTable.setPlaceholder(new Label("Brak pracownik�w"));
		usersOutOfProjectTable.setPlaceholder(new Label("Brak pracownik�w"));
		
		projectNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getNameProjectProperty());		
		projectsTable.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> showDataOfProject(newValue));
		
		userNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getFirstNameProperty());
		userLastNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getLastNameProperty());	
		
		userNameInProjectColumn.setCellValueFactory(cellData ->
			cellData.getValue().getFirstNameProperty());
		userLastNameInProjectColumn.setCellValueFactory(cellData ->
			cellData.getValue().getLastNameProperty());	
			
		lackProjectNameLabel1.setVisible(false);
		lackProjectNameLabel2.setVisible(false);
		
		readAndShowProjectsFromDataBase();	
		
		showDataOfProject(null);
	}
	
	@FXML private void hideLackProjectNameLabel1(){
		lackProjectNameLabel1.setVisible(false);
	}
	
	@FXML private void hideLackProjectNameLabel2(){
		lackProjectNameLabel2.setVisible(false);
	}
	
	/*public void refreshAvailableUsersOnInterface(){
		int positionInTabelView = projectsTable.getSelectionModel().getSelectedIndex();
		readAndShowProjectsFromDataBase();
		projectsTable.getSelectionModel().select(positionInTabelView);
		
		showDataOfProject(projectsTable.getSelectionModel().getSelectedItem());
	}*/
	
	private void readAndShowProjectsFromDataBase(){ 	
		projects = DAO.MMProject.list();			
		dataProjects.clear();
		for (int i=0; i<projects.size(); i++){				
			Project projectFromDb =   (Project) projects.get(i);	
			dataProjects.add(projectFromDb);					
		}		
		projectsTable.setItems(dataProjects);
	}
		
	private void showDataOfProject(Project project){
		if (project != null){
			projectNameField.setText(project.getName());			
			dataUsersInProject.clear();
			Set<User> usersOfProject = project.getUsers();			
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
		hideLackProjectNameLabel1();
		hideLackProjectNameLabel2();
	}
	
	private void readAndShowAvailableUsersFromDataBase(Set<User> usersOfProject){
		List<AbstractEntity> users = DAO.MMUser.list();	
		dataUsersOutOfProject.clear();
		for (int i=0; i<users.size(); i++){
			User userFromDb =   (User) users.get(i);				
			// dodaj do TableView jeśli userFromDb nie jest jeszcze przypięty do projektu
			if (!usersOfProject.contains(userFromDb))
				dataUsersOutOfProject.add(userFromDb);
		}		
		usersOutOfProjectTable.setItems(dataUsersOutOfProject);
	}
	
	
	@FXML private void addUserToProject(){
		if (usersOutOfProjectTable.getSelectionModel().getSelectedIndex() > -1){	
			User userToAdd = usersOutOfProjectTable.getSelectionModel().selectedItemProperty().get();
			dataUsersInProject.add(userToAdd);
			dataUsersOutOfProject.remove(userToAdd);
		}
	}
	
	@FXML private void removeUserFromProject(){
		if (usersInProjectTable.getSelectionModel().getSelectedIndex() > -1){	
			User userToRemove = usersInProjectTable.getSelectionModel().selectedItemProperty().get();
			dataUsersInProject.remove(userToRemove);			
			dataUsersOutOfProject.add(userToRemove);
		}
	}
	
	@FXML private void updateProject() throws ClassNotFoundException{	
		int currentPositionInTableView;
		if (isProjectNameFieldFull()) 
			if (isProjectUnique(projectsTable.getSelectionModel().getSelectedItem().getId())){
				Set<User> userToUpdate = new HashSet<User>();			
				for (int i=0; i < dataUsersInProject.size(); i++)
					userToUpdate.add(dataUsersInProject.get(i));							
				usersInProjectTable.setItems(dataUsersInProject);
				currentPositionInTableView = projectsTable.getSelectionModel().getSelectedIndex(); // zapamiętaj bieżące podświetlenie w tabeli			
				DAO.MMProject.update(
					new Project(
						projectsTable.getSelectionModel().getSelectedItem().getId(),
						projectNameField.getText(),
						userToUpdate
						));
				readAndShowProjectsFromDataBase();
				projectsTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na bieżący wiersz		
				MainManagerController.changedProjectsData = true;
			}
			else {
				lackProjectNameLabel2.setText("taki projekt już istnieje");
				lackProjectNameLabel2.setVisible(true);
			}
	}
	
	@FXML private void addProject(){
		int currentPositionInTableView;
		if (isNewProjectNameFieldsFull()) 
			if (isNewProjectUnique(newProjectNameField.getText())){
				Integer projectID = DAO.MMProject.add(
					new Project(
							newProjectNameField.getText()));	
					newProjectNameField.setText("");
					MainManagerController.changedProjectsData = true;
				}	
				else {
					lackProjectNameLabel1.setText("taki projekt już istnieje");
					lackProjectNameLabel1.setVisible(true);
				}
		currentPositionInTableView = projectsTable.getItems().size();
		readAndShowProjectsFromDataBase();
		projectsTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na ostatni wiersz
	}
	
	@FXML private void deleteProject() throws ClassNotFoundException{
		if (projectsTable.getSelectionModel().getSelectedIndex() >= -1)	// rób jeśli jest zaznaczony projekt	
		{
			DAO.MMProject.delete(projectsTable.getSelectionModel().getSelectedItem().getId());
			readAndShowProjectsFromDataBase();
			clearFields();
			MainManagerController.changedProjectsData = true;
		}
	}
	
	
	private boolean isNewProjectNameFieldsFull(){
		if (newProjectNameField.getText().equals("")) {
			lackProjectNameLabel1.setText("to pole nie może być puste");
			lackProjectNameLabel1.setVisible(true);
			return false;
		}
		return true;
	}
	
	private boolean isProjectNameFieldFull(){
		if (projectNameField.getText().equals("")) {
			lackProjectNameLabel2.setText("to pole nie może być puste");
			lackProjectNameLabel2.setVisible(true);
			return false;
		}
		return true;
	}
	
	// metoda sprawdza czy projekt jest unikalny
	private boolean isNewProjectUnique(String projectName){
		for (int i=0; i<projects.size(); i++){
			Project projectFromDb = (Project) projects.get(i);
			if (projectName.equals(projectFromDb.getName())) 
				return false;			
		}	
		return true;
	}
	
	// metoda sprawdza czy projekt jest unikalny bez uwzględniania bieżacego projektu
	private boolean isProjectUnique(int currentProjectId){
		for (int i=0; i<projects.size(); i++){
			Project projectFromDb = (Project) projects.get(i);
			if (currentProjectId == projectFromDb.getId()) continue;
			if (projectFromDb.getName().equals(projectNameField.getText())) 
				return false;			
		}	
		return true;
	}

	public void clearFields(){
		projectsTable.getSelectionModel().clearSelection();
		dataUsersOutOfProject.clear();
		dataUsersInProject.clear();
	}
}
