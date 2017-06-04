package task.timer.view;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.Timer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import task.timer.model.AbstractEntity;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.Project;
import task.timer.model.Record;
import task.timer.model.User;

public class MainEmployeerController {
	@FXML private Label loggedUserName;
	
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
	@FXML private ChoiceBox projectChoice;
	@FXML private TextField descriptionName;
	@FXML private DatePicker date;
	@FXML private Label dataLabel;
	
	private final String patternDate = "yyyy-MM-dd";
	private final String patternTime = "HH:mm:ss";
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
	Record newRecord;
	
	ObservableList<Project> listOfProjects = FXCollections.observableArrayList();
	ObservableList<String> listOfProjectsName = FXCollections.observableArrayList();
	
	private final ObservableList<Record> dataRecords = 
			FXCollections.observableArrayList();
	
	@FXML private void initialize(){
		loggedUserName.setText(
				LoginWindowController.loggedUser.getFirstName() 
				+ " " 
				+ LoginWindowController.loggedUser.getLastName());
	
	
		setDatePicker();
		
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
		stopTimeColumn.setCellValueFactory(cellData ->
			cellData.getValue().getTimeStopProperty());		

		readProjectsFromDataBase();
		projectChoice.setItems(listOfProjectsName);
    
		readAndShowRecordsFromDataBase();
	}
	
	@FXML private void timing() throws ClassNotFoundException{
		indexOfCurrentProject = projectChoice.getSelectionModel().getSelectedIndex();
		
		// zareaguj gdy jest wybrany projekt
		if (indexOfCurrentProject>-1){	
			if (startStopTime.getText().equals("START")){
				int currentPositionInTableView;
				disableElements();						
				timeStart = LocalTime.now();
				startTimeMeaserement();			
				addNewRecordToDataBase();	
				
				currentPositionInTableView = recordTable.getItems().size();
				readAndShowRecordsFromDataBase();		
				recordTable.getSelectionModel().select(currentPositionInTableView); // ustaw podswietlenie na ostatni wiersz
			}				
			else {
				enableElements();
				timeStop = LocalTime.now().withNano(0);
			      				
				stopTimeMeaserement();
				recordTable.getSelectionModel().getSelectedItem().setTimeStop(timeStop);				
				DAO.MMRecord.update(recordTable.getSelectionModel().getSelectedItem());
				
				recordTable.refresh();
				descriptionName.clear();
			}
		}
	}


	@FXML private void readAndShowRecordsFromDataBase(){
	
		List<Record> records = DAO.MMRecord.listRecords(LoginWindowController.loggedUser, null, currentDate);	
		
		if (records.size() > 0){
			dataRecords.clear();			
			dataRecords.addAll(records);					
			recordTable.setItems(dataRecords);	
		}
	}
	
	// umożliwia edycję pola w TableView i zapis do bazy danych po zatwierdzeniu przez ENTER
	@FXML private void onEditDescription(TableColumn.CellEditEvent<Record, String> descriptionEditEvent) throws ClassNotFoundException{	
		recordTable.getSelectionModel().getSelectedItem().setDescription(descriptionEditEvent.getNewValue());
		DAO.MMRecord.update(recordTable.getSelectionModel().getSelectedItem());		
	}

	private void disableElements(){
		startStopTime.setText("STOP");	
		projectChoice.setDisable(true);
		descriptionName.setDisable(true);
		date.setDisable(true);	
		recordTable.setDisable(true);
		measuredTimeHoursLabel.setText("0");	
		measuredTimeMinutesLabel.setText("0");
		measuredTimeSecondsLabel.setText("0");
	}
	
	private void enableElements(){
		startStopTime.setText("START");
		projectChoice.setDisable(false);
		descriptionName.setDisable(false);
		date.setDisable(false);
		recordTable.setDisable(false);
	}
	
	private void addNewRecordToDataBase(){
		indexOfCurrentProject = projectChoice.getSelectionModel().getSelectedIndex();
		
		Project currentProject = listOfProjects.get(indexOfCurrentProject);
  
	    Record newRecord = new Record(
	    		LoginWindowController.loggedUser, 
				currentProject, 
				descriptionName.getText(),
				currentDate,
				timeStart,
				timeStart
				);	    
	    Integer recordID = DAO.MMRecord.add(newRecord);
	}
	
/*	private void updateRecordToDataBase(Record recordToUpdate) throws ClassNotFoundException{
		DAO.MMRecord.update(recordToUpdate);
		//readAndShowRecordsFromDataBase();
	}*/

	private void setDatePicker(){
		date.setValue(LocalDate.now());
		StringConverter converter = new StringConverter<LocalDate>() {
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
		Set<Project> projects = LoginWindowController.loggedUser.getProjects();
		Project project;
				
		if (!projects.isEmpty()){		
			Iterator<Project> wskaznik = projects.iterator();					
			for (Project i : projects)
				if (wskaznik.hasNext()) {
					project = wskaznik.next();
					listOfProjects.add(project);
					listOfProjectsName.add(project.getName());
				}
		}
	}
	

}