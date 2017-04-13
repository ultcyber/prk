package arch.task.timer.model;

import java.util.Date;

public class History {
	private int historyId;
	private int userIdFromUsers;
	private int projectIdFromProjects;
	private long timeStart;
	private long timeStop;
	private Date data;
	private int workIdFromWorks;
	
	public int getHistoryId() {
		return historyId;
	}
	public void setHistoryId(int historyId) {
		this.historyId = historyId;
	}
	public int getUserIdFromUsers() {
		return userIdFromUsers;
	}
	public void setUserIdFromUsers(int userIdFromUsers) {
		this.userIdFromUsers = userIdFromUsers;
	}
	public int getProjectIdFromProjects() {
		return projectIdFromProjects;
	}
	public void setProjectIdFromProjects(int projectIdFromProjects) {
		this.projectIdFromProjects = projectIdFromProjects;
	}
	public long getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(long timeStart) {
		this.timeStart = timeStart;
	}
	public long getTimeStop() {
		return timeStop;
	}
	public void setTimeStop(long timeStop) {
		this.timeStop = timeStop;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public int getWorkIdFromWorks() {
		return workIdFromWorks;
	}
	public void setWorkIdFromWorks(int workIdFromWorks) {
		this.workIdFromWorks = workIdFromWorks;
	}
	

}
