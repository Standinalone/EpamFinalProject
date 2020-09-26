package com.epam.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.epam.project.dao.mysql.MySqlDAOFactory;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public abstract class DaoFactory {

	public abstract IUserDAO getUserDAO();
	
	public abstract ITopicDAO getTopicDAO();

	public abstract ICourseDAO getCourseDAO();
	
	public abstract ICourseDtoDAO getCourseDtoDAO();
	
	public abstract ITokenDAO getTokenDAO();

	public abstract ICourseProfilePageDAO getCourseProfilePageDAO();

	public static DaoFactory getDaoFactory(DatabaseEnum db) throws DatabaseNotSupportedException {
		try {
			switch (db) {
			case MYSQL:
				return MySqlDAOFactory.getInstance();
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

	public abstract void closeStatementAndResultSet(PreparedStatement ps, ResultSet rs);

}
