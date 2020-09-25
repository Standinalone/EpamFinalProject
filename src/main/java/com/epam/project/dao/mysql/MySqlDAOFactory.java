package com.epam.project.dao.mysql;

import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.epam.project.constants.Constants;
import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.ICourseDAO;
import com.epam.project.dao.ICourseHomePageDAO;
import com.epam.project.dao.ICourseProfilePageDAO;
import com.epam.project.dao.ITokenDAO;
import com.epam.project.dao.ITopicDAO;
import com.epam.project.dao.IUserDAO;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

public final class MySqlDAOFactory extends DaoFactory {

	private Connection connection;
	private static MySqlDAOFactory instance;
	private DataSource ds = new MysqlConnectionPoolDataSource();
//	private MySqlDataSource ds = new MysqlConnectionPoolDataSource();

	private MySqlDAOFactory() {
		// Option 1
		try {
			// Obtain environment naming context
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			// Look up data source
			ds = (DataSource) envCtx.lookup("jdbc/epam");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Option 2
//		try {
//			ResourceBundle db = ResourceBundle.getBundle(Constants.PROPS_FILE);
//			String user = db.getString("db.user");
//			String password = db.getString("db.password");
//			String host = db.getString("db.host");
//			String port = db.getString("db.port");
//			String dbName = db.getString("db.dbName");
//
//			String url = String.format("jdbc:mysql://%s:%s/%s", host, port, dbName);
//			ds.setUrl(url);
//			ds.setPassword(password);
//			ds.setUser(user);
//		} catch (MissingResourceException e) {
//			e.printStackTrace();
//		}
	}

	public static DaoFactory getInstance() {
		if (instance == null) {
			instance = new MySqlDAOFactory();
		}
		return instance;
	}

	public Connection getConnection() throws SQLException {
		return connection;
	}

	@Override
	public IUserDAO getUserDAO() {
		return MySqlUserDAO.getInstance();
	}

	@Override
	public void open() {
		try {
			connection = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ITopicDAO getTopicDAO() {
		return MySqlTopicDAO.getInstance();
	}

	@Override
	public ICourseDAO getCourseDAO() {
		return MySqlCourseDAO.getInstance();
	}

	@Override
	public void closeStatementAndResultSet(PreparedStatement ps, ResultSet rs) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ICourseHomePageDAO getCourseHomePageDAO() {
		return MySqlCourseHomePageDAO.getInstance();
	}

	@Override
	public ICourseProfilePageDAO getCourseProfilePageDAO() {
		return MySqlCourseProfilePageDAO.getInstance();
	}

	@Override
	public ITokenDAO getTokenDAO() {
		return MySqlTokenDAO.getInstance();
	}

}
