package task.timer.view;

import java.util.List;

import task.timer.model.AbstractEntity;
import task.timer.model.MEFactory;
import task.timer.model.ManageEntity;
import task.timer.model.User;

public class LoginWindowController {
	ManageEntity MM = new MEFactory().getUserEntityManager();

	public int processLogin(String username, String password ){
		List<AbstractEntity> users = MM.list();
		for (AbstractEntity user : users){
			User thisUser = (User) user;
			if (thisUser.getLogin() == username && thisUser.getPassword() == password){
				return thisUser.getId();
			}
		}
		return -1;
	}
}
