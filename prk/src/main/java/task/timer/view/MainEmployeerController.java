package task.timer.view;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

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
import javafx.util.StringConverter;
import task.timer.model.AbstractEntity;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.Project;
import task.timer.model.Record;
import task.timer.model.User;

public class MainEmployeerController {
	ManageEntity MMProject = new MEFactory().getProjectEntityManager();
	ManageEntity MMRecord = new MEFactory().getRecordEntityManager();
	ManageEntity MMUser = new MEFactory().getUserEntityManager();
	
	@FXML private TableView<Record> recordTable;
	@FXML private TableColumn<Record, String> dateColumn;
	@FXML private TableColumn<Record, String> projectNameColumn;
	@FXML private TableColumn<Record, String> workNameColumn;
	@FXML private TableColumn<Record, String> startTimeColumn;
	@FXML private TableColumn<Record, String> stopTimeColumn;
	
	@FXML private Button startStopTime;
	@FXML private Label measuredTimeHoursLabel;
	@FXML private Label measuredTimeMinutesLabel;
	@FXML private Label measuredTimeSecondsLabel;
	@FXML private ChoiceBox projectChoice;
	@FXML private TextField taskName;
	@FXML private DatePicker date;
	
	private final String pattern = "yyyy-MM-dd";
	private LocalDate currentDate;
	private LocalTime timeStart;
	private LocalTime timeStop;
	private Timer time;
	private long passageOfTimeSeconds;		
	private long timeHour;								
	private long timeMinutes;
	private long timeSeconds;
	private long rest;	
	List<AbstractEntity> projects;
	List<AbstractEntity> users;
	private int indexOfCurrentProject;
	User loggedUser;
	
	private final ObservableList<Record> dataRecords = 
			FXCollections.observableArrayList();
	private final ObservableList<Project> dataProjects =
			FXCollections.observableArrayList();
	
		
	@FXML private void timing() throws ClassNotFoundException{
		if (startStopTime.getText().equals("START")){
			setVisualAfterStartButton();						
			timeStart = LocalTime.now();
			startTimeMeaserement();			
			addNewRecordToDataBase();	
			readAndShowRecordsFromDataBase();		
		}				
		else {
			startStopTime.setText("START");
			stopTimeMeaserement();
			projectChoice.setDisable(false);
			taskName.setDisable(false);
			date.setDisable(false);
		}
	}

@FXML private void initialize(){
	loggedUser = setLoggedUser();
	
	setDatePicker();
	currentDate = date.getValue();	
	
	dateColumn.setCellValueFactory(cellData ->
		cellData.getValue().getDateProperty());

	projectNameColumn.setCellValueFactory(cellData ->
		cellData.getValue().getProjectName());
		
	//workNameColumn.setCellValueFactory(cellData ->
	//	cellData.getValue().get);
	
	startTimeColumn.setCellValueFactory(cellData ->
		cellData.getValue().getTimeStartProperty());
	
	stopTimeColumn.setCellValueFactory(
			new PropertyValueFactory<>("timeStopRecord"));
	
	
    projectChoice.setItems(readProjectsFromDataBase());

    
    readAndShowRecordsFromDataBase();
	//recordTable.setItems(dataRecords);

}
	private void setVisualAfterStartButton(){
		startStopTime.setText("STOP");	
		projectChoice.setDisable(true);
		taskName.setDisable(true);
		date.setDisable(true);		
		measuredTimeHoursLabel.setText("0");	
		measuredTimeMinutesLabel.setText("0");
		measuredTimeSecondsLabel.setText("0");
	}
	
	private void addNewRecordToDataBase(){
		indexOfCurrentProject = projectChoice.getSelectionModel().getSelectedIndex();	    
	    Project currentProject = (Project)projects.get(indexOfCurrentProject);	    
	    Integer recordID = MMRecord.add(
	    		new Record(
	    				loggedUser, 
	    				currentProject, 
	    				currentDate,
	    				timeStart,
	    				timeStart
	    				));
	}

	private User setLoggedUser(){
		users = MMUser.list();	 
		return (User) users.get(1);
	}

	private void setDatePicker(){
		date.setValue(LocalDate.now());
		StringConverter converter = new StringConverter<LocalDate>() {
	        DateTimeFormatter dateFormatter = 
	            DateTimeFormatter.ofPattern(pattern);
	        
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
	    date.setPromptText(pattern.toLowerCase());
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

	private ObservableList<String> readProjectsFromDataBase(){
		ObservableList<String> listProjectsName = FXCollections.observableArrayList();	
		projects = MMProject.list();	
		for (int i=0; i<projects.size(); i++){
			Project projectFromDb =   (Project) projects.get(i);		
			listProjectsName.add(projectFromDb.getName());
		}		
		return listProjectsName;
	}
	
	private void readAndShowRecordsFromDataBase(){
		List<AbstractEntity> records; 	
		records = MMRecord.list();			
		dataRecords.clear();
		for (int i=0; i<records.size(); i++){				
			Record recordFromDb =   (Record) records.get(i);	
			dataRecords.add(recordFromDb);					
		}		
		recordTable.setItems(dataRecords);	
	}

}