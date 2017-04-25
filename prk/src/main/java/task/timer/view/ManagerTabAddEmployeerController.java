package task.timer.view;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import arch.task.timer.model.ManageUsers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import task.timer.model.AbstractEntity;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.RecordFx;
import task.timer.model.User;
import task.timer.model.UserFx;

public class ManagerTabAddEmployeerController {
	ManageEntity MM = new MEFactory().getUserEntityManager();
	
	@FXML private TableView<User> usersTable;
	@FXML private TableColumn<User, String> userNameColumn;
	@FXML private TableColumn<User, String> userLastNameColumn;
	
	@FXML private TextField userName;
	@FXML private TextField userLastName;
	@FXML private TextField userLogin;
	@FXML private TextField userPassword;
	@FXML private ChoiceBox userPermissions;
	
	private String permission;
    
	private final ObservableList<User> dataUsers = 
			FXCollections.observableArrayList();
	
	@FXML private void initialize(){	
		userNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getFirstNameProperty());
		userLastNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getLastNameProperty());		
		usersTable.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> refreshInformationsFromTable(newValue));
		
		readAndShowUsersFromDataBase();		
		refreshInformationsFromTable(null);
		userPermissions.getItems().addAll("administrator", "manager", "pracownik");
		userPermissions.getSelectionModel().selectedIndexProperty()
			.addListener((observable, oldValue, newValue) -> setPermission());
	}
	
	@FXML private void clearFields(){
		usersTable.getSelectionModel().clearSelection();
	}
	
	@FXML private void updateUser() throws ClassNotFoundException{
		
		//aktualna pozycja zaznaczonego usera (ID) w bazie
		System.out.println(usersTable.getSelectionModel().getSelectedItem().getId());
		
		MM.update(
				usersTable.getSelectionModel().getSelectedItem().getId(), 
				new User(
						userLogin.getText(), 
						userPassword.getText(), 
						userName.getText(), 
						userLastName.getText(), 
						permission));

		readAndShowUsersFromDataBase();
	}
	
	@FXML private Integer addUser(){
		Integer userID = MM.add(
				new User(
						userLogin.getText(), 
						userPassword.getText(), 
						userName.getText(), 
						userLastName.getText(), 
						permission));
		readAndShowUsersFromDataBase();
		return userID;
	}
	
	private void readAndShowUsersFromDataBase(){
		List<AbstractEntity> users = MM.list();	
		dataUsers.clear();
		for (int i=0; i<users.size(); i++){
			User userFromDb =   (User) users.get(i);		
			dataUsers.add(userFromDb);
		}		
		usersTable.setItems(dataUsers);
	}
	
	private void setPermission(){
		if (userPermissions.getSelectionModel().getSelectedIndex() == 0) permission = "A";
		else if (userPermissions.getSelectionModel().getSelectedIndex() == 1) permission = "rw";
		else if (userPermissions.getSelectionModel().getSelectedIndex() == 2) permission = "r";
		
	}
	
	private void refreshInformationsFromTable(User usr){
		if (usr != null){
			userName.setText(usr.getFirstName());
			userLastName.setText(usr.getLastName());
			userLogin.setText(usr.getLogin());
			userPassword.setText(usr.getPassword());
			if (usr.getPermissions().equals("A")) userPermissions.getSelectionModel().select(0);
			if (usr.getPermissions().equals("rw")) userPermissions.getSelectionModel().select(1);
			if (usr.getPermissions().equals("r")) userPermissions.getSelectionModel().select(2);
		}
		else {
			userName.setText("");
			userLastName.setText("");
			userLogin.setText("");
			userPassword.setText("");
			userPermissions.getSelectionModel().select(null);
		}
	}

}
