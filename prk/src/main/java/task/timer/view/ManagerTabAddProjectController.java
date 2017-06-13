package task.timer.view;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import task.timer.Main;
import task.timer.helper.AlertDialog;
import task.timer.model.AbstractEntity;
import task.timer.model.Project;
import task.timer.model.User;

/**
 * The Class ManagerTabAddProjectController.
 * @author Marcin Zglenicki
 * @since JDK 1.8
 */
public class ManagerTabAddProjectController {
	
	/** The hbox. */
	@FXML private HBox hbox;
	
	/** The project. */
	@FXML private AnchorPane project;
	
	/** The rectangle 1. */
	@FXML private Rectangle rectangle1;
	
	/** The rectangle 2. */
	@FXML private Rectangle rectangle2;
	
	/** Table with projects. */
	@FXML private TableView<Project> projectsTable;
	
	/** The project name column. */
	@FXML private TableColumn<Project, String> projectNameColumn;
	
	/** Table with users out of project. */
	@FXML private TableView<User> usersOutOfProjectTable;
	
	/** The user name column. */
	@FXML private TableColumn<User, String> userNameColumn;
	
	/** The user last name column. */
	@FXML private TableColumn<User, String> userLastNameColumn;
	
	/** Table with users in project. */
	@FXML private TableView<User> usersInProjectTable;
	
	/** The users name in project column. */
	@FXML private TableColumn<User, String> userNameInProjectColumn;
	
	/** The users last name in project column. */
	@FXML private TableColumn<User, String> userLastNameInProjectColumn;
	
	/** The project name field. */
	@FXML private TextField projectNameField;
	
	/** The new project name field. */
	@FXML private TextField newProjectNameField;
	
	/** Label1 - lack new project name. */
	@FXML private Label lackProjectNameLabel1;
	
	/** Label2 - lack exist project name. */
	@FXML private Label lackProjectNameLabel2;
	
	/** The delete menu item. */
	@FXML private MenuItem deleteMenuItem;
	
	/** List of projects. */
	private List<AbstractEntity> projects;
	
	/** List of projects - preparing to show in TableView. */
	private final ObservableList<Project> dataProjects = 
			FXCollections.observableArrayList();
	
	/** List of users that are out of project - preparing to show in TableView. */
	private final ObservableList<User> dataUsersOutOfProject = 
			FXCollections.observableArrayList();
	
	/** List of users that are in project - preparing to show in TableView. */
	private final ObservableList<User> dataUsersInProject = 
			FXCollections.observableArrayList();

	/**
	 * Initialize graphics interface.
	 */
	@FXML private void initialize(){	
		binding(); // binding selected items on graphics interface
		
		usersInProjectTable.setPlaceholder(new Label("Lista pusta - brak danych"));
		usersOutOfProjectTable.setPlaceholder(new Label("Lista pusta - brak danych"));
		
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
		
		refreshData();
	}
	
	/**
	 * Preparing fields to show informations about projects. 
	 * Read projects from database.
	 * Show projects in TableView.
	 * Show users in and out of project
	 */
	@FXML private void refreshData(){
		clearFields();
		readProjects();
		showProjects();		
		// shows users in/out of project
		showDataOfProject(null); 
	}
	
	/**
	 * Hide label 1 - lack project name.
	 */
	@FXML private void hideLackProjectNameLabel1(){
		lackProjectNameLabel1.setVisible(false);
	}
	
	/**
	 * Hide label 2 - lack project name.
	 */
	@FXML private void hideLackProjectNameLabel2(){
		lackProjectNameLabel2.setVisible(false);
	}
		
	/**
	 * Adds the user to project.
	 */
	@FXML private void addUserToProject(){
		if (usersOutOfProjectTable.getSelectionModel().getSelectedIndex() > -1){	
			User userToAdd = usersOutOfProjectTable.getSelectionModel().selectedItemProperty().get();
			dataUsersInProject.add(userToAdd);
			dataUsersOutOfProject.remove(userToAdd);
		}
	}
	
	/**
	 * Removes the user from project.
	 */
	@FXML private void removeUserFromProject(){
		if (usersInProjectTable.getSelectionModel().getSelectedIndex() > -1){	
			User userToRemove = usersInProjectTable.getSelectionModel().selectedItemProperty().get();
			dataUsersInProject.remove(userToRemove);			
			dataUsersOutOfProject.add(userToRemove);
		}
	}
	
	/**
	 * Update project.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	@FXML private void updateProject() throws ClassNotFoundException{	
		int currentPositionInTableView;
		if (isProjectNameFieldFull()) 
			if (isProjectUnique(projectsTable.getSelectionModel().getSelectedItem().getId())){
				Set<User> usersToUpdate = new HashSet<User>();			
				for (int i=0; i < dataUsersInProject.size(); i++)
					usersToUpdate.add(dataUsersInProject.get(i));							
				usersInProjectTable.setItems(dataUsersInProject);						
				Main.getMMProject().update(
					new Project(
						projectsTable.getSelectionModel().getSelectedItem().getId(),
						projectNameField.getText(),
						usersToUpdate
						));
				currentPositionInTableView = projectsTable.getSelectionModel().getSelectedIndex(); // zapamiętaj bieżące podświetlenie w tabeli	
				readProjects();
				showProjects();
				projectsTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na bieżący wiersz	
				new AlertDialog("Operacja zakończona", "Zaktualizowano dane", AlertType.INFORMATION);
			}
			else {
				lackProjectNameLabel2.setText("taki projekt już istnieje");
				lackProjectNameLabel2.setVisible(true);
			}
	}
	
	/**
	 * Adds the project.
	 */
	@FXML private void addProject(){
		int currentPositionInTableView;
		if (isNewProjectNameFieldsFull()) 
			if (isNewProjectUnique(newProjectNameField.getText())){
				Main.getMMProject().add(
					new Project(newProjectNameField.getText()));	
				
				newProjectNameField.setText("");
				currentPositionInTableView = projectsTable.getItems().size();
				readProjects();
				showProjects();
				projectsTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na ostatni wiersz
				
				new AlertDialog("Operacja zakończona", "Dodano projekt", AlertType.INFORMATION);
				}	
				else {
					lackProjectNameLabel1.setText("taki projekt już istnieje");
					lackProjectNameLabel1.setVisible(true);
				}
	}
	
	/**
	 * Delete project.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	@FXML private void deleteProject() throws ClassNotFoundException{
		if (projectsTable.getSelectionModel().getSelectedIndex() > -1)
		{
			AlertDialog dialog = new AlertDialog("Potwierdź usunięcie projektu", "Czy na pewno chcesz usunąć projekt?", AlertType.CONFIRMATION);
			if (dialog.getResult() == ButtonType.OK) {
				Main.getMMProject().delete(projectsTable.getSelectionModel().getSelectedItem().getId());
				readProjects();
				showProjects();
				clearFields();
				new AlertDialog("Operacja zakończona", "Projekt został usunięty", AlertType.INFORMATION);
			}
		}
	}
	
	/**
	 * Binding elements on graphic interface.
	 */
	private void binding(){
		rectangle1.widthProperty().bind(project.widthProperty().subtract(10));	
		rectangle1.heightProperty().bind(project.heightProperty().subtract(10));
		
		rectangle2.widthProperty().bind(hbox.widthProperty().subtract(7));
		rectangle2.heightProperty().bind(hbox.heightProperty().subtract(20));

		
		userNameColumn.prefWidthProperty().bind(usersOutOfProjectTable.widthProperty().divide(2));
		userLastNameColumn.prefWidthProperty().bind(usersOutOfProjectTable.widthProperty().divide(2));
		
		userNameInProjectColumn.prefWidthProperty().bind(usersInProjectTable.widthProperty().divide(2));
		userLastNameInProjectColumn.prefWidthProperty().bind(usersInProjectTable.widthProperty().divide(2));
	}
	
	/**
	 * Read projects.
	 */
	private void readProjects(){
		projects = Main.getMMProject().list();
		dataProjects.clear();		
		projects.forEach(projectFromDataBase -> dataProjects.add((Project) projectFromDataBase));
	}
	
	
	/**
	 * Show projects.
	*/
	private void showProjects(){ 	
		projectsTable.setItems(dataProjects);
	}
		
	/**
	 * Show data of project: users in and out of project
	 *
	 * @param project - current project
	 */
	private void showDataOfProject(Project project){
		if (project != null){
			readUsersInProject(project);
			usersInProjectTable.setItems(dataUsersInProject);
			
			readUsersOutOfProject(dataUsersInProject);
			usersOutOfProjectTable.setItems(dataUsersOutOfProject);
		}
		else {
			projectNameField.setText("");
		}
		hideLackProjectNameLabel1();
		hideLackProjectNameLabel2();
	}
	
	/**
	 * Read users in project.
	 *
	 * @param project - current project
	 */
	private void readUsersInProject(Project project){
		projectNameField.setText(project.getName());			
		dataUsersInProject.clear();
		Set<User> usersOfProject = project.getUsers();	
		
		Iterator<User> wskaznik = usersOfProject.iterator();	
		wskaznik.forEachRemaining(userToAdd -> dataUsersInProject.add(userToAdd));		
	}
	
	/**
	 * Read a list of users and discards those that are already in the project.
	 *
	 * @param usersOfProject - users in project
	 */
	private void readUsersOutOfProject(ObservableList<User> usersOfProject){
		List<AbstractEntity> users = Main.getMMUser().list();	
		dataUsersOutOfProject.clear();		
		users.forEach(userFromDataBase -> {
			if (!usersOfProject.contains(userFromDataBase))
				dataUsersOutOfProject.add((User) userFromDataBase);
		});		
	}
	
	/**
	 * Checks if field of new project name is full.
	 *
	 * @return true, if field of new project name is full
	 */
	private boolean isNewProjectNameFieldsFull(){
		if (newProjectNameField.getText().equals("")) {
			lackProjectNameLabel1.setText("to pole nie może być puste");
			lackProjectNameLabel1.setVisible(true);
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if field of project name is full.
	 *
	 * @return true, if field of project name is full
	 */
	private boolean isProjectNameFieldFull(){
		if (projectNameField.getText().equals("")) {
			lackProjectNameLabel2.setText("to pole nie może być puste");
			lackProjectNameLabel2.setVisible(true);
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if project name is unique.
	 *
	 * @param projectName - name of the current project
	 * @return true, if project name is unique
	 */
	private boolean isNewProjectUnique(String projectName){
		for (int i=0; i<projects.size(); i++){
			Project projectFromDb = (Project) projects.get(i);
			if (projectName.equals(projectFromDb.getName())) 
				return false;			
		}	
		return true;
	}
	
	/**
	 * Checks if project name is unique except for the current project.
	 *
	 * @param currentProjectId the current project id
	 * @return true, if project name is unique
	 */
	private boolean isProjectUnique(int currentProjectId){
		for (int i=0; i<projects.size(); i++){
			Project projectFromDb = (Project) projects.get(i);
			if (currentProjectId == projectFromDb.getId()) continue;
			if (projectFromDb.getName().equals(projectNameField.getText())) 
				return false;			
		}	
		return true;
	}
	
	/**
	 * Clear fields on the graphical interface.
	 */
	protected void clearFields(){
		projectsTable.getSelectionModel().clearSelection();
		dataUsersOutOfProject.clear();
		dataUsersInProject.clear();
		newProjectNameField.setText("");
		projectNameField.setText("");
		lackProjectNameLabel1.setText("");
		lackProjectNameLabel2.setText("");
	}
}
