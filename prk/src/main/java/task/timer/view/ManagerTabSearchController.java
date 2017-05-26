package task.timer.view;

import java.util.LinkedList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import task.timer.model.AbstractEntity;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.Project;
import task.timer.model.User;

public class ManagerTabSearchController {
	ManageEntity MMUser = new MEFactory().getUserEntityManager();
	ManageEntity MMProject = new MEFactory().getProjectEntityManager();
	
	@FXML private ChoiceBox chooseUser;
	@FXML private ChoiceBox chooseProject;

	@FXML private void initialize(){
		
		
		chooseUser.getItems().addAll(readUsersFromDataBase());
		chooseProject.getItems().addAll(readProjectsFromDataBase());

	}
	
	private List<String> readUsersFromDataBase(){
		List<String> listUsers;
		listUsers = new LinkedList<String>();		
		List<AbstractEntity> users = MMUser.list();	
		for (int i=0; i<users.size(); i++){
			User userFromDb =   (User) users.get(i);		
			listUsers.add(userFromDb.getFirstName()+ " " + userFromDb.getLastName());
		}		
		return listUsers;
	}
	
	private List<String> readProjectsFromDataBase(){
		List<String> listProjects;
		listProjects = new LinkedList<String>();		
		List<AbstractEntity> projects = MMProject.list();	
		for (int i=0; i<projects.size(); i++){
			Project projectFromDb =   (Project) projects.get(i);		
			listProjects.add(projectFromDb.getName());
		}		
		return listProjects;
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
