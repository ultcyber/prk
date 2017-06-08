package task.timer.model;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javafx.scene.control.Alert.AlertType;
import task.timer.helper.AlertDialog;

// TODO: Auto-generated Javadoc
/**
 * The Class FactoryCreator.
 */
public class FactoryCreator {
	   
   	/** The factory. */
   	private SessionFactory factory;
	   
	   /**
   	 * Instantiates a new factory creator.
   	 */
   	public FactoryCreator(){
		   
		   try {
				factory=new Configuration().configure().buildSessionFactory();
			}
			catch(Throwable ex) {
				System.err.println("Failed to create sessionFactory object." + ex);
				ex.printStackTrace();
				new AlertDialog("Error", "Błąd połączenia z bazą danych.", "Wystąpił błąd z połączeniem z bazą danych. Spróbuj ponownie uruchomić aplikację. \n\nW razie częstego występowania błędu, skontaktuj się z administratorem podając poniższe szczegóły błędu.", AlertType.ERROR, ex);
				throw new ExceptionInInitializerError(ex);
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
