package task.timer.view;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import task.timer.Main;
import task.timer.helper.AlertDialog;
import task.timer.model.AbstractEntity;
import task.timer.model.Project;
import task.timer.model.User;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class ManagerTabAddProjectController {

	@FXML private VBox vbox1;
	@FXML private VBox vbox2;
	@FXML private HBox hbox;
	@FXML private AnchorPane anchorpane;
	@FXML private Rectangle rectangle1;
	@FXML private Rectangle rectangle2;
	
	private List<AbstractEntity> projects;
	
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
	
	@FXML private MenuItem deleteMenuItem;
	
	private final ObservableList<Project> dataProjects = 
			FXCollections.observableArrayList();
	
	private final ObservableList<User> dataUsersOutOfProject = 
			FXCollections.observableArrayList();
	
	private final ObservableList<User> dataUsersInProject = 
			FXCollections.observableArrayList();

	@FXML private void initialize(){	
		rectangle1.widthProperty().bind(vbox1.widthProperty());		
		rectangle2.widthProperty().bind(hbox.widthProperty().subtract(7));

		
		userNameColumn.prefWidthProperty().bind(usersOutOfProjectTable.widthProperty().divide(2));
		userLastNameColumn.prefWidthProperty().bind(usersOutOfProjectTable.widthProperty().divide(2));
		
		userNameInProjectColumn.prefWidthProperty().bind(usersInProjectTable.widthProperty().divide(2));
		userLastNameInProjectColumn.prefWidthProperty().bind(usersInProjectTable.widthProperty().divide(2));
		
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
		
		readData();
	}
	
	@FXML private void readData(){
		clearFields();
		showProjects();			
		showDataOfProject(null);
	}
	
	@FXML private void hideLackProjectNameLabel1(){
		lackProjectNameLabel1.setVisible(false);
	}
	
	@FXML private void hideLackProjectNameLabel2(){
		lackProjectNameLabel2.setVisible(false);
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
				showProjects();
				projectsTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na bieżący wiersz	
				new AlertDialog("Operacja zakończona", "Zaktualizowano dane", AlertType.INFORMATION);
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
				Main.getMMProject().add(
					new Project(newProjectNameField.getText()));	
				
				newProjectNameField.setText("");
				currentPositionInTableView = projectsTable.getItems().size();
				showProjects();
				projectsTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na ostatni wiersz
				
				new AlertDialog("Operacja zakończona", "Dodano projekt", AlertType.INFORMATION);
				}	
				else {
					lackProjectNameLabel1.setText("taki projekt już istnieje");
					lackProjectNameLabel1.setVisible(true);
				}
	}
	
	@FXML private void deleteProject() throws ClassNotFoundException{
		if (projectsTable.getSelectionModel().getSelectedIndex() > -1)
		{
			AlertDialog dialog = new AlertDialog("Potwierdź usunięcie projektu", "Czy na pewno chcesz usunąć projekt?", AlertType.CONFIRMATION);
			if (dialog.getResult() == ButtonType.OK) {
				Main.getMMProject().delete(projectsTable.getSelectionModel().getSelectedItem().getId());
				showProjects();
				clearFields();
				new AlertDialog("Operacja zakończona", "Projekt został usunięty", AlertType.INFORMATION);
			}
		}
	}
		
	private void showProjects(){ 	
		readProjects();
		projectsTable.setItems(dataProjects);
	}
	
	private void readProjects(){
		projects = Main.getMMProject().list();
		dataProjects.clear();		
		projects.forEach(projectFromDataBase -> dataProjects.add((Project) projectFromDataBase));
	}
		
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
	
	private void readUsersInProject(Project project){
		projectNameField.setText(project.getName());			
		dataUsersInProject.clear();
		Set<User> usersOfProject = project.getUsers();	
		
		Iterator<User> wskaznik = usersOfProject.iterator();	
		wskaznik.forEachRemaining(userToAdd -> dataUsersInProject.add(userToAdd));		
	}
	
	private void readUsersOutOfProject(ObservableList<User> usersOfProject){
		List<AbstractEntity> users = Main.getMMUser().list();	
		dataUsersOutOfProject.clear();		
		users.forEach(userFromDataBase -> {
			// dodaj do TableView jeśli userFromDataBase nie jest jeszcze przypięty do projektu
			if (!usersOfProject.contains(userFromDataBase))
				dataUsersOutOfProject.add((User) userFromDataBase);
		});		
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
