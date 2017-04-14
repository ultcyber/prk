package task.timer.view;



import java.util.Calendar;

import javax.swing.Timer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import task.timer.model.Record;

public class RecordsController {
	@FXML private TableView<Record> recordTable;
	@FXML private TableColumn<Record, Integer> userIdColumn;
	@FXML private TableColumn<Record, String> projectNameColumn;
	@FXML private TableColumn<Record, String> workNameColumn;
	@FXML private TableColumn<Record, Long> startTimeColumn;
	@FXML private TableColumn<Record, Long> stopTimeColumn;
	
	@FXML private Button startStopTime;
	@FXML private Label measuredTime;
	@FXML private ChoiceBox projectChoice;
	
	private int timeCounter;
	private Timer time;
	
	
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
	
	//recordTable.setTableMenuButtonVisible(true);
	
	/*	userIdColumn.setCellValueFactory(cellData -> 
			cellData.getValue().recordIdProperty().asObject());
	
	projectNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().projectNameProperty());*/
}

}