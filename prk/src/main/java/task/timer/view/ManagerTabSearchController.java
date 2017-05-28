package task.timer.view;

import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import task.timer.model.AbstractEntity;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
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
	@FXML private TableColumn<Record, String> startTimeColumn;
	@FXML private TableColumn<Record, String> stopTimeColumn;
	
	@FXML private DatePicker date;
	
	private final ObservableList<Record> dataRecords = 
			FXCollections.observableArrayList();

	@FXML private void initialize(){	
		chooseUser.getItems().addAll(readUsersFromDataBase());
		chooseProject.getItems().addAll(readProjectsFromDataBase());
	}
	
	private List<String> readUsersFromDataBase(){
		List<String> listUsers;
		listUsers = new LinkedList<String>();		
		List<AbstractEntity> users = LoginWindowController.MMUser.list();	
		for (int i=0; i<users.size(); i++){
			User userFromDb =   (User) users.get(i);		
			listUsers.add(userFromDb.getFirstName()+ " " + userFromDb.getLastName());
		}		
		return listUsers;
	}
	
	private List<String> readProjectsFromDataBase(){
		List<String> listProjects;
		listProjects = new LinkedList<String>();		
		List<AbstractEntity> projects = LoginWindowController.MMProject.list();	
		for (int i=0; i<projects.size(); i++){
			Project projectFromDb =   (Project) projects.get(i);		
			listProjects.add(projectFromDb.getName());
		}		
		return listProjects;
	}
	
	@FXML private void searchAndShowRecords(){ 
		//List<AbstractEntity> listRecords = new LinkedList<AbstractEntity>();
		/*
		listRecords = LoginWindowController.MMRecord.listRecords(
				(User) chooseUser.getSelectionModel().getSelectedItem(), 
				(Project) chooseProject.getSelectionModel().getSelectedItem(), 
				date.getValue());*/
	
		List<AbstractEntity> listRecords = LoginWindowController.MMRecord.list();
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
