package task.timer.view;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
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
import task.timer.helper.Helper;
import task.timer.model.AbstractEntity;
import task.timer.model.User;

/**
 * The Class ManagerTabAddEmployeerController.
 * @author Marcin Zglenicki
 * @since JDK 1.8
 */
public class ManagerTabAddEmployeerController {
	
	/** The rectangle 1. */
	@FXML private Rectangle rectangle1;
	
	/** The rectangle 2. */
	@FXML private Rectangle rectangle2;
	
	/** The hbox. */
	@FXML private HBox hbox;
	
	/** The employeer. */
	@FXML private AnchorPane employeer;
	
	/** Table with users. */
	@FXML private TableView<User> usersTable;
	
	/** The user name column. */
	@FXML private TableColumn<User, String> userNameColumn;
	
	/** The user last name column. */
	@FXML private TableColumn<User, String> userLastNameColumn;
	
	/** The user name field. */
	@FXML private TextField userNameField;
	
	/** The user last name field. */
	@FXML private TextField userLastNameField;
	
	/** The user login field. */
	@FXML private TextField userLoginField;
	
	/** The user password field. */
	@FXML private TextField userPasswordField;
	
	/** The user confirm password field. */
	@FXML private TextField userConfirmPasswordField;
	
	/** The user permissions box. */
	@FXML private ChoiceBox<String> userPermissionsBox;
	
	/** The password label. */
	@FXML private Label passwordLabel;
	
	/** The confirm password label. */
	@FXML private Label confirmPasswordLabel;
	
	/** The lack user name label. */
	@FXML private Label lackUserNameLabel;
	
	/** The lack user last name label. */
	@FXML private Label lackUserLastNameLabel;
	
	/** The lack user login label. */
	@FXML private Label lackUserLoginLabel;
	
	/** The lack user password label. */
	@FXML private Label lackUserPasswordLabel;
	
	/** The lack user confirm password label. */
	@FXML private Label lackUserConfirmPasswordLabel;
	
	/** The lack user permissions label. */
	@FXML private Label lackUserPermissionsLabel;
	
	/** The save user. */
	@FXML private Button saveUser;
	
	/** The set new user button. */
	@FXML private Button setNewUser;
	
	/** The change cancel button. */
	@FXML private Button changeCancelButton;
	
	/** The refresh button. */
	@FXML private Button refreshButton;
	
	/** The editing check. */
	@FXML private CheckBox editingCheck;
	
	/** The reminder check. */
	@FXML private CheckBox reminderCheck;
	
	/** The delete menu item. */
	@FXML private MenuItem deleteMenuItem;
	
	/** The permission. */
	private String permission;
	
	/** The new user. */
	private boolean newUser;
	
	/** The change password. */
	private boolean changePassword;
	
	/** List of users. */
	private List<AbstractEntity> users;
    
	/** List of users - preparing to show in TableView. */
	private final ObservableList<User> dataUsers = 
			FXCollections.observableArrayList();
	
	/**
	 * Initialize graphics interface.
	 */
	@FXML private void initialize(){	
		binding();// binding selected items on graphics interface
		
		usersTable.setPlaceholder(new Label("Lista pusta - brak danych"));
		userNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getFirstNameProperty());
		userLastNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getLastNameProperty());		
		usersTable.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> refreshInformationsFromTableView(newValue));

		userPermissionsBox.getItems().addAll("manager", "pracownik");
		userPermissionsBox.getSelectionModel().selectedIndexProperty()
			.addListener((observable, oldValue, newValue) -> setPermission());
		
		userPermissionsBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> changedChoiceBox());
		
		refreshData();
	}
	
	/**
	 * Preparing fields to show informations about user. 
	 * Read users from database.
	 * Show users in TableView.
	 * Show details about current user
	 */
	@FXML private void refreshData(){
		clearFields();
		readUsers();
		showUsers();
		refreshInformationsFromTableView(null);
	}
	
	/**
	 * Hide lack user name label.
	 */
	@FXML private void hideLackUserNameLabel(){
		lackUserNameLabel.setVisible(false);
	}
	
	/**
	 * Hide lack user last name label.
	 */
	@FXML private void hideLackUserLastNameLabel(){
		lackUserLastNameLabel.setVisible(false);
	}
	
	/**
	 * Hide lack user login label.
	 */
	@FXML private void hideLackUserLoginLabel(){
		lackUserLoginLabel.setVisible(false);
	}
	
	/**
	 * Hide lack user password label.
	 */
	@FXML private void hideLackUserPasswordLabel(){
		lackUserPasswordLabel.setVisible(false);
	}
	
	/**
	 * Hide lack user confirm password label.
	 */
	@FXML private void hideLackUserConfirmPasswordLabel(){
		lackUserConfirmPasswordLabel.setVisible(false);
	}
		
	/**
	 * Delete user.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	@FXML private void deleteUser() throws ClassNotFoundException{
		if (usersTable.getSelectionModel().getSelectedIndex() >= -1) // rób jeśli jest zaznaczony pracownik i masz uprawnienia do jego usuniecia
			
		{
			AlertDialog dialog = new AlertDialog("Potwierdź usunięcie loginu", "Czy na pewno chcesz usunąć login?", AlertType.CONFIRMATION);
			if (dialog.getResult() == ButtonType.OK) {
				Main.getMMUser().delete(usersTable.getSelectionModel().getSelectedItem().getId());
				readUsers();
				showUsers();
				clearFields();
				new AlertDialog("Operacja zakończona", "Login został usunięty", AlertType.INFORMATION);
			}
		}
	}
	
	/**
	 * Save user.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	@FXML private void saveUser() throws ClassNotFoundException, NoSuchAlgorithmException{
		if (newUser) addUser();
			else updateUser();
	}
		
	/**
	 * Preparing data to create new user
	 */
	@FXML private void setToNewUser(){
		showPassword();
		clearFields();
		hideLackMessages();
		changeCancelButton.setText("Anuluj");
		newUser = true;
	}
	
	/**
	 * Change password/cancel button.
	 */
	@FXML private void passwordCancelButton(){
		if (newUser) cancel();
		else changePassword();
	}
	
	/**
	 * Binding elements on graphic interface.
	 */
	private void binding(){
		rectangle1.widthProperty().bind(employeer.widthProperty().subtract(10));
		rectangle1.heightProperty().bind(employeer.heightProperty().subtract(10));
		
		rectangle2.widthProperty().bind(hbox.widthProperty());
		
		userNameColumn.prefWidthProperty().bind(usersTable.widthProperty().divide(2));
		userLastNameColumn.prefWidthProperty().bind(usersTable.widthProperty().divide(2));
	}
	
	
	/**
	 * When userPermissionsBox is changed then:
	 *  - hide Label: lack permissions
	 *  - hide/show CheckBoxes: editingCheck and reminderCheck
	 */
	private void changedChoiceBox(){
		hideLackUserPermissionsLabel();
		hideShowCheckBoxes();
	}
	
	/**
	 * Hide lack user permissions label.
	 */
	private void hideLackUserPermissionsLabel(){
		lackUserPermissionsLabel.setVisible(false);
	}
	
	private void hideShowCheckBoxes(){
		if (userPermissionsBox.getSelectionModel().getSelectedIndex() == 1){
			editingCheck.setVisible(true);
			reminderCheck.setVisible(true);
		}
		else {
			editingCheck.setVisible(false);
			reminderCheck.setVisible(false);
		}			
	}
	
	/**
	 * Cancel - canceling create new user.
	 */
	private void cancel(){
		refreshInformationsFromTableView(null);
		hidePassword();
		clearFields();
		changeCancelButton.setText("Zmień hasło");
		newUser = false;
	}
	
	/**
	 * Cancel password change or prepare to change password.
	 */
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
	
	/**
	 * Update user.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	private void updateUser() throws ClassNotFoundException, NoSuchAlgorithmException{
		if (usersTable.getSelectionModel().getSelectedIndex() > -1) {
			if (changePassword) updateUserWithNewPassword();
			else updateUserWithoutPassword();	
		}
	}
	
	/**
	 * Update user with new password.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
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
					readUsers();
					showUsers();
					usersTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na bieżący wiersz
					new AlertDialog("Operacja zakończona", "Zaktualizowano dane", AlertType.INFORMATION);
					changePassword = false;
		}
	}
	
	/**
	 * Update user without password.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
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
					readUsers();
					showUsers();
					usersTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na bieżący wiersz
					new AlertDialog("Operacja zakończona", "Zaktualizowano dane", AlertType.INFORMATION);
		}
	}

	
	/**
	 * Adds the user.
	 *
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	private void addUser() throws NoSuchAlgorithmException{
		int currentPositionInTableView;
		if (isAllFieldsAreFull() 
				&& isTheSamePassword() 
				&& isLoginUnique(-1)){		
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
			readUsers();
			showUsers();
			usersTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na ostatni wiersz
			new AlertDialog("Operacja zakończona", "Dodano pracownika", AlertType.INFORMATION);
			newUser = false;
		}	
	}
	
	/**
	 * Read users.
	 */
	private void readUsers(){
		users = Main.getMMUser().list();	
		dataUsers.clear();
		users.forEach(userFromDb -> dataUsers.add((User)userFromDb)); 
	}
	
	/**
	 * Show users.
	 */
	private void showUsers(){
		usersTable.setItems(dataUsers);
	}
	
	/**
	 * Set user permissions
	 */
	private void setPermission(){
		if (userPermissionsBox.getSelectionModel().getSelectedIndex() == 0) permission = "manager";
		else if (userPermissionsBox.getSelectionModel().getSelectedIndex() == 1) permission = "pracownik";
		
	}
	
	/**
	 * Refresh informations from table view.
	 *
	 * @param usr - current user
	 */
	private void refreshInformationsFromTableView(User usr){
		hideLackMessages();
		newUser = false;
		
		if (usr != null){
			setPrivilagesToModifyUser(usr);
			hidePassword();
			changeCancelButton.setText("Zmień hasło");
			userNameField.setText(usr.getFirstName());
			userLastNameField.setText(usr.getLastName());
			userLoginField.setText(usr.getLogin());
			userPasswordField.setText(usr.getPassword());
			
			if (usr.getPermissions().equals("manager")) userPermissionsBox.getSelectionModel().select(0);
			
			if (usr.getPermissions().equals("pracownik")) userPermissionsBox.getSelectionModel().select(1);
			
			hideShowCheckBoxes();
			
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
	
	/**
	 * Set permissions for the logged in user
	 *
	 * @param usr - current (selected) user
	 */
	private void setPrivilagesToModifyUser(User user){
		setFields(false);
		if (!LoginWindowController.loggedUser.getPermissions().equals("administrator"))
		{
			if (LoginWindowController.loggedUser.getId() != user.getId()){
				if ((user.getPermissions().equals("manager")) 
						|| (user.getPermissions().equals("administrator"))){
					setFields(true);
				}
				if (user.getPermissions().equals("pracownik")){
					setFields(false);
				}
			}
			
			else {
				setFields(false);
				deleteMenuItem.setDisable(true);
			}
		}
		else {
			if (LoginWindowController.loggedUser.getId() == user.getId()){
				deleteMenuItem.setDisable(true);
				userPermissionsBox.setDisable(true);
			}
		}
	}
	
	/**
	 * Set fields as enabled or disabled
	 *
	 * @param disabled - sets fields
	 */
	private void setFields(boolean disabled){
		userNameField.setDisable(disabled);
		userLastNameField.setDisable(disabled);
		userLoginField.setDisable(disabled);
		userPermissionsBox.setDisable(disabled);
		editingCheck.setDisable(disabled);
		reminderCheck.setDisable(disabled);
		changeCancelButton.setDisable(disabled);
		saveUser.setDisable(disabled);
		deleteMenuItem.setDisable(disabled);
	}
	
	/**
	 * Hide lack messages.
	 */
	protected void hideLackMessages(){
		lackUserNameLabel.setVisible(false);
		lackUserLastNameLabel.setVisible(false);
		lackUserLoginLabel.setVisible(false);
		lackUserPasswordLabel.setVisible(false);
		lackUserConfirmPasswordLabel.setVisible(false);
		lackUserPermissionsLabel.setVisible(false);
	}
	
	/**
	 * Hide fields about password.
	 */
	protected void hidePassword(){
		passwordLabel.setVisible(false);
		confirmPasswordLabel.setVisible(false);
		userPasswordField.setVisible(false);
		userConfirmPasswordField.setVisible(false);
		lackUserPasswordLabel.setVisible(false);
		lackUserConfirmPasswordLabel.setVisible(false);		
	}
	
	/**
	 * Show fields about password.
	 */
	private void showPassword(){
		passwordLabel.setVisible(true);
		confirmPasswordLabel.setVisible(true);
		userPasswordField.setVisible(true);
		userConfirmPasswordField.setVisible(true);
	}
	
	/**
	 * Checks if is all fields are full.
	 *
	 * @return true, if is all fields are full
	 */
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
	
	/**
	 * Checks if is login unique.
	 *
	 * @param currentUserId the current user id
	 * @return true, if is login unique
	 */
	private boolean isLoginUnique(int currentUserId){
		User user = Main.getMMUser().getUserForLogin(userLoginField.getText());
		if (user == null) return true;
		else {
			if (user.getId() == currentUserId) return true;
		}	
		lackUserLoginLabel.setText("taki login istnieje w bazie");
		lackUserLoginLabel.setVisible(true);
		return false;
	}
	
	/**
	 * Checks if is the same password.
	 *
	 * @return true, if is the same password
	 */
	private boolean isTheSamePassword(){
		if (!userPasswordField.getText().equals(userConfirmPasswordField.getText())) {
			lackUserConfirmPasswordLabel.setText("hasło i powtórzone hasło muszą być takie same");
			lackUserConfirmPasswordLabel.setVisible(true);
			return false;
		}
		return true;
	}
	
	/**
	 * Clear fields.
	 */
	protected void clearFields(){
		usersTable.getSelectionModel().clearSelection();
		userNameField.clear();
		userLastNameField.clear();
		userLoginField.clear();
		userPasswordField.clear();
		userConfirmPasswordField.clear();
		userPermissionsBox.getSelectionModel().select(null);
	}
	
	/**
	 * Reset setting.
	 */
	protected void resetSetting(){
		newUser = false;;
		changePassword = false;
		changeCancelButton.setText("Zmień hasło");
	}

}
