package task.timer.model;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.util.config.ConfigurationException;

import javafx.scene.control.Alert.AlertType;
import task.timer.Main;
import task.timer.helper.AlertDialog;

/**
 * FactoryCreator class. Helps creating a SessionFactory object.
 * @author Mateusz Trybulec
 */
public class FactoryCreator {
	   
   	/** The factory. */
   	private SessionFactory factory;
	   
	   /**
   	 * Instantiates a new factory creator. Takes from default location of hibernate.cfg.xml.
   	 *  For jar packages, looks at the jar directory.
   	 *  
   	 *  If failed, displays an appropriate error dialog to the user.
   	 */
   	public FactoryCreator(){

		try {
			CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
			File jarFile = new File(codeSource.getLocation().toURI().getPath());
			String jarDir = jarFile.getParentFile().getPath();
			System.out.println(jarDir+"/hibernate.cfg.xml");

			factory = new Configuration().configure(new File(jarDir+"/hibernate.cfg.xml")).buildSessionFactory();
		} catch (ConfigurationException | URISyntaxException e) {
			try {
				factory = new Configuration().configure().buildSessionFactory();

			} catch (Throwable ex) {
				System.err.println("Failed to create sessionFactory object." + ex);
				ex.printStackTrace();
				new AlertDialog("Error", "Błąd połączenia z bazą danych.",
						"Wystąpił błąd z połączeniem z bazą danych. Spróbuj ponownie uruchomić aplikację. \n\nW razie częstego występowania błędu, skontaktuj się z administratorem podając poniższe szczegóły błędu.",
						AlertType.ERROR, ex);
				throw new ExceptionInInitializerError(ex);
			}
		}
	}
	   
	   /**
   	 * Gets the factory.
   	 *
   	 * @return current session factory
   	 */
	   public SessionFactory getFactory() {
	   	return factory;
	   }


	   
}
