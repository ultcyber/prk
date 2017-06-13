/*
 * Copyright (c) 2017 Mateusz Trybulec and Marcin Zglenicki. All rights reserved.
 */
package task.timer.model;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.query.Query;

import javafx.scene.control.Alert.AlertType;
import task.timer.helper.AlertDialog;

/**
 * This class implements a persistent Data Access Objects that 
 * allows for a machine-friendly interaction with the database.<p>
 * 
 * It facilitates standard database table interactions 
 * (add/delete/update/list) as well as adds some utilility functions.<p>
 * All the operations are performed on {@link AbstractEntity} supertype, which
 * allows for generic handling of all database entities.
 * All supported entities extend from an abstract layer {@link AbstractEntity}.<p>
 * 
 * Specific ManageEntities are defined at construction time using
 * EntityType.<p>
 * 
 * A ManageEnity instance will accept an existing SessionFactory or
 * create it's own at construction.<p>
 * 
 * @author Mateusz Trybulec
 * @since JDK 1.8
 * 
 * @param <T>
 *            the generic type of {@link AbstractEntity}
 */
public class ManageEntity<T> implements IEntityManager {

	/** The Hibernate session factory. */
	private SessionFactory factory;

	/** The entity type. */
	private EntityType entityType;

	/**
	 * Instantiates a new Manage Entity.
	 * 
	 * Will create its own SessionFactory using default configuration.
	 *
	 * @param entityType
	 *            the entity type from supported entities.
	 */
	public ManageEntity(EntityType entityType) {
		this.entityType = entityType;

		factory = new FactoryCreator().getFactory();
	}

	/**
	 * Instantiates a new manage entity.
	 * 
	 * Will use the SessionFactory specified in the parameter.
	 * 
	 * @param factory
	 *            the session factory to use.
	 * @param entityType
	 *            the entity type from supported entities.
	 */
	public ManageEntity(SessionFactory factory, EntityType entityType) {
		this.factory = factory;
		this.entityType = entityType;
	}

	/**
	 * Adds a record to the database of EntityType defined at instantiation.
	 * 
	 * Will ping the database before performing the operation.
	 *
	 * @param entity the entity to add to database.
	 * @return unique id of the entity added to the database.
	 */
	@Override
	public Integer add(AbstractEntity entity) {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer entityID = null;
		try {
			if (!ping()) {
				throw new Exception("No connection!");
			}
			tx = session.beginTransaction();
			entityID = (Integer) session.save(entity);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			fail(e);
		} finally {
			session.close();
		}
		return entityID;
	}
	
	/**
	 * Updates a given entity in the database.
	 * 
	 * Will ping the database before performing the operation.
	 *
	 * @param newEntity the updated entity. The id must match the id of the entity to be updated in the database.
	 * @throws ClassNotFoundException if class for given {@link EntityType} cannot be found.
	 */
	@Override
	public void update(AbstractEntity newEntity) throws ClassNotFoundException {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			if (!ping()) {
				throw new Exception("No connection!");
			}
			tx = session.beginTransaction();
			session.update(newEntity);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			fail(e);
		} finally {
			session.close();
		}
	}

	
	/**
	 * Deletes an entity type of given ID from the database.
	 *
	 * Will ping the database before performing the operation.
	 * 
	 * @param id the id of the entity to delete.
	 * @throws ClassNotFoundException if {@link EntityType} class cannot be found.
	 */
	@Override
	public void delete(int id) throws ClassNotFoundException {
		Session session = factory.openSession();
		Transaction tx = null;

		try {
			if (!ping()) {
				throw new Exception("No connection!");
			}
			tx = session.beginTransaction();
			AbstractEntity existingEntity = (AbstractEntity) session.get(Class.forName(entityType.getClassType()), id);
			session.delete(existingEntity);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			fail(e);
		} finally {
			session.close();
		}
	}

	
	/**
	 * Returns all the entities of defined {@link EntityType}.<p>
	 * 
	 * When implemented for specific classes implementing AbstractEntity,
	 * members of the list need to be cast to the target sub-class
	 * 
	 * Will ping the database before performing the operation.
	 *
	 * @return the list of {@link AbstractEntity}.
	 */
	@Override
	public List<AbstractEntity> list() {
		Session session = factory.openSession();
		Transaction tx = null;

		List<AbstractEntity> entities = null;
		try {
			if (!ping()) {
				throw new Exception("No connection!");
			}
			tx = session.beginTransaction();
			entities = session.createQuery("from " + entityType).list();
			tx.commit();
			return entities;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			fail(e);
		} finally {
			session.close();
		}
		return entities;
	}

	/**
	 * Gets the user for a specific login.
	 * 
	 * Will ping the database before performing the operation.
	 *
	 * @param login
	 *            the searched login.
	 * @return the User for specified login or null if User not found.
	 */
	public User getUserForLogin(String login) {

		Session session = factory.openSession();
		
		try {
			if (!ping()) {
				throw new Exception("No connection!");
			}
			String hql = "from User U where U.login = :user_login";
			Query<User> query = session.createQuery(hql);
			query.setParameter("user_login", login);
			List<User> results = query.list();

			if (results.size() == 1) {
				return (User) results.get(0);
			}
		} catch (Exception e) {
			fail(e);
		} finally {
			session.close();
		}

		return null;

	}

	/**
	 * List records for date range.
	 *
	 * Will ping the database before performing the operation.
	 *
	 * @param dateStart
	 *            the search start date
	 * @param dateEnd
	 *            the search end date
	 * @return the list of records.
	 */
	public List<Record> listRecordsForDateRange(String dateStart, String dateEnd) {
		Session session = factory.openSession();
		List<Record> results = null;
		
		try {
			if (!ping()) {
				throw new Exception("No connection!");
			}
			String hql = "from Records R where R.date between :date_start and :date_end";
			Query<Record> query = session.createQuery(hql);
			query.setParameter("date_start", dateStart);
			query.setParameter("date_end", dateEnd);
			results = query.list();
		} catch (Exception e) {
			fail(e);
		} finally {
			session.close();
		}
		return results;

	}

	/**
	 * Lists records for a given user and/or project and/or specific date.<p>
	 * 
	 * When parameter passed as null, it is treated as optional, 
	 * i.e. all are returned. Example: a null for a user 
	 * will return records for all the users.<p>
	 * 
	 * Will ping the database before performing the operation.
	 *
	 * @param user
	 *            the user
	 * @param project
	 *            the project
	 * @param date
	 *            the date
	 * @return the list of records.
	 */
	public List<Record> listRecords(User user, Project project, LocalDate date) {
		Session session = factory.openSession();
		List<Record> entities = null;

		// Creating a string and removing all null values
		String[] data = new String[3];

		data[0] = user != null ? "R.user = " + String.valueOf(user.getId()) : null;
		data[1] = project != null ? "R.project = " + String.valueOf(project.getId()) : null;
		data[2] = date != null ? "R.date = " + String.format("'%s'", date) : null;

		data = Arrays.stream(data).filter(x -> x != null && x != "null").toArray(String[]::new);

		try {
			if (!ping()) {
				throw new Exception("No connection!");
			}
			String sql;
			if (data.length > 0) {
				sql = "from Record R where " + String.join(" and ", data);
			} else {
				sql = "from Record";
			}
			Query<Record> query = session.createQuery(sql, Record.class);
			entities = query.list();

		} catch (Exception e) {
			fail(e);
			e.printStackTrace();
		} finally {
			session.close();
		}
		return entities;
	}

	/**
	 * Lists records for a given user and/or project and/or specific date range.<p>
	 * 
	 * When parameter passed as null, it is treated as optional, 
	 * i.e. all are returned. Example: a null for a user 
	 * will return records for all the users.<p>
	 * 
	 * Will ping the database before performing the operation.
	 *
	 * @param user
	 *            the user
	 * @param project
	 *            the project
	 * @param dateStart
	 *            the search start date.
	 * @param dateEnd
	 *            the search end date.
	 *       
	 * @return the list of records.
	 */
	public List<Record> listRecords(User user, Project project, LocalDate dateStart, LocalDate dateEnd) {
		if (dateEnd == null) {
			return listRecords(user, project, dateStart);
		}

		Session session = factory.openSession();
		List<Record> entities = null;

		// Creating a string and removing all null values
		String[] data = new String[4];

		data[0] = user != null ? "R.user = " + String.valueOf(user.getId()) : null;
		data[1] = project != null ? "R.project = " + String.valueOf(project.getId()) : null;
		data[2] = dateStart != null ? "R.date between " + String.format("'%s'", dateStart) : null;
		if (data[2] != null)
			data[3] = dateEnd != null ? String.format("'%s'", dateEnd) : null;
		else
			data[3] = dateEnd != null ? String.format("R.date <= '%s'", dateEnd) : null;

		data = Arrays.stream(data).filter(x -> x != null && x != "null").toArray(String[]::new);

		try {
			if (!ping()) {
				throw new Exception("No connection!");
			}
			String sql;
			if (data.length > 0) {
				sql = "from Record R where " + String.join(" and ", data);
			} else {
				sql = "from Record";
			}
			Query<Record> query = session.createQuery(sql, Record.class);
			entities = query.list();

			return entities;

		} catch (Exception e) {
			fail(e);
			e.printStackTrace();
		} finally {
			session.close();
		}
		return entities;
	}

	/**
	 * Ping the database using the ping url from the SessionFactory configuration file.
	 *
	 * @return the result of the ping operation
	 */
	public boolean ping() {
		SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) factory;
		Map<String, Object> props = sessionFactoryImpl.getProperties();
		String urlAddr = props.get("database.test.url").toString();
		try {
			URL url = new URL(urlAddr);
			URLConnection connection = url.openConnection();
			if (connection.getContentLength() == -1) {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}


	/**
	 * A standard fail handler for the class.
	 * 
	 * Shows a error window to the user with stackTrace of passed Throwable.
	 *
	 * @param e the Throwable of which stackTrace shall be printed.
	 */
	@Override
	public void fail(Throwable e) {
		new AlertDialog("Error", "Błąd połączenia z bazą danych.",
				"Wystąpił błąd z połączeniem z bazą danych. Spróbuj ponownie uruchomić aplikację. \n\nW razie częstego występowania błędu, skontaktuj się z administratorem podając poniższe szczegóły błędu.",
				AlertType.ERROR, e);
	}

}
