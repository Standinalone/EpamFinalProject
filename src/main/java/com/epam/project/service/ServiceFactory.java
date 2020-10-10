package com.epam.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.dao.DatabaseEnum;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.impl.MySqlServiceFactory;

/**
 * Base abstract service factory class
 *
 */
public abstract class ServiceFactory {

	private static final Logger log = LoggerFactory.getLogger(ServiceFactory.class);
	
	/**
	 * Method for getting a ServiceFactory instance based on passed DatabaeEnum object
	 * 
	 * @param db DatabaseEnum object
	 * @return ServiceFactory instance
	 * @throws DatabaseNotSupportedException
	 */
	public static ServiceFactory getServiceFactory(DatabaseEnum db) throws DatabaseNotSupportedException {
		try {
			switch (db) {
			case MYSQL:
				return MySqlServiceFactory.getInstance();
			default:
				throw new IllegalArgumentException();
			}
		} catch (IllegalArgumentException e) {
			log.error("Db {} is not supported", db.name());
			throw new DatabaseNotSupportedException(db.name() + " db is not supported");
		}
	}

	/**
	 * @return IUserService instance
	 */
	public abstract IUserService getUserService();

	/**
	 * @return ICourseService instance
	 */
	public abstract ICourseService getCourseService();

	/**
	 * @return ITopicService instance
	 */
	public abstract ITopicService getTopicService();

	/**
	 * @return ITokenService instance
	 */
	public abstract ITokenService getTokenService();
}
