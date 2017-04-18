package task.timer.view;

import java.util.Calendar;

import javax.swing.Timer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import task.timer.model.Record;
import task.timer.model.RecordToTableView;

public class RecordsController {
	@FXML private TableView<RecordToTableView> recordTable;
	@FXML private TableColumn<RecordToTableView, Integer> userIdColumn;
	@FXML private TableColumn<RecordToTableView, String> projectNameColumn;
	@FXML private TableColumn<RecordToTableView, String> workNameColumn;
	@FXML private TableColumn<RecordToTableView, Long> startTimeColumn;
	@FXML private TableColumn<RecordToTableView, Long> stopTimeColumn;
	
	@FXML private Button startStopTime;
	@FXML private Label measuredTime;
	@FXML private ChoiceBox projectChoice;
	
	private int timeCounter;
	private Timer time;
	
	private final ObservableList<RecordToTableView> dataRecords = 
			FXCollections.observableArrayList();
	
		
	@FXML private void timing(){
		if (startStopTime.getText().equals("START")){
			startStopTime.setText("STOP");	
			projectChoice.setDisable(true);
			timeCounter=0;
			measuredTime.setText("0:0:0");
			
			startTimeMeaserement(measuredTime);		
		}				
		else {
			startStopTime.setText("START");
			stopTimeMeaserement();
			projectChoice.setDisable(false);
			

		}
	}

@FXML private void initialize(){
	measuredTime.setText("0:0:0");
	
	projectNameColumn.setCellValueFactory(
			new PropertyValueFactory<>("projectName"));
	
	workNameColumn.setCellValueFactory(
			new PropertyValueFactory<>("workName"));
	
	startTimeColumn.setCellValueFactory(
			new PropertyValueFactory<>("timeStartRecord"));
	
	stopTimeColumn.setCellValueFactory(
			new PropertyValueFactory<>("timeStopRecord"));
	
	readProjectsFromDataBase();
	
	readRecordsFromDataBase();
	
	recordTable.setItems(dataRecords);

}

	private void startTimeMeaserement(Label measuredTimeLabel){
		time = new Timer(1000, e -> {				
			timeCounter++;
			int timeHour = timeCounter/3600;
			int rest = timeCounter%3600;										
			int timeMinutes = rest/60;
			int timeSeconds = rest%60;			
			Platform.runLater(() -> measuredTime.setText(timeHour + ":" + timeMinutes + ":" + timeSeconds));
		});		
		time.start();
	}

	private void stopTimeMeaserement(){
		time.stop();
	}

	private void readProjectsFromDataBase(){
		
	}
	
	private void readRecordsFromDataBase(){
		dataRecords.add(new RecordToTableView("aa1", "bb1", "cc1", "stop1"));
		dataRecords.add(new RecordToTableView("aa2", "bb2", "cc2", "stop2"));
		dataRecords.add(new RecordToTableView("trzeci projekt", "trzecie zadanie", "czas", "stop3"));
		dataRecords.add(new RecordToTableView("czwarty", "czwarte", "późno", "stop4"));
	}

}