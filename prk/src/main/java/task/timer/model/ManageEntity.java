package task.timer.model;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.query.Query;

import javafx.scene.control.Alert.AlertType;
import task.timer.helper.AlertDialog;

/**
 * The Class ManageEntity.
 *
 * @param <T>
 *            the generic type
 */
public class ManageEntity<T> implements IEntityManager {

	/** The factory. */
	private SessionFactory factory;

	/** The entity type. */
	private EntityType entityType;

	/**
	 * Instantiates a new manage entity.
	 *
	 * @param entityType
	 *            the entity type
	 */
	public ManageEntity(EntityType entityType) {
		this.entityType = entityType;

		factory = new FactoryCreator().getFactory();
	}

	/**
	 * Instantiates a new manage entity.
	 *
	 * @param factory
	 *            the factory
	 */
	public ManageEntity(SessionFactory factory) {
		this.factory = factory;
	}

	/**
	 * Instantiates a new manage entity.
	 *
	 * @param factory
	 *            the factory
	 * @param entityType
	 *            the entity type
	 */
	public ManageEntity(SessionFactory factory, EntityType entityType) {
		this.factory = factory;
		this.entityType = entityType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see task.timer.model.IEntityManager#add(task.timer.model.AbstractEntity)
	 */
	@Override
	public Integer add(AbstractEntity entity) {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer entityID = null;
		try {
			if (!ping()) {
				throw new HibernateException("No connection!");
			}
			tx = session.beginTransaction();
			entityID = (Integer) session.save(entity);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			fail(e);
		} finally {
			session.close();
		}
		return entityID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * task.timer.model.IEntityManager#update(task.timer.model.AbstractEntity)
	 */
	@Override
	public void update(AbstractEntity newEntity) throws ClassNotFoundException {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			if (!ping()) {
				throw new HibernateException("No connection!");
			}
			tx = session.beginTransaction();
			session.update(newEntity);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			fail(e);
		} finally {
			session.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see task.timer.model.IEntityManager#delete(int)
	 */
	@Override
	public void delete(int id) throws ClassNotFoundException {
		Session session = factory.openSession();
		Transaction tx = null;

		try {
			if (!ping()) {
				throw new HibernateException("No connection!");
			}
			tx = session.beginTransaction();
			AbstractEntity existingEntity = (AbstractEntity) session.get(Class.forName(entityType.getClassType()), id);
			session.delete(existingEntity);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			fail(e);
		} finally {
			session.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see task.timer.model.IEntityManager#list()
	 */
	@Override
	public List<AbstractEntity> list() {
		Session session = factory.openSession();
		Transaction tx = null;

		List<AbstractEntity> entities = null;
		try {
			if (!ping()) {
				throw new HibernateException("No connection!");
			}
			tx = session.beginTransaction();
			entities = session.createQuery("from " + entityType).list();
			tx.commit();
			return entities;
		} catch (HibernateException e) {
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
	 * Gets the user for login.
	 *
	 * @param login
	 *            the login
	 * @return the user for login
	 */
	public User getUserForLogin(String login) {

		Session session = factory.openSession();
		
		try {
			if (!ping()) {
				throw new HibernateException("No connection!");
			}
			String hql = "from User U where U.login = :user_login";
			Query<User> query = session.createQuery(hql);
			query.setParameter("user_login", login);
			List<User> results = query.list();

			if (results.size() == 1) {
				return (User) results.get(0);
			}
		} catch (HibernateException e) {
			fail(e);
		} finally {
			session.close();
		}

		return null;

	}

	/**
	 * List records for date range.
	 *
	 * @param dateStart
	 *            the date start
	 * @param dateEnd
	 *            the date end
	 * @return the list
	 */
	public List<Record> listRecordsForDateRange(String dateStart, String dateEnd) {
		Session session = factory.openSession();
		List<Record> results = null;
		
		try {
			if (!ping()) {
				throw new HibernateException("No connection!");
			}
			String hql = "from Records R where R.date between :date_start and :date_end";
			Query<Record> query = session.createQuery(hql);
			query.setParameter("date_start", dateStart);
			query.setParameter("date_end", dateEnd);
			results = query.list();
		} catch (HibernateException e) {
			fail(e);
		} finally {
			session.close();
		}
		return results;

	}

	/**
	 * List records.
	 *
	 * @param user
	 *            the user
	 * @param project
	 *            the project
	 * @param date
	 *            the date
	 * @return the list
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
				throw new HibernateException("No connection!");
			}
			String sql;
			if (data.length > 0) {
				sql = "from Record R where " + String.join(" and ", data);
			} else {
				sql = "from Record";
			}
			Query<Record> query = session.createQuery(sql, Record.class);
			entities = query.list();

		} catch (HibernateException e) {
			fail(e);
			e.printStackTrace();
		} finally {
			session.close();
		}
		return entities;
	}

	/**
	 * List records.
	 *
	 * @param user
	 *            the user
	 * @param project
	 *            the project
	 * @param dateStart
	 *            the date start
	 * @param dateEnd
	 *            the date end
	 * @return the list
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
				throw new HibernateException("No connection!");
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

		} catch (HibernateException e) {
			fail(e);
			e.printStackTrace();
		} finally {
			session.close();
		}
		return entities;
	}

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

	@Override
	public void fail(Throwable e) {
		new AlertDialog("Error", "Błąd połączenia z bazą danych.",
				"Wystąpił błąd z połączeniem z bazą danych. Spróbuj ponownie uruchomić aplikację. \n\nW razie częstego występowania błędu, skontaktuj się z administratorem podając poniższe szczegóły błędu.",
				AlertType.ERROR, e);
	}

}
