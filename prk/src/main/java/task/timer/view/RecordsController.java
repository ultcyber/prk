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
	
	private int licznikCzasu;
	private Timer t;
	
	
	@FXML private void timing(){
		if (startStopTime.getText().equals("START")){
			startStopTime.setText("STOP");	
			projectChoice.setDisable(true);
			licznikCzasu=0;
			measuredTime.setText("0:0:0");
		
			t = new Timer(1000, e -> {				
				licznikCzasu++;
				int timeHour = licznikCzasu/3600;
				int reszta = licznikCzasu%3600;										
				int timeMinutes = reszta/60;
				int timeSeconds = reszta%60;
				
				Platform.runLater(() -> measuredTime.setText(timeHour + ":" + timeMinutes + ":" + timeSeconds));
			});
			
			t.start();
		}
				
		else {
			startStopTime.setText("START");
			t.stop();
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