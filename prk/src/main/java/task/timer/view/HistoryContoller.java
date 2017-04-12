package task.timer.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import task.timer.model.History;

public class HistoryContoller {
	@FXML private TableView<History> historyTable;
	@FXML private TableColumn<History, Integer> userIdColumn;
	@FXML private TableColumn<History, String> projectNameColumn;
	@FXML private TableColumn<History, String> workNameColumn;
	@FXML private TableColumn<History, Long> startTimeColumn;
	@FXML private TableColumn<History, Long> stopTimeColumn;
	
}



