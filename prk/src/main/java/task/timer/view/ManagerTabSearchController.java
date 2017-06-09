package task.timer.view;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import task.timer.Main;
import task.timer.helper.ExcelCreator;
import task.timer.model.AbstractEntity;
import task.timer.model.Project;
import task.timer.model.Record;
import task.timer.model.User;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalTime;



public class ManagerTabSearchController {
	
	@FXML private ChoiceBox<AbstractEntity> chooseUser;
	@FXML private ChoiceBox<AbstractEntity> chooseProject;
	
	@FXML private TableView<Record> recordTable;
	@FXML private TableColumn<Record, String> userNameColumn;
	@FXML private TableColumn<Record, String> projectNameColumn;
	@FXML private TableColumn<Record, String> descriptionColumn;
	@FXML private TableColumn<Record, String> dateColumn;
	@FXML private TableColumn<Record, String> startTimeColumn;
	@FXML private TableColumn<Record, String> stopTimeColumn;
	
	@FXML private TableView<Pair<Long,Long>> totalTimeTable;
	@FXML private TableColumn<Pair<Long,Long>, String> hoursTotalColumn;
	@FXML private TableColumn<Pair<Long,Long>, String> minutesTotalColumn;
	
	private ObservableList<Pair<Long,Long>> dataTotalTime = 
			FXCollections.observableArrayList();
	
	@FXML private DatePicker date;
	@FXML private DatePicker dateEnd;
	
	private List<AbstractEntity> users;
	private List<AbstractEntity> projects;
	
	private final ObservableList<Record> dataRecords = 
			FXCollections.observableArrayList();

	@FXML private void initialize(){
		projects = new ArrayList<AbstractEntity>();
		users = new ArrayList<AbstractEntity>();
		readUsersFromDataBase();
		chooseUser.getItems().addAll(users);
		
		readProjectsFromDataBase();
		chooseProject.getItems().addAll(projects);
		
		chooseUser.getSelectionModel().select(0);
		chooseProject.getSelectionModel().select(0);
				
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
		
	}
	
	@FXML private void readData(){
		clearFields();
		readUsersFromDataBase();
		readProjectsFromDataBase();
		refreshChooseUser();
		refreshChooseProject();
	}
	
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
	
	@FXML public void exportXls(ActionEvent event) throws IOException{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Wybierz folder docelowy i nazwę pliku");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		File file = fileChooser.showSaveDialog(stage);
		
		byte[] data = new ExcelCreator(dataRecords).getOutputData();
		FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
		try {
			fos.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		fos.close();
	}
	
	private void readUsersFromDataBase(){
		users = Main.getMMUser().list();	
		users.add(0, new User("", "", "Wszyscy", "", "", false, false));	
	}
	
	private List<AbstractEntity> readProjectsFromDataBase(){	
		projects = Main.getMMProject().list();	
		projects.add(0, new Project("Wszystkie"));			
		return projects;
	}
		
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
		
	protected void refreshChooseUser(){
		chooseUser.getItems().clear();
		chooseUser.getItems().addAll(users);
		chooseUser.getSelectionModel().select(0);
	}
	
	protected void refreshChooseProject(){
		chooseProject.getItems().clear();
		chooseProject.getItems().addAll(projects);
		chooseProject.getSelectionModel().select(0);
	}
	
	protected void clearFields(){
		recordTable.getSelectionModel().clearSelection();
		dataRecords.clear();
		dataTotalTime.clear();
	}
}
