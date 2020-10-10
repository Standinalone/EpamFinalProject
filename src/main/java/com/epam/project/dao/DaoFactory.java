package com.epam.project.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.dao.mysql.MySqlDAOFactory;
import com.epam.project.exceptions.DatabaseNotSupportedException;

/**
 * Base abstract dao factory class
 *
 */
public abstract class DaoFactory {
	private static final Logger log = LoggerFactory.getLogger(DaoFactory.class);

	/**
	 * Method for getting a DaoFactory instance based on passed DatabaeEnum object
	 * 
	 * @param db DatabaseEnum object
	 * @return DaoFactory instance
	 * @throws DatabaseNotSupportedException
	 */
	public static DaoFactory getDaoFactory(DatabaseEnum db) throws DatabaseNotSupportedException {
		try {
			switch (db) {
			case MYSQL:
				return MySqlDAOFactory.getInstance();
			default:
				throw new IllegalArgumentException();
			}
		} catch (IllegalArgumentException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
			throw new DatabaseNotSupportedException(db.name() + " db is not supported");
		}
	}

	/**
	 * @return IUserDAO instance
	 */
	public abstract IUserDAO getUserDAO();

	/**
	 * @return ITopicDAO instance
	 */
	public abstract ITopicDAO getTopicDAO();

	/**
	 * @return ICourseDAO instance
	 */
	public abstract ICourseDAO getCourseDAO();

	/**
	 * @return ICourseDtoDAO instance
	 */
	public abstract ICourseDtoDAO getCourseDtoDAO();

	/**
	 * @return ITokenDAO instance
	 */
	public abstract ITokenDAO getTokenDAO();

	/**
	 * @return ICourseProfilePageDAO instance
	 */
	public abstract ICourseProfilePageDAO getCourseProfilePageDAO();

	/**
	 * @return pooled connection
	 * @throws SQLException
	 */
	public abstract Connection getConnection() throws SQLException;

	/**
	 * Closes the connection
	 */
	public abstract void close();

	/**
	 * Opens a new connection
	 */
	public abstract void open();

	/**
	 * Sets up the connection for starting a new transaction
	 * 
	 * @throws SQLException
	 */
	public abstract void beginTransation() throws SQLException;

	/**
	 * Sets up the connection for ending a transaction
	 */
	public abstract void endTransaction();

	/**
	 * Calls a rollback on a connection
	 */
	public abstract void rollback();

}
