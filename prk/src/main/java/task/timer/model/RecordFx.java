package task.timer.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class RecordToTableView {
	private final SimpleStringProperty projectName;
	private final SimpleStringProperty workName;
	private final SimpleStringProperty timeStartRecord;
	private final SimpleStringProperty timeStopRecord;
	private DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm");
	

	public RecordToTableView(String pName, String wName, String timeStart, String timeStop){
		this.projectName = new SimpleStringProperty(pName);
		this.workName = new SimpleStringProperty(wName); 
		this.timeStartRecord = new SimpleStringProperty(timeStart);
		this.timeStopRecord = new SimpleStringProperty(timeStop);
	}


	public String getProjectName() {
		return projectName.get();
	}

	public void setProjectName(String pName) {
		projectName.set(pName);
	}

	public String getWorkName() {
		return workName.get();
	}

	public void setWorkName(String wName) {
		workName.set(wName);
	}

	public String getTimeStartRecord() {
		return timeStartRecord.get();
	}

	public void setTimeStartRecord(String timeStart) {
		timeStartRecord.set(timeStart);
	}

	public void setTimeStopRecord(String timeStop) {
		timeStopRecord.set(timeStop);
	}
	
	public String getTimeStopRecord() {
		return timeStopRecord.get();
	}
	
	
	
}
