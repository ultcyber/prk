package task.timer.view;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import task.timer.Main;
import task.timer.helper.ExcelCreator;
import task.timer.model.AbstractEntity;
import task.timer.model.Project;
import task.timer.model.Record;
import task.timer.model.User;


/**
 * The Class ManagerTabSearchController.
 * @author Marcin Zglenicki
 * @since JDK 1.8
 */
public class ManagerTabSearchController {
	
	/** The search. */
	@FXML private AnchorPane search;
	
	/** The rectangle. */
	@FXML private Rectangle rectangle;
	
	/** The choose user. */
	@FXML private ChoiceBox<AbstractEntity> chooseUser;
	
	/** The choose project. */
	@FXML private ChoiceBox<AbstractEntity> chooseProject;
	
	/** Table with records. */
	@FXML private TableView<Record> recordTable;
	
	/** The user name column. */
	@FXML private TableColumn<Record, String> userNameColumn;
	
	/** The project name column. */
	@FXML private TableColumn<Record, String> projectNameColumn;
	
	/** The description column. */
	@FXML private TableColumn<Record, String> descriptionColumn;
	
	/** The date column. */
	@FXML private TableColumn<Record, String> dateColumn;
	
	/** The start time column. */
	@FXML private TableColumn<Record, String> startTimeColumn;
	
	/** The stop time column. */
	@FXML private TableColumn<Record, String> stopTimeColumn;
	
	/** The total time table. */
	@FXML private TableView<Pair<Long,Long>> totalTimeTable;
	
	/** The hours total column. */
	@FXML private TableColumn<Pair<Long,Long>, String> hoursTotalColumn;
	
	/** The minutes total column. */
	@FXML private TableColumn<Pair<Long,Long>, String> minutesTotalColumn;
	
	/** The date. */
	@FXML private DatePicker date;
	
	/** The date end. */
	@FXML private DatePicker dateEnd;
	
	/** List of users. */
	private List<AbstractEntity> users;
	
	/** List of projects. */
	private List<AbstractEntity> projects;
	
	/** List of records - preparing to show in TableView. */
	private final ObservableList<Record> dataRecords = 
			FXCollections.observableArrayList();
	
	/** The data total time. */
	private ObservableList<Pair<Long,Long>> dataTotalTime = 
			FXCollections.observableArrayList();

	/**
	 * Initialize graphics interface.
	 */
	@FXML private void initialize(){
		binding();	// binding selected items on graphics interface
		
		projects = new ArrayList<AbstractEntity>();
		users = new ArrayList<AbstractEntity>();
				
		recordTable.setPlaceholder(new Label("Lista pusta - brak danych"));
		totalTimeTable.setPlaceholder(new Label("")); // don't want a placeholder;
		
		userNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getUserName());
		
		projectNameColumn.setCellValueFactory(cellData ->
			cellData.getValue().getProjectName());
		
		descriptionColumn.setCellValueFactory(cellData ->
			cellData.getValue().getDescriptionProperty());
		
		dateColumn.setCellValueFactory(cellData ->
			cellData.getValue().getDateProperty());
		
		startTimeColumn.setCellValueFactory(cellData ->
			cellData.getValue().getTimeStartProperty());
		startTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn()); // włącza edytowanie pola
		
		stopTimeColumn.setCellValueFactory(cellData ->
			cellData.getValue().getTimeStopProperty());
		stopTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn()); // włącza edytowanie pola
		
		// Total time columns	
		hoursTotalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().toString()));
		hoursTotalColumn.setSortable(false);
		minutesTotalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().toString()));
		minutesTotalColumn.setSortable(false);
		
		refreshData();
	}
	
	/**
	 * Preparing fields to show informations about users and projects. 
	 * Read users from database.
	 * Read projects from database.
	 * Add users and project to ChoiceBoxes
	 */
	@FXML private void refreshData(){
		clearFields();
		readUsersFromDataBase();
		readProjectsFromDataBase();
		refreshChooseUser();
		refreshChooseProject();
	}
	
	/**
	 * Search and show records.
	 * Calculate total time based on search results.
	 */
	@FXML private void searchAndShowRecords(){
		List<Record> listRecords = Main.getMMRecord().listRecords(			
				chooseUser.getSelectionModel().getSelectedIndex() > 0 ? (User) users.get(chooseUser.getSelectionModel().getSelectedIndex()) : null,
				chooseProject.getSelectionModel().getSelectedIndex() > 0? (Project) projects.get(chooseProject.getSelectionModel().getSelectedIndex()) : null, 
				date.getValue(), dateEnd.getValue());
		
		dataRecords.clear();
			for (int i = 0; i < listRecords.size(); i++) {
				Record recordFromDb = (Record) listRecords.get(i);
				dataRecords.add(recordFromDb);
			}
		
		recordTable.setItems(dataRecords);
		
		dataTotalTime.clear();
		if (listRecords.size() >0){
			dataTotalTime.add(calculateTotalTime(listRecords));
		}
		totalTimeTable.setItems(dataTotalTime);
	}
	
	/**
	 * Export xls with search results.
	 * @author Mateusz Trybulec
	 *
	 * @param event the event
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@FXML private void exportXls(ActionEvent event) throws IOException{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Wybierz folder docelowy i nazwę pliku");
		fileChooser.setInitialFileName("records-export.xls");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		File file = fileChooser.showSaveDialog(stage);
		FileOutputStream fos = null;
		
		byte[] data = new ExcelCreator(dataRecords).getOutputData();
		try {
			fos = new FileOutputStream(file.getAbsolutePath());
			fos.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Binding elements on graphic interface.
	 */
	private void binding(){
		rectangle.widthProperty().bind(search.widthProperty().subtract(10));
		rectangle.heightProperty().bind(search.heightProperty().subtract(10));
		
		double width = dateColumn.widthProperty().get();
		width += startTimeColumn.widthProperty().get();
		width += stopTimeColumn.widthProperty().get();

		userNameColumn.prefWidthProperty().bind(recordTable.widthProperty().subtract(width).divide(3));
		projectNameColumn.prefWidthProperty().bind(recordTable.widthProperty().subtract(width).divide(3));
		descriptionColumn.prefWidthProperty().bind(recordTable.widthProperty().subtract(width).divide(3));
	}
	
	/**
	 * Calculate total time.
	 * author Mateusz Trybulec
	 *
	 * @param list of records
	 * @return the pair - hours and minutes
	 */
	private Pair<Long,Long> calculateTotalTime(List<Record> records){		
		Long hours = 0L;
		Long minutes = 0L;
		Long seconds = 0L;
		
		for (Record r : records){
				LocalTime start = r.getTimeStart();
				if (r.getTimeStop() == null) continue;
				LocalTime stop = r.getTimeStop();
				
				Long diff = SECONDS.between(start,stop);
				// Accounting for day change (the operation is ADD as the number is negative)
				seconds += diff < 0 ? (24*3600)+diff : diff;			
		}
	
		hours += seconds / 3600;
		minutes = (seconds % 3600) / 60;
		
		return new Pair<Long, Long>(hours,minutes);		
	}

	
	/**
	 * Read users from data base.
	 */
	private void readUsersFromDataBase(){
		users = Main.getMMUser().list();	
		users.add(0, new User("", "", "Wszyscy", "", "", false, false));	
	}
	
	/**
	 * Read projects from data base.
	 */
	private void readProjectsFromDataBase(){	
		projects = Main.getMMProject().list();	
		projects.add(0, new Project("Wszystkie"));			
	}
		
		
	/**
	 * Refresh choice box of user.
	 */
	protected void refreshChooseUser(){
		chooseUser.getItems().clear();
		chooseUser.getItems().addAll(users);
		chooseUser.getSelectionModel().select(0);
	}
	
	/**
	 * Refresh choice box of project.
	 */
	protected void refreshChooseProject(){
		chooseProject.getItems().clear();
		chooseProject.getItems().addAll(projects);
		chooseProject.getSelectionModel().select(0);
	}
	
	/**
	 * Clear fields on the graphical interface.
	 */
	protected void clearFields(){
		recordTable.getSelectionModel().clearSelection();
		dataRecords.clear();
		dataTotalTime.clear();
	}
}


