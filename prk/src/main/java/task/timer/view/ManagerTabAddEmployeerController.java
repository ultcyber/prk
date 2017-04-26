package task.timer.view;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import arch.task.timer.model.ManageUsers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
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

	
	
	@FXML private Label lackUserName;
	@FXML private Label lackUserLastName;
	@FXML private Label lackUserLogin;
	@FXML private Label lackUserPassword;
	@FXML private Label lackUserPermissions;
	
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
		
		lackUserName.setVisible(false);
		lackUserLastName.setVisible(false);
		lackUserLogin.setVisible(false);
		lackUserPassword.setVisible(false);
		lackUserPermissions.setVisible(false);
		
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
		int positionInTable;
		
		positionInTable = usersTable.getSelectionModel().getSelectedIndex(); // zapamiętaj bieżące podświetlenie w tabeli
		MM.update(				 
				new User(
						usersTable.getSelectionModel().getSelectedItem().getId(),
						userLogin.getText(), 
						userPassword.getText(), 
						userName.getText(), 
						userLastName.getText(), 
						permission));

		readAndShowUsersFromDataBase();
		usersTable.getSelectionModel().select(positionInTable); // ustaw podswietlenie na bieżący wiersz
	}
	
	@FXML void addUser(){
		if (isAllFieldsAreFull() && (isLoginUnique())){		
			Integer userID = MM.add(
				new User(
						userLogin.getText(), 
						userPassword.getText(), 
						userName.getText(), 
						userLastName.getText(), 
						permission));
		}
		// pokaż aktualny stan bazy pracowników
		readAndShowUsersFromDataBase();
		usersTable.getSelectionModel().select(usersTable.getItems().size()-1); // ustaw podswietlenie na ostatni wiersz
	}
	
	@FXML private void deleteUser() throws ClassNotFoundException{
		if (usersTable.getSelectionModel().getSelectedIndex() >= -1)	// rób jeśli jest zaznaczony pracownik	
		{
			MM.delete(usersTable.getSelectionModel().getSelectedItem().getId());
			readAndShowUsersFromDataBase();
		}
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
	
	private boolean isAllFieldsAreFull(){
		boolean lack = true;
		if (userName.getText().equals("")) {
			lackUserName.setVisible(true);		
			lack = false;
		}
		if (userLastName.getText().equals("")) {
			lackUserLastName.setVisible(true);
			lack = false;
		}
		if (userLogin.getText().equals("")) {
			lackUserLogin.setText("to pole nie może być puste");
			lackUserLogin.setVisible(true);
			lack = false;
		}
		if (userPassword.getText().equals("")) {
			lackUserPassword.setVisible(true);
			lack = false;
		}
		if (userPermissions.getSelectionModel().getSelectedIndex() <= -1) {
			lackUserPermissions.setVisible(true);
			lack = false;
		}
		System.out.println(lack);
		return lack;
	}
	
	private boolean isLoginUnique(){
		List<AbstractEntity> users = MM.list();	
		for (int i=0; i<users.size(); i++){
			User userFromDb =   (User) users.get(i);		
			if (userFromDb.getLogin().equals(userLogin.getText())) {
				lackUserLogin.setText("taki login już istnieje");
				lackUserLogin.setVisible(true);
				System.out.println("false");
				return false;
			}
		}	
		System.out.println("true");
		return true;
	}

}
