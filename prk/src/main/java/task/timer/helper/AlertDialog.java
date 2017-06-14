package task.timer.helper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Helper class for launching different types of Alert Windows.
 * @author Mateusz Trybulec
 */
public class AlertDialog {
	
	/** The result. */
	private Optional<ButtonType> result;
	
		
	/**
	 * Instantiates a new alert dialog.
	 *
	 * @param headerText the header text
	 * @param contextText the context text
	 * @param alertType the alert type
	 */
	public AlertDialog(String headerText, String contextText, AlertType alertType) {
		
		Alert alert = new Alert(alertType);
		alert.setContentText(contextText);
		alert.setTitle("");
		alert.setHeaderText(headerText);
		alert.setContentText(contextText);
		alert.setResizable(true);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		if (alertType == AlertType.CONFIRMATION){
			Button btnOk = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
			Button btnCancel = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
			btnOk.setText("TAK");
			btnCancel.setText("NIE");
			
			setResult(alert.showAndWait());			
		}
		else {
			alert.showAndWait();
		}
	}
	
	/**
	 * Instantiates a new alert dialog.
	 *
	 * @param title the title
	 * @param headerText the header text
	 * @param contextText the context text
	 * @param alertType the alert type
	 */
	public AlertDialog(String title, String headerText, String contextText, AlertType alertType) {
		Alert alert = new Alert(alertType);
		
		alert.setContentText(contextText);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contextText);
		alert.setResizable(true);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		if (alertType == AlertType.CONFIRMATION){
			Button btnOk = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
			Button btnCancel = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
			btnOk.setText("TAK");
			btnCancel.setText("NIE");
			
			setResult(alert.showAndWait());			
		}
		else {
			alert.showAndWait();
		}
	}
	
	
	/**
	 * Instantiates a new alert dialog with special Exception box.
	 *
	 * @param title the title
	 * @param headerText the header text
	 * @param contextText the context text
	 * @param alertType the alert type
	 * @param ex the exception
	 */
	public AlertDialog(String title, String headerText, String contextText, AlertType alertType, Throwable ex) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contextText);
		alert.setResizable(true);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("Szczegóły błędu:\n");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
		
	}
	
	/**
	 * Instantiates a new alert dialog with special Exception box.
	 *
	 * @param headerText the header text
	 * @param contextText the context text
	 * @param alertType the alert type
	 * @param ex the exception
	 */
	public AlertDialog(String headerText, String contextText, AlertType alertType, Throwable ex) {
		Alert alert = new Alert(alertType);
		alert.setTitle("");
		alert.setHeaderText(headerText);
		alert.setContentText(contextText);
		alert.setResizable(true);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("Szczegóły błędu:\n");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
		
	}
	

	/**
	 * Gets the result - the button that was clicked. This will work only for <code>{@link AlertType}.CONFIRMATION</code> AlertDialogs.
	 *
	 * @return the result
	 */
	public ButtonType getResult() {
		return result.get();
	}

	/**
	 * Sets the result.
	 *
	 * @param result the new result
	 */
	public void setResult(Optional<ButtonType> result) {
		this.result = result;
	}
	
}
