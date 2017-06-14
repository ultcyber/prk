package task.timer.view;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.Timer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;
import task.timer.Main;
import task.timer.ViewLoader;
import task.timer.helper.AlertDialog;
import task.timer.helper.ChangePasswordDialog;
import task.timer.helper.Helper;
import task.timer.model.Project;
import task.timer.model.Record;
import task.timer.model.User;

/**
 * The Class MainEmployeerController.
 * @author Marcin Zglenicki
 * @since JDK 1.8
 */
public class MainEmployeerController {
	
	/** The logged user name. */
	@FXML private Label loggedUserName;
	
	/** The anchorpane1. */
	@FXML private AnchorPane anchorPane1;
	
	/** The anchorpane2. */
	@FXML private AnchorPane anchorPane2;
	
	/** The rectangle 1. */
	@FXML private Rectangle rectangle1;
	
	/** The rectangle 2. */
	@FXML private Rectangle rectangle2;
	
	/** The choose project box. */
	@FXML private ChoiceBox<Project> chooseProject;
	
	/** Table with records. */
	@FXML private TableView<Record> recordTable;
	
	/** The date column. */
	@FXML private TableColumn<Record, String> dateColumn;
	
	/** The project name column. */
	@FXML private TableColumn<Record, String> projectNameColumn;
	
	/** The description column. */
	@FXML private TableColumn<Record, String> descriptionColumn;
	
	/** The start time column. */
	@FXML private TableColumn<Record, String> startTimeColumn;
	
	/** The stop time column. */
	@FXML private TableColumn<Record, String> stopTimeColumn;
	
	/** The start stop time button. */
	@FXML private Button startStopTime;
	
	/** The measured time - hours label. */
	@FXML private Label measuredTimeHoursLabel;
	
	/** The measured time - minutes label. */
	@FXML private Label measuredTimeMinutesLabel;
	
	/** The measured time - seconds label. */
	@FXML private Label measuredTimeSecondsLabel;

	/** The description name. */
	@FXML private TextField descriptionName;
	
	/** The current date tool. */
	@FXML private DatePicker date;
	
	/** Label with current data. */
	@FXML private Label dataLabel;
	
	/** The delete menu item. */
	@FXML private MenuItem deleteMenuItem;
	
	/** The pattern date. */
	private final String patternDate = "yyyy-MM-dd";
	
	/** The current date. */
	private LocalDate currentDate;
	
	/** The time when user press start. */
	private LocalTime timeStart;
	
	/** The time when user press stop. */
	private LocalTime timeStop;
	
	/** The time - new thread. */
	private Timer time;
	
	/** The passage of time in seconds. */
	private long passageOfTimeSeconds;		
	
	/** The time hour. */
	private long timeHour;								
	
	/** The time minutes. */
	private long timeMinutes;
	
	/** The time seconds. */
	private long timeSeconds;
	
	/** The rest - to count time. */
	private long rest;	

	/** The index of current project. */
	private int indexOfCurrentProject;
	
	/** List of records. */
	private List<Record> records;
	
	/** List of projects. */
	private List<Project> projects;
	
	/** List of records - preparing to show in TableView. */
	private final ObservableList<Record> dataRecords = 
			FXCollections.observableArrayList();
	
	/**
	 * Initialize.
	 */
	@FXML private void initialize(){
		binding();
		
		projects = new ArrayList<Project>();
		
		recordTable.setPlaceholder(new Label("Lista pusta - brak danych"));
		
		loggedUserName.setText(
				LoginWindowController.loggedUser.getFirstName() 
				+ " " 
				+ LoginWindowController.loggedUser.getLastName());

		date.setValue(LocalDate.now());
		currentDate = date.getValue();
		dataLabel.setText(currentDate.toString());
	
		dateColumn.setCellValueFactory(cellData ->
			cellData.getValue().getDateProperty());
		projectNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getProjectName());			
		descriptionColumn.setCellValueFactory(cellData ->
			cellData.getValue().getDescriptionProperty());			
		descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn()); // włącza edytowanie pola	
		startTimeColumn.setCellValueFactory(cellData ->
			cellData.getValue().getTimeStartProperty());	
		if (LoginWindowController.loggedUser.getEditing())
			startTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn()); // włącza edytowanie pola		
		stopTimeColumn.setCellValueFactory(cellData ->
			cellData.getValue().getTimeStopProperty());		
		if (LoginWindowController.loggedUser.getEditing()) 
			stopTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn()); // włącza edytowanie pola

		readProjects();		
		chooseProject.getItems().addAll(projects); 
		
		readRecords();
		showRecords();
		
		if (LoginWindowController.loggedUser.getReminder())
			launchReminder();		
	}
	
	/**
	 * Timing  
	 * prepare fields 
	 * start/stop measured work time
	 * add new record to database
	 * refresh view
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	@FXML private void timing() throws ClassNotFoundException{
		indexOfCurrentProject = chooseProject.getSelectionModel().getSelectedIndex();
		
		// zareaguj gdy jest wybrany projekt
		if (indexOfCurrentProject>-1){	
			if (startStopTime.getText().equals("START")){
				int currentPositionInTableView;
				disableElements();						
				timeStart = LocalTime.now();
				startTimeMeaserement();			
				addRecord();	
				currentPositionInTableView = recordTable.getItems().size();
				readRecords();		
				showRecords();
				recordTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na ostatni wiersz
			}				
			else {
				enableElements();
				timeStop = LocalTime.now().withNano(0);			      				
				stopTimeMeaserement();
				recordTable.getSelectionModel().getSelectedItem().setTimeStop(timeStop);				
				Main.getMMRecord().update(recordTable.getSelectionModel().getSelectedItem());				
				recordTable.refresh();
				descriptionName.clear();
			}
		}
	}

	/**
	 * Read records from database.
	 */
	@FXML private void readRecords(){	
		records = Main.getMMRecord().listRecords(LoginWindowController.loggedUser, null, currentDate);		
		if (records.size() > 0){
			dataRecords.clear();			
			dataRecords.addAll(records);									
		}
	}
	
	/**
	 * Show records in TableView.
	 */
	private void showRecords(){
		recordTable.setItems(dataRecords);
	}
	
	/**
	 * Editing description and update record in database.
	 *
	 * @param descriptionEditEvent the description edit event
	 * @throws ClassNotFoundException the class not found exception
	 */
	// umożliwia edycję pola w TableView i zapis do bazy danych po zatwierdzeniu przez ENTER
	@FXML private void onEditDescription(TableColumn.CellEditEvent<Record, String> descriptionEditEvent) throws ClassNotFoundException{	
		recordTable.getSelectionModel().getSelectedItem().setDescription(descriptionEditEvent.getNewValue());
		Main.getMMRecord().update(recordTable.getSelectionModel().getSelectedItem());		
	}
	
	/**
	 * Edit start time and update record in database.
	 *
	 * @param timeStartEditEvent the time start edit event
	 * @throws ClassNotFoundException the class not found exception
	 */
	// umożliwia edycję startu pracy w TableView i zapis do bazy danych po zatwierdzeniu przez ENTER
	@FXML private void onEditStartTime(TableColumn.CellEditEvent<Record, String> timeStartEditEvent) throws ClassNotFoundException{	
		if (LoginWindowController.loggedUser.getEditing()){
			LocalTime time;
			try{
				time = java.time.LocalTime.parse(timeStartEditEvent.getNewValue());			
				recordTable.getSelectionModel()
				.getSelectedItem()
				.setTimeStart(time);
			Main.getMMRecord().update(recordTable.getSelectionModel().getSelectedItem());
			}
			catch (DateTimeParseException e){
				time = java.time.LocalTime.parse(timeStartEditEvent.getOldValue());
				recordTable.getSelectionModel()
				.getSelectedItem()
				.setTimeStart(time);
				recordTable.refresh();
			}	
		}
	}
	
	/**
	 * Edit stop time and update record in database.
	 *
	 * @param timeStopEditEvent the time stop edit event
	 * @throws ClassNotFoundException the class not found exception
	 */
	// umożliwia edycję konca pracy w TableView i zapis do bazy danych po zatwierdzeniu przez ENTER
	@FXML private void onEditStopTime(TableColumn.CellEditEvent<Record, String> timeStopEditEvent) throws ClassNotFoundException{	
		if (LoginWindowController.loggedUser.getEditing()){
			LocalTime time;
			try{
				time = java.time.LocalTime.parse(timeStopEditEvent.getNewValue());	
				recordTable.getSelectionModel().getSelectedItem().setTimeStop(time);
				Main.getMMRecord().update(recordTable.getSelectionModel().getSelectedItem());
			}
			catch (DateTimeParseException e){
				time = java.time.LocalTime.parse(timeStopEditEvent.getOldValue());
				recordTable.getSelectionModel().getSelectedItem().setTimeStop(time);		
			}
			recordTable.refresh();
		}
	}
	
	/**
	 * Delete record from datebase.
	 *
	 * @throws ClassNotFoundException the class not found exception
	 */
	@FXML void deleteRecord() throws ClassNotFoundException{
		if (recordTable.getSelectionModel().getSelectedIndex() > -1)
		{
			AlertDialog dialog = new AlertDialog("Potwierdź usunięcie wpisu", "Czy na pewno chcesz usunąć wpis?", AlertType.CONFIRMATION);
			if (dialog.getResult() == ButtonType.OK) {
			Main.getMMRecord().delete(recordTable.getSelectionModel().getSelectedItem().getId());
			dataRecords.remove(recordTable.getSelectionModel().getSelectedItem());
			new AlertDialog("Operacja zakończona", "Wpis został usunięty", AlertType.INFORMATION);
			}
		}
	}
	
	/**
	 * Logout.
	 *
	 * @param event the event
	 */
	@FXML private void logout(MouseEvent event){	
		ViewLoader<AnchorPane, Object> viewLoader = new ViewLoader<AnchorPane, Object>("view/LoginWindow.fxml");
		Helper.changeStage(viewLoader, event);
	}
	
	/**
	 * Binding elements on graphic interface.
	 */
	private void binding(){
		rectangle1.widthProperty().bind(anchorPane2.widthProperty());
		rectangle2.widthProperty().bind(anchorPane1.widthProperty());
		rectangle2.heightProperty().bind(anchorPane1.heightProperty().subtract(10));
		
		double width = dateColumn.widthProperty().get();
		width += startTimeColumn.widthProperty().get();
		width += stopTimeColumn.widthProperty().get();

		projectNameColumn.prefWidthProperty().bind(recordTable.widthProperty().subtract(width).divide(2));
		descriptionColumn.prefWidthProperty().bind(recordTable.widthProperty().subtract(width).divide(2));
	}
	
	/**
	 * Adds the record to database.
	 */
	private void addRecord(){
		indexOfCurrentProject = chooseProject.getSelectionModel().getSelectedIndex();
		
		Project currentProject = (Project) projects.get(indexOfCurrentProject);
  
	    Record newRecord = new Record(
	    		LoginWindowController.loggedUser, 
				currentProject, 
				descriptionName.getText(),
				currentDate,
				timeStart,
				null
				);	    
	    Main.getMMRecord().add(newRecord);
	}
	
	/**
	 * Disable elements on graphic interface.
	 */
	private void disableElements(){
		startStopTime.setText("STOP");	
		chooseProject.setDisable(true);
		descriptionName.setDisable(true);
		date.setDisable(true);	
		recordTable.setDisable(true);
		measuredTimeHoursLabel.setText("0");	
		measuredTimeMinutesLabel.setText("0");
		measuredTimeSecondsLabel.setText("0");
	}
	
	/**
	 * Enable elements on graphic interface.
	 */
	private void enableElements(){
		startStopTime.setText("START");
		chooseProject.setDisable(false);
		descriptionName.setDisable(false);
		date.setDisable(false);
		recordTable.setDisable(false);
	}

	/**
	 * Start time measerement in new thread.
	 */
	private void startTimeMeaserement(){
		time = new Timer(1000, e -> {	
			timeStop = LocalTime.now();
			passageOfTimeSeconds = ChronoUnit.SECONDS.between(timeStart, timeStop);		
			timeHour = passageOfTimeSeconds/3600;
			rest = passageOfTimeSeconds%3600;										
			timeMinutes = rest/60;
			timeSeconds = rest%60;	
			Platform.runLater(() -> measuredTimeHoursLabel.setText(timeHour + ""));
			Platform.runLater(() -> measuredTimeMinutesLabel.setText(timeMinutes + ""));
			Platform.runLater(() -> measuredTimeSecondsLabel.setText(timeSeconds + ""));		
		});		
		time.start();
	}

	/**
	 * Stop time measerement - stop thread.
	 */
	private void stopTimeMeaserement(){
		time.stop();
	}

	/**
	 * Read the projects assigned to the logged in user
	 */
	private void readProjects(){		
		Set<Project> projectsFromDataBase = LoginWindowController.loggedUser.getProjects();
		Iterator<Project> wskaznik = projectsFromDataBase.iterator();
		wskaznik.forEachRemaining(project -> projects.add(project));
	}
	
	/**
	 * Launch reminder.
	 * @author Mateusz Trybulec
	 */
	private void launchReminder(){
		
		final int randDelay = new Random().nextInt(60)*60000+30;
				
		new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(randDelay);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					Platform.runLater(new Runnable() {
						public void run() {
							AlertDialog dg;
													
							if (time != null && time.isRunning()) {
								dg = new AlertDialog("Przypomnienie!", "Czy nadal pracujesz nad zadaniem?",
										AlertType.CONFIRMATION);
								if (dg.getResult() != ButtonType.OK) {
									startStopTime.fire();
								}

							} else 
								new AlertDialog("Przypomnienie!",
										"Proszę pamiętać o wciśnięciu START przed rozpoczęciem pracy nad zadaniem",
										AlertType.INFORMATION);
							
						}
					});
				}
			}
		}.start();
		
	}
	
	
	/**
	 * Allows a password change. Opens up a dialog.
	 *
	 * author: Mateusz Trybulec
	 * @return User object
	 * @throws ClassNotFoundException the class not found exception
	 */
	
	@FXML
	private void changePassword() throws ClassNotFoundException{
		User user = (User) LoginWindowController.loggedUser;
		String newPassHash = new ChangePasswordDialog().getPasswordHash();		
		
		if (newPassHash != null){
			user.setPassword(newPassHash);
		}	
		Main.getMMUser().update(user);				
	}


}