package task.timer.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javafx.beans.property.SimpleStringProperty;

/**
 * The Class RecordFx.
 * @author Marcin Zglenicki
 */
public class RecordFx {
	
	/** The project name. */
	private final SimpleStringProperty projectName;
	
	/** The work name. */
	private final SimpleStringProperty workName;
	
	/** The time start record. */
	private final SimpleStringProperty timeStartRecord;
	
	/** The time stop record. */
	private final SimpleStringProperty timeStopRecord;
	
	/** The df. */
	private DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm");
	

	/**
	 * Instantiates a new record fx.
	 *
	 * @param pName the name
	 * @param wName the w name
	 * @param timeStart the time start
	 * @param timeStop the time stop
	 */
	public RecordFx(String pName, String wName, String timeStart, String timeStop){
		this.projectName = new SimpleStringProperty(pName);
		this.workName = new SimpleStringProperty(wName); 
		this.timeStartRecord = new SimpleStringProperty(timeStart);
		this.timeStopRecord = new SimpleStringProperty(timeStop);
	}


	/**
	 * Gets the project name.
	 *
	 * @return the project name
	 */
	public String getProjectName() {
		return projectName.get();
	}

	/**
	 * Sets the project name.
	 *
	 * @param pName the new project name
	 */
	public void setProjectName(String pName) {
		projectName.set(pName);
	}

	/**
	 * Gets the work name.
	 *
	 * @return the work name
	 */
	public String getWorkName() {
		return workName.get();
	}

	/**
	 * Sets the work name.
	 *
	 * @param wName the new work name
	 */
	public void setWorkName(String wName) {
		workName.set(wName);
	}

	/**
	 * Gets the time start record.
	 *
	 * @return the time start record
	 */
	public String getTimeStartRecord() {
		return timeStartRecord.get();
	}

	/**
	 * Sets the time start record.
	 *
	 * @param timeStart the new time start record
	 */
	public void setTimeStartRecord(String timeStart) {
		timeStartRecord.set(timeStart);
	}

	/**
	 * Sets the time stop record.
	 *
	 * @param timeStop the new time stop record
	 */
	public void setTimeStopRecord(String timeStop) {
		timeStopRecord.set(timeStop);
	}
	
	/**
	 * Gets the time stop record.
	 *
	 * @return the time stop record
	 */
	public String getTimeStopRecord() {
		return timeStopRecord.get();
	}
	
	
	
}
