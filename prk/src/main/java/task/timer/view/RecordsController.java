package task.timer.view;

import javafx.fxml.FXML;
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
	

@FXML private void initialize(){
	recordTable.setTableMenuButtonVisible(true);
	
	userIdColumn.setCellValueFactory(cellData -> 
			cellData.getValue().recordIdProperty().asObject());
	
	projectNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().projectNameProperty());
	

	

}

}