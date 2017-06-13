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

public class MainEmployeerController {
	@FXML private Label loggedUserName;
	@FXML private AnchorPane anchorPane1;
	@FXML private AnchorPane anchorPane2;
	@FXML private Rectangle rectangle1;
	@FXML private Rectangle rectangle2;
	
	List<Record> records;
	@FXML private ChoiceBox<Project> chooseProject;
	
	@FXML private TableView<Record> recordTable;
	@FXML private TableColumn<Record, String> dateColumn;
	@FXML private TableColumn<Record, String> projectNameColumn;
	@FXML private TableColumn<Record, String> descriptionColumn;
	@FXML private TableColumn<Record, String> startTimeColumn;
	@FXML private TableColumn<Record, String> stopTimeColumn;
	
	@FXML private Button startStopTime;
	@FXML private Label measuredTimeHoursLabel;
	@FXML private Label measuredTimeMinutesLabel;
	@FXML private Label measuredTimeSecondsLabel;

	@FXML private TextField descriptionName;
	@FXML private DatePicker date;
	@FXML private Label dataLabel;
	
	@FXML private MenuItem deleteMenuItem;
	
	private final String patternDate = "yyyy-MM-dd";
	private LocalDate currentDate;
	
	private LocalTime timeStart;
	private LocalTime timeStop;
	private Timer time;
	private long passageOfTimeSeconds;		
	private long timeHour;								
	private long timeMinutes;
	private long timeSeconds;
	private long rest;	

	private int indexOfCurrentProject;
	
	private List<Project> projects;
	
	private final ObservableList<Record> dataRecords = 
			FXCollections.observableArrayList();
	
	@FXML private void initialize(){
		rectangle1.widthProperty().bind(anchorPane2.widthProperty());
		rectangle2.widthProperty().bind(anchorPane1.widthProperty());
		rectangle2.heightProperty().bind(anchorPane1.heightProperty().subtract(10));
		
		double width = dateColumn.widthProperty().get();
		width += startTimeColumn.widthProperty().get();
		width += stopTimeColumn.widthProperty().get();

		projectNameColumn.prefWidthProperty().bind(recordTable.widthProperty().subtract(width).divide(2));
		descriptionColumn.prefWidthProperty().bind(recordTable.widthProperty().subtract(width).divide(2));
		
		recordTable.setPlaceholder(new Label("Lista pusta - brak danych"));
		
		loggedUserName.setText(
				LoginWindowController.loggedUser.getFirstName() 
				+ " " 
				+ LoginWindowController.loggedUser.getLastName());
		
		setDatePicker();
		projects = new ArrayList<Project>();
		
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

		readProjectsFromDataBase();
		
		chooseProject.getItems().addAll(projects);    
		readAndShowRecordsFromDataBase();
		
		if (LoginWindowController.loggedUser.getReminder()){
			launchReminder();
		}
		
	}
	
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
				readAndShowRecordsFromDataBase();		
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

	@FXML private void readAndShowRecordsFromDataBase(){	
		records = Main.getMMRecord().listRecords(LoginWindowController.loggedUser, null, currentDate);		
		if (records.size() > 0){
			dataRecords.clear();			
			dataRecords.addAll(records);					
			recordTable.setItems(dataRecords);	
		}
	}
	
	// umożliwia edycję pola w TableView i zapis do bazy danych po zatwierdzeniu przez ENTER
	@FXML private void onEditDescription(TableColumn.CellEditEvent<Record, String> descriptionEditEvent) throws ClassNotFoundException{	
		recordTable.getSelectionModel().getSelectedItem().setDescription(descriptionEditEvent.getNewValue());
		Main.getMMRecord().update(recordTable.getSelectionModel().getSelectedItem());		
	}
	
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
	
	@FXML private void logout(MouseEvent event){	
		ViewLoader<AnchorPane, Object> viewLoader = new ViewLoader<AnchorPane, Object>("view/LoginWindow.fxml");
		Helper.changeStage(viewLoader, event);
	}
	
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
	
	private void enableElements(){
		startStopTime.setText("START");
		chooseProject.setDisable(false);
		descriptionName.setDisable(false);
		date.setDisable(false);
		recordTable.setDisable(false);
	}

	private void setDatePicker(){
		date.setValue(LocalDate.now());
		StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
	        DateTimeFormatter dateFormatter = 
	            DateTimeFormatter.ofPattern(patternDate);
	        
	        @Override
	        public String toString(LocalDate date) {
	            if (date != null) 
	            	return dateFormatter.format(date);
	            else 
	                return "";          
	        }
	        
	        @Override
	        public LocalDate fromString(String string) {
	            if (string != null && !string.isEmpty()) 
	                return LocalDate.parse(string, dateFormatter);
	            else 
	                return null;          
	        }
	    };             
	    date.setConverter(converter);
	    date.setPromptText(patternDate.toLowerCase());
	}

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

	private void stopTimeMeaserement(){
		time.stop();
	}

	private void readProjectsFromDataBase(){		
		Set<Project> projectsFromDataBase = LoginWindowController.loggedUser.getProjects();
		Iterator<Project> wskaznik = projectsFromDataBase.iterator();
		wskaznik.forEachRemaining(project -> projects.add(project));
	}
	
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
	 * @param login name
	 * @return User object
	 * @throws ClassNotFoundException 
	 * @throws NoSuchAlgorithmException the no such algorithm exception for encryption handler.
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