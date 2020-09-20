package com.epam.project.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.ITopicDAO;
import com.epam.project.dao.IUserDAO;
import com.epam.project.entity.Topic;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public class MySqlTopicDAO implements ITopicDAO {
	private static final String SQL_FIND_TOPIC_BY_ID = "SELECT * FROM Topics WHERE id = ?";
	private static final String FIELD_NAME = "name";
	private static DaoFactory daoFactory;
	private static MySqlTopicDAO instance;

	private MySqlTopicDAO() {
	}

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public static ITopicDAO getInstance() {
		if (instance == null) {
			instance = new MySqlTopicDAO();
		}
		return instance;
	}

	@Override
	public Topic findById(int id) {
		daoFactory.open();
		Topic topic = null;
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_FIND_TOPIC_BY_ID);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				topic = new Topic();
				topic.setId(id);
				topic.setName(rs.getString(FIELD_NAME));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		daoFactory.close();
		return topic;
	}

}
