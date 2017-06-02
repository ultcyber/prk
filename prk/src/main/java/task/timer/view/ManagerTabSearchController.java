package task.timer.view;

import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import task.timer.model.AbstractEntity;
import task.timer.model.Project;
import task.timer.model.Record;
import task.timer.model.User;

public class ManagerTabSearchController {
//	ManageEntity MMProject = new MEFactory().getProjectEntityManager();

	
	@FXML private ChoiceBox chooseUser;
	@FXML private ChoiceBox chooseProject;
	
	@FXML private TableView<Record> recordTable;
	@FXML private TableColumn<Record, String> userNameColumn;
	@FXML private TableColumn<Record, String> projectNameColumn;
	@FXML private TableColumn<Record, String> descriptionColumn;
	@FXML private TableColumn<Record, String> dateColumn;
	@FXML private TableColumn<Record, String> startTimeColumn;
	@FXML private TableColumn<Record, String> stopTimeColumn;
	
	@FXML private DatePicker date;
	private List<AbstractEntity> users;
	private List<AbstractEntity> projects;
	
	private final ObservableList<Record> dataRecords = 
			FXCollections.observableArrayList();

	@FXML private void initialize(){	
		chooseUser.getItems().addAll(readUsersFromDataBase());
		chooseProject.getItems().addAll(readProjectsFromDataBase());
		
		chooseUser.getSelectionModel().select(0);
		chooseProject.getSelectionModel().select(0);
		
		recordTable.setPlaceholder(new Label("Dla wybranych kryteriów - brak danych do wyświetlenia"));
		
		userNameColumn.setCellValueFactory(cellData ->
		cellData.getValue().getUserName());
		
		projectNameColumn.setCellValueFactory(cellData ->
		cellData.getValue().getProjectName());
		
		descriptionColumn.setCellValueFactory(cellData ->
		cellData.getValue().getDescriptionProperty());
		
		dateColumn.setCellValueFactory(cellData ->
		cellData.getValue().getDateProperty());
		
		startTimeColumn.setCellValueFactory(cellData ->
		cellData.getValue().getTimeStartProperty());
		
		stopTimeColumn.setCellValueFactory(cellData ->
		cellData.getValue().getTimeStopProperty());
	}
	
	private List<String> readUsersFromDataBase(){
		List<String> listUsers;
		listUsers = new LinkedList<String>();		
		users = DAO.MMUser.list();	
		listUsers.add("Wszyscy");
		for (int i=0; i<users.size(); i++){
			User userFromDb =   (User) users.get(i);		
			listUsers.add(userFromDb.getFirstName()+ " " + userFromDb.getLastName());
		}		
		return listUsers;
	}
	
	private List<String> readProjectsFromDataBase(){
		List<String> listProjects;
		listProjects = new LinkedList<String>();		
		projects = DAO.MMProject.list();	
		listProjects.add("Wszystkie");
		for (int i=0; i<projects.size(); i++){
			Project projectFromDb =   (Project) projects.get(i);		
			listProjects.add(projectFromDb.getName());
		}		
		return listProjects;
	}
	
	@FXML private void searchAndShowRecords(){ 
		List<Record> listRecords = DAO.MMRecord.listRecords(
				chooseUser.getSelectionModel().getSelectedIndex() > 0 ? (User) users.get(chooseUser.getSelectionModel().getSelectedIndex()-1) : null,
				chooseProject.getSelectionModel().getSelectedIndex() > 0? (Project) projects.get(chooseProject.getSelectionModel().getSelectedIndex()-1) : null, 
				date.getValue());
		
		dataRecords.clear();
		for (int i=0; i<listRecords.size(); i++){				
			Record recordFromDb =   (Record) listRecords.get(i);
			dataRecords.add(recordFromDb);					
		}	
		
		recordTable.setItems(dataRecords);
	}
	
	public void refreshChooseUser(){
		chooseUser.getItems().clear();
		chooseUser.getItems().addAll(readUsersFromDataBase());
	}
	
	public void refreshChooseProject(){
		chooseProject.getItems().clear();
		chooseProject.getItems().addAll(readProjectsFromDataBase());
	}
}
