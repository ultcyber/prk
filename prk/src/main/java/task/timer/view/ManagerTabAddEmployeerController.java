package task.timer.view;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import task.timer.Main;
import task.timer.helper.AlertDialog;
import task.timer.helper.Helper;
import task.timer.model.AbstractEntity;
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
	@FXML private Button changeCancelButton;
	@FXML private Button refreshButton;
	
	@FXML private CheckBox editingCheck;
	@FXML private CheckBox reminderCheck;
	
	@FXML private MenuItem deleteMenuItem;
	
	private String permission;
	private boolean newUser;
	private boolean changePassword;
    
	private final ObservableList<User> dataUsers = 
			FXCollections.observableArrayList();
	
	@FXML private void initialize(){	
		usersTable.setPlaceholder(new Label("Lista pusta - brak danych"));
		userNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getFirstNameProperty());
		userLastNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getLastNameProperty());		
		usersTable.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> refreshInformationsFromTableView(newValue));
		
		readAndShowUsersFromDataBase();		
		refreshInformationsFromTableView(null);
		
		userPermissionsBox.getItems().addAll("administrator", "manager", "pracownik");
		userPermissionsBox.getSelectionModel().selectedIndexProperty()
			.addListener((observable, oldValue, newValue) -> setPermission());
	}
	
	@FXML private void readData(){
		clearFields();
		readAndShowUsersFromDataBase();
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
	
	@FXML private void deleteUser() throws ClassNotFoundException{
		if (usersTable.getSelectionModel().getSelectedIndex() >= -1)	// rób jeśli jest zaznaczony pracownik	
		{
			AlertDialog dialog = new AlertDialog("Potwierdź usunięcie loginu", "Czy na pewno chcesz usunąć login?", AlertType.CONFIRMATION);
			if (dialog.getResult() == ButtonType.OK) {
				Main.getMMUser().delete(usersTable.getSelectionModel().getSelectedItem().getId());
				readAndShowUsersFromDataBase();
				clearFields();
				new AlertDialog("Operacja zakończona", "Login został usunięty", AlertType.INFORMATION);
			}
		}
	}
	
	@FXML private void saveUser() throws ClassNotFoundException, NoSuchAlgorithmException{
		if (newUser) addUser();
			else updateUser();
	}
	
	
	@FXML private void setToNewUser(){
		showPassword();
		clearFields();
		hideLackMessages();
		changeCancelButton.setText("Anuluj");
		newUser = true;
	}
	
	@FXML private void passwordCancelButton(){
		if (newUser) cancel();
		else changePassword();
	}
	
	private void cancel(){
		refreshInformationsFromTableView(null);
		hidePassword();
		clearFields();
		changeCancelButton.setText("Zmień hasło");
		newUser = false;
	}
	
	private void changePassword(){
		if (changeCancelButton.getText().equals("Anuluj")){
			hidePassword();
			changeCancelButton.setText("Zmień hasło");
			changePassword = false;
		}	
		else {
			if (usersTable.getSelectionModel().getSelectedIndex() > -1) {
				changePassword = true;
				changeCancelButton.setText("Anuluj");
				userPasswordField.setText("");
				userConfirmPasswordField.setText("");
				lackUserPasswordLabel.setVisible(false);
				lackUserConfirmPasswordLabel.setVisible(false);				
				showPassword();
			}
		}
	}
	
	private void updateUser() throws ClassNotFoundException, NoSuchAlgorithmException{
		if (usersTable.getSelectionModel().getSelectedIndex() > -1) {
			if (changePassword) updateUserWithNewPassword();
			else updateUserWithoutPassword();	
		}
	}
	
	private void updateUserWithNewPassword() throws ClassNotFoundException, NoSuchAlgorithmException{
		int currentPositionInTableView = usersTable.getSelectionModel().getSelectedIndex(); // zapamiętaj bieżące podświetlenie w tabeli
		if ((isAllFieldsAreFull()) 
				&& (isTheSamePassword())
				&& (isLoginUnique(usersTable.getSelectionModel().getSelectedItem().getId()))){	
					currentPositionInTableView = usersTable.getSelectionModel().getSelectedIndex(); // zapamiętaj bieżące podświetlenie w tabeli
					Main.getMMUser().update(				 
							new User(
									usersTable.getSelectionModel().getSelectedItem().getId(),
									userLoginField.getText(), 
									Helper.encryptPassword(userPasswordField.getText()), 
									userNameField.getText(), 
									userLastNameField.getText(), 
									permission,
									editingCheck.isSelected(),
									reminderCheck.isSelected()));
					// pokaż aktualny stan bazy pracowników
					readAndShowUsersFromDataBase();
					usersTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na bieżący wiersz
					new AlertDialog("Operacja zakończona", "Zaktualizowano dane", AlertType.INFORMATION);
					changePassword = false;
		}
	}
	
	private void updateUserWithoutPassword() throws ClassNotFoundException, NoSuchAlgorithmException{
		int currentPositionInTableView = usersTable.getSelectionModel().getSelectedIndex(); // zapamiętaj bieżące podświetlenie w tabeli
		if ((isAllFieldsAreFull()) 
				&& (isLoginUnique(usersTable.getSelectionModel().getSelectedItem().getId()))){	
					Main.getMMUser().update(				 
							new User(
									usersTable.getSelectionModel().getSelectedItem().getId(),
									userLoginField.getText(), 
									usersTable.getSelectionModel().getSelectedItem().getPassword(), 
									userNameField.getText(), 
									userLastNameField.getText(), 
									permission,
									editingCheck.isSelected(),
									reminderCheck.isSelected()));
					// pokaż aktualny stan bazy pracowników
					readAndShowUsersFromDataBase();
					usersTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na bieżący wiersz
					new AlertDialog("Operacja zakończona", "Zaktualizowano dane", AlertType.INFORMATION);
		}
	}

	
	private void addUser() throws NoSuchAlgorithmException{
		int currentPositionInTableView;
		//	jeśli wszystkie pola są wypełnione i jeśli login jest unikalny i hasło jest takie, jak powtórzone hasło; 
		// przekazywana wartość "-1" w metodzie isLoginUnique wskazuje, że wszyscy userzy będą przeszukani
		if (isAllFieldsAreFull() && isTheSamePassword() && isLoginUnique(-1)){		
			Main.getMMUser().add(
				new User(
						userLoginField.getText(), 
						Helper.encryptPassword(userPasswordField.getText()), 
						userNameField.getText(), 
						userLastNameField.getText(), 
						permission,
						editingCheck.isSelected(),
						reminderCheck.isSelected()));
			currentPositionInTableView = usersTable.getItems().size(); // ustaw podświetlenie w tabeli na ostatni wiersz			
			readAndShowUsersFromDataBase(); // pokaż aktualny stan bazy pracowników
			usersTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na ostatni wiersz
			new AlertDialog("Operacja zakończona", "Dodano pracownika", AlertType.INFORMATION);
			newUser = false;
		}	
	}
	
	private void readAndShowUsersFromDataBase(){
		List<AbstractEntity> users = Main.getMMUser().list();	
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
		hideLackMessages();
		newUser = false;
	
		if (usr != null){
			hidePassword();
			changeCancelButton.setText("Zmień hasło");
			userNameField.setText(usr.getFirstName());
			userLastNameField.setText(usr.getLastName());
			userLoginField.setText(usr.getLogin());
			userPasswordField.setText(usr.getPassword());
			if (usr.getPermissions().equals("administrator")) userPermissionsBox.getSelectionModel().select(0);
			if (usr.getPermissions().equals("manager")) userPermissionsBox.getSelectionModel().select(1);
			if (usr.getPermissions().equals("pracownik")) userPermissionsBox.getSelectionModel().select(2);
			editingCheck.setSelected(usr.getEditing());
			reminderCheck.setSelected(usr.getReminder());
		}
		else {
			userNameField.setText("");
			userLastNameField.setText("");
			userLoginField.setText("");
			userPasswordField.setText("");
			userConfirmPasswordField.setText("");
			userPermissionsBox.getSelectionModel().select(null);
			editingCheck.setSelected(false);
			reminderCheck.setSelected(false);
		}
	}
	
	private void hideLackMessages(){
		lackUserNameLabel.setVisible(false);
		lackUserLastNameLabel.setVisible(false);
		lackUserLoginLabel.setVisible(false);
		lackUserPasswordLabel.setVisible(false);
		lackUserConfirmPasswordLabel.setVisible(false);
		lackUserPermissionsLabel.setVisible(false);
	}
	
	public void hidePassword(){
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
		
		if (userPermissionsBox.getSelectionModel().getSelectedIndex() <= -1) {
			lackUserPermissionsLabel.setVisible(true);
			lack = false;
		}
		
		if ((newUser) || (changePassword)){
			if (userPasswordField.getText().equals("")) {
				lackUserPasswordLabel.setVisible(true);
				lack = false;
			}
			if (userConfirmPasswordField.getText().equals("")) {
				lackUserConfirmPasswordLabel.setText("to pole nie może być puste");
				lackUserConfirmPasswordLabel.setVisible(true);
				lack = false;
			}
		}
		

		return lack;
	}
	
	// metoda sprawdza czy login jest unikalny bez uwzględniania bieżacego usera
	private boolean isLoginUnique(int currentUserId){
		List<AbstractEntity> users = Main.getMMUser().list();	
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
			lackUserConfirmPasswordLabel.setText("hasło i powtórzone hasło muszą być takie same");
			lackUserConfirmPasswordLabel.setVisible(true);
			return false;
		}
		return true;
	}

}
