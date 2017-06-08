package task.timer.view;

import java.util.LinkedList;
import java.util.List;

import javax.swing.text.DateFormatter;

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

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.SECONDS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;



public class ManagerTabSearchController {
//	ManageEntity MMProject = new MEFactory().getProjectEntityManager();

	
	@FXML private ChoiceBox chooseUser;
	@FXML private ChoiceBox chooseProject;
	
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
	
	private List<String> listUsersNames;
	private List<String> listProjectsNames;
	private List<AbstractEntity> users;
	private List<AbstractEntity> projects;
	
	private final ObservableList<Record> dataRecords = 
			FXCollections.observableArrayList();

	@FXML private void initialize(){	
		listProjectsNames = new LinkedList<String>();
		listUsersNames = new LinkedList<String>();
		
		chooseUser.getItems().addAll(readUsersFromDataBase());
		chooseProject.getItems().addAll(readProjectsFromDataBase());
		
		chooseUser.getSelectionModel().select(0);
		chooseProject.getSelectionModel().select(0);
				
		recordTable.setPlaceholder(new Label("Dla wybranych kryteriów - brak danych do wyświetlenia"));
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
				chooseUser.getSelectionModel().getSelectedIndex() > 0 ? (User) users.get(chooseUser.getSelectionModel().getSelectedIndex()-1) : null,
				chooseProject.getSelectionModel().getSelectedIndex() > 0? (Project) projects.get(chooseProject.getSelectionModel().getSelectedIndex()-1) : null, 
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
	
	private List<String> readUsersFromDataBase(){
		listUsersNames.clear();		
		users = Main.getMMUser().list();	
		listUsersNames.add("Wszyscy");
		for (int i=0; i<users.size(); i++){
			User userFromDb =   (User) users.get(i);		
			listUsersNames.add(userFromDb.getFirstName()+ " " + userFromDb.getLastName());
		}		
		return listUsersNames;
	}
	
	private List<String> readProjectsFromDataBase(){
		listProjectsNames.clear();		
		projects = Main.getMMProject().list();	
		listProjectsNames.add("Wszystkie");
		for (int i=0; i<projects.size(); i++){
			Project projectFromDb =   (Project) projects.get(i);		
			listProjectsNames.add(projectFromDb.getName());
		}		
		return listProjectsNames;
	}
	
	public void refreshChooseUser(){
		chooseUser.getItems().clear();
		chooseUser.getItems().addAll(listUsersNames);
		chooseUser.getSelectionModel().select(0);
	}
	
	public void refreshChooseProject(){
		chooseProject.getItems().clear();
		chooseProject.getItems().addAll(listProjectsNames);
		chooseProject.getSelectionModel().select(0);
	}
	
	public Pair<Long,Long> calculateTotalTime(List<Record> records){		
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
	
	public void clearFields(){
		recordTable.getSelectionModel().clearSelection();
		dataRecords.clear();
		dataTotalTime.clear();
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
	
}
