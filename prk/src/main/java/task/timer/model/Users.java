package task.timer.model;

public class Users {
	private int userId;
	private String login;
	private String firstName;
	private String lastName;
	private String password;
	private char permissions;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public char getPermissions() {
		return permissions;
	}
	public void setPermissions(char permissions) {
		this.permissions = permissions;
	}
	
	
}
