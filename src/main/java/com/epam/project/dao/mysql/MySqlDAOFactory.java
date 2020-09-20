package com.epam.project.dao.mysql;

import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.epam.project.constants.Constants;
import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.ICourseDAO;
import com.epam.project.dao.IRoleDAO;
import com.epam.project.dao.ITopicDAO;
import com.epam.project.dao.IUserDAO;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

public final class MySqlDAOFactory extends DaoFactory {

	private Connection connection;
	private static MySqlDAOFactory instance;
	private MysqlDataSource ds = new MysqlConnectionPoolDataSource();

	private MySqlDAOFactory() {

		try {
			ResourceBundle db = ResourceBundle.getBundle(Constants.PROPS_FILE);
			String user = db.getString("user");
			String password = db.getString("password");
			String host = db.getString("host");
			String port = db.getString("port");
			String dbName = db.getString("dbName");

			String url = String.format("jdbc:mysql://%s:%s/%s", host, port, dbName);
			ds.setUrl(url);
			ds.setPassword(password);
			ds.setUser(user);
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}
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
			System.out.println("connection established");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		if (connection != null) {
			try {
				connection.close();
				System.out.println("connection closed");
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
	public IRoleDAO getRoleDAO() {
		return MySqlRoleDAO.getInstance();
	}

	@Override
	public ICourseDAO getCourseDAO() {
		return MySqlCourseDAO.getInstance();
	}

}
