package task.timer.view;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import arch.task.timer.model.ManageUsers;
import arch.task.timer.model.UserFx;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import task.timer.helper.Helper;
import task.timer.model.AbstractEntity;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.RecordFx;
import task.timer.model.User;

public class ManagerTabAddEmployeerController {
	
	@FXML private TableView<User> usersTable;
	@FXML private TableColumn<User, String> userNameColumn;
	@FXML private TableColumn<User, String> userLastNameColumn;
	
	@FXML private TextField userNameField;
	@FXML private TextField userLastNameField;
	@FXML private TextField userLoginField;
	@FXML private TextField userPasswordField;
	@FXML private TextField userConfirmPasswordField;
	@FXML private ChoiceBox<String> userPermissionsBox;
	
	@FXML private Label passwordLabel;
	@FXML private Label confirmPasswordLabel;
	
	@FXML private Label lackUserNameLabel;
	@FXML private Label lackUserLastNameLabel;
	@FXML private Label lackUserLoginLabel;
	@FXML private Label lackUserPasswordLabel;
	@FXML private Label lackUserConfirmPasswordLabel;
	@FXML private Label lackUserPermissionsLabel;
	
	@FXML private Button saveUser;
	@FXML private Button setNewUser;
	
	@FXML private MenuItem deleteMenuItem;
	
	private String permission;
	private boolean newUser;
    
	private final ObservableList<User> dataUsers = 
			FXCollections.observableArrayList();
	
	@FXML private void initialize(){	
		userNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getFirstNameProperty());
		userLastNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getLastNameProperty());		
		usersTable.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> refreshInformationsFromTableView(newValue));
/*				
		lackUserNameLabel.setVisible(false);
		lackUserLastNameLabel.setVisible(false);
		lackUserLoginLabel.setVisible(false);
		lackUserPasswordLabel.setVisible(false);
		lackUserPermissionsLabel.setVisible(false);*/
		
		readAndShowUsersFromDataBase();		
		refreshInformationsFromTableView(null);
		
		userPermissionsBox.getItems().addAll("administrator", "manager", "pracownik");
		userPermissionsBox.getSelectionModel().selectedIndexProperty()
			.addListener((observable, oldValue, newValue) -> setPermission());
	}
	
	@FXML private void readData(){
		clearFields();
		readAndShowUsersFromDataBase();
/*		readAndShowProjectsFromDataBase();			
		showDataOfProject(null);*/
	}
	
	@FXML private void hideLackUserNameLabel(){
		lackUserNameLabel.setVisible(false);
	}
	
	@FXML private void hideLackUserLastNameLabel(){
		lackUserLastNameLabel.setVisible(false);
	}
	
	@FXML private void hideLackUserLoginLabel(){
		lackUserLoginLabel.setVisible(false);
	}
	
	@FXML private void hideLackUserPasswordLabel(){
		lackUserPasswordLabel.setVisible(false);
	}
	
	@FXML private void hideLackUserConfirmPasswordLabel(){
		lackUserConfirmPasswordLabel.setVisible(false);
	}
	
	@FXML private void hideLackUserPermissionsLabel(){
		lackUserPermissionsLabel.setVisible(false);
	}
	
	@FXML public void clearFields(){
		usersTable.getSelectionModel().clearSelection();
		userNameField.clear();;
		userLastNameField.clear();
		userLoginField.clear();
		userPasswordField.clear();
		userConfirmPasswordField.clear();
		userPermissionsBox.getSelectionModel().select(null);
	}
	
	@FXML private void saveUser() throws ClassNotFoundException, NoSuchAlgorithmException{
		if (newUser) addUser();
			else updateUser();
	}
	
	@FXML private void deleteUser() throws ClassNotFoundException{
		if (usersTable.getSelectionModel().getSelectedIndex() >= -1)	// rób jeśli jest zaznaczony pracownik	
		{
			DAO.MMUser.delete(usersTable.getSelectionModel().getSelectedItem().getId());
			readAndShowUsersFromDataBase();
			clearFields();
		}
	}
	
	@FXML private void setToNewUser(){
		showPassword();
		clearFields();
		newUser = true;
	}
	
	private void updateUser() throws ClassNotFoundException, NoSuchAlgorithmException{
		int currentPositionInTableView;
		
		// jeśli wszystkie pola są wypełnione i jeśli login jest unikalny
		// metoda isLoginUnique przeszuka wszystkich userów poza bieżącym
		if ((isAllFieldsAreFull()) && (isLoginUnique(usersTable.getSelectionModel().getSelectedItem().getId()))){	
					currentPositionInTableView = usersTable.getSelectionModel().getSelectedIndex(); // zapamiętaj bieżące podświetlenie w tabeli
					DAO.MMUser.update(				 
							new User(
									usersTable.getSelectionModel().getSelectedItem().getId(),
									userLoginField.getText(), 
									Helper.encryptPassword(userPasswordField.getText()), 
									userNameField.getText(), 
									userLastNameField.getText(), 
									permission));
					// pokaż aktualny stan bazy pracowników
					readAndShowUsersFromDataBase();
					usersTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na bieżący wiersz		
				}
 }

	
	void addUser() throws NoSuchAlgorithmException{
		int currentPositionInTableView;
		//	jeśli wszystkie pola są wypełnione i jeśli login jest unikalny i hasło jest takie, jak powtórzone hasło; 
		// przekazywana wartość "-1" w metodzie isLoginUnique wskazuje, że wszyscy userzy będą przeszukani
		if (isAllFieldsAreFull() && isTheSamePassword() && isLoginUnique(-1)){		
			Integer userID = DAO.MMUser.add(
				new User(
						userLoginField.getText(), 
						Helper.encryptPassword(userPasswordField.getText()), 
						userNameField.getText(), 
						userLastNameField.getText(), 
						permission));
			currentPositionInTableView = usersTable.getItems().size(); // ustaw podświetlenie w tabeli na ostatni wiersz			
			readAndShowUsersFromDataBase(); // pokaż aktualny stan bazy pracowników
			usersTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na ostatni wiersz
		}		
	}
	
	private void readAndShowUsersFromDataBase(){
		List<AbstractEntity> users = DAO.MMUser.list();	
		dataUsers.clear();
		for (int i=0; i<users.size(); i++){
			User userFromDb =   (User) users.get(i);		
			dataUsers.add(userFromDb);
		}		
		usersTable.setItems(dataUsers);
	}
	
	private void setPermission(){
		if (userPermissionsBox.getSelectionModel().getSelectedIndex() == 0) permission = "administrator";
		else if (userPermissionsBox.getSelectionModel().getSelectedIndex() == 1) permission = "manager";
		else if (userPermissionsBox.getSelectionModel().getSelectedIndex() == 2) permission = "pracownik";
		
	}
	
	private void refreshInformationsFromTableView(User usr){
		lackUserNameLabel.setVisible(false);
		lackUserLastNameLabel.setVisible(false);
		lackUserLoginLabel.setVisible(false);
		lackUserPasswordLabel.setVisible(false);
		lackUserConfirmPasswordLabel.setVisible(false);
		lackUserPermissionsLabel.setVisible(false);
	
		if (usr != null){
			hidePassword();
			newUser = false;
			userNameField.setText(usr.getFirstName());
			userLastNameField.setText(usr.getLastName());
			userLoginField.setText(usr.getLogin());
			userPasswordField.setText(usr.getPassword());
			if (usr.getPermissions().equals("administrator")) userPermissionsBox.getSelectionModel().select(0);
			if (usr.getPermissions().equals("manager")) userPermissionsBox.getSelectionModel().select(1);
			if (usr.getPermissions().equals("pracownik")) userPermissionsBox.getSelectionModel().select(2);
		}
		else {
			userNameField.setText("");
			userLastNameField.setText("");
			userLoginField.setText("");
			userPasswordField.setText("");
			userConfirmPasswordField.setText("");
			userPermissionsBox.getSelectionModel().select(null);
		}
	}
	
	private void hidePassword(){
		passwordLabel.setVisible(false);
		confirmPasswordLabel.setVisible(false);
		userPasswordField.setVisible(false);
		userConfirmPasswordField.setVisible(false);
		lackUserPasswordLabel.setVisible(false);
		lackUserConfirmPasswordLabel.setVisible(false);		
	}
	
	private void showPassword(){
		passwordLabel.setVisible(true);
		confirmPasswordLabel.setVisible(true);
		userPasswordField.setVisible(true);
		userConfirmPasswordField.setVisible(true);
		lackUserPasswordLabel.setVisible(true);
		lackUserConfirmPasswordLabel.setVisible(true);	
	}
	
	private boolean isAllFieldsAreFull(){
		boolean lack = true;
		if (userNameField.getText().equals("")) {
			lackUserNameLabel.setVisible(true);				
			lack = false;
		}
		if (userLastNameField.getText().equals("")) {
			lackUserLastNameLabel.setVisible(true);
			lack = false;
		}
		if (userLoginField.getText().equals("")) {
			lackUserLoginLabel.setText("to pole nie może być puste");
			lackUserLoginLabel.setVisible(true);
			lack = false;
		}
		if (userPasswordField.getText().equals("")) {
			lackUserPasswordLabel.setVisible(true);
			lack = false;
		}
		if (userConfirmPasswordField.getText().equals("")) {
			lackUserConfirmPasswordLabel.setVisible(true);
			lack = false;
		}
		if (userPermissionsBox.getSelectionModel().getSelectedIndex() <= -1) {
			lackUserPermissionsLabel.setVisible(true);
			lack = false;
		}
		return lack;
	}
	
	// metoda sprawdza czy login jest unikalny bez uwzględniania bieżacego usera
	private boolean isLoginUnique(int currentUserId){
		List<AbstractEntity> users = DAO.MMUser.list();	
		for (int i=0; i<users.size(); i++){
			User userFromDb =   (User) users.get(i);
			if (currentUserId == userFromDb.getId()) continue;
			if (userFromDb.getLogin().equals(userLoginField.getText())) {
				lackUserLoginLabel.setText("taki login już istnieje");
				lackUserLoginLabel.setVisible(true);
				return false;
			}
		}	
		return true;
	}
	
	private boolean isTheSamePassword(){
		if (!userPasswordField.getText().equals(userConfirmPasswordField.getText())) {
			lackUserConfirmPasswordLabel.setText("Hasło i powtórzone hasło muszą być takie same");
			lackUserConfirmPasswordLabel.setVisible(true);
			return false;
		}
		return true;
	}

}
