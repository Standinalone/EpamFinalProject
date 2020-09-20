package com.epam.project.service;

import com.epam.project.dao.DatabaseEnum;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.impl.MySqlServiceFactory;

public abstract class ServiceFactory {

	public static ServiceFactory getServiceFactory(DatabaseEnum db) throws DatabaseNotSupportedException {
		try {
			switch (db) {
			case MYSQL:
				return MySqlServiceFactory.getInstance();
			case ORACLE:
				throw new IllegalArgumentException();
			case H2:
				throw new IllegalArgumentException();
			default:
				throw new IllegalArgumentException();
			}
		} catch (IllegalArgumentException e) {
			throw new DatabaseNotSupportedException(db.name() + " db is not supported");
		}
	}
	
	public abstract IUserService getUserService();

	public abstract ICourseService getCourseService();
}
