package task.timer.view;

import arch.task.timer.model.History;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class HistoryController {
	@FXML private TableView<History> historyTable;
	@FXML private TableColumn<History, Integer> userIdColumn;
	@FXML private TableColumn<History, String> projectNameColumn;
	@FXML private TableColumn<History, String> workNameColumn;
	@FXML private TableColumn<History, Long> startTimeColumn;
	@FXML private TableColumn<History, Long> stopTimeColumn;
	
}



