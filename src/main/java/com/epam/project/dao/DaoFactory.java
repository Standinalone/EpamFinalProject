package com.epam.project.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.epam.project.dao.mysql.MySqlDAOFactory;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public abstract class DaoFactory {

	public abstract IUserDAO getUserDAO();
	
	public abstract ITopicDAO getTopicDAO();
	
	public abstract IRoleDAO getRoleDAO();

	public abstract ICourseDAO getCourseDAO();

	public static DaoFactory getDaoFactory(DatabaseEnum db) throws DatabaseNotSupportedException {
		try {
			switch (db) {
			case MYSQL:
				return MySqlDAOFactory.getInstance();
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

	public abstract Connection getConnection() throws SQLException;
	
	public abstract void close();

	public abstract void open();


}
