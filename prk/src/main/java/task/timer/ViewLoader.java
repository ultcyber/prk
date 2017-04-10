package task.timer;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

public class ViewLoader<T,U> {
	private T ViewLoader = null;
	private U fxmlController = null;
	public ViewLoader(String fxml){
		try{
			FXMLLoader fxmlLoader = new
					FXMLLoader(Main.class.getResource(fxml));
			
			ViewLoader = fxmlLoader.load();
			fxmlController = fxmlLoader.getController();
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	public T getLayout(){ return ViewLoader;}
	public U getController() { return fxmlController;}
}