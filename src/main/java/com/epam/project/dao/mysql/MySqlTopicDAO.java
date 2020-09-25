package com.epam.project.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.GenericDAO;
import com.epam.project.dao.ITopicDAO;
import com.epam.project.entity.Topic;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public class MySqlTopicDAO extends GenericDAO<Topic> implements ITopicDAO {
	private static final String SQL_FIND_TOPIC_BY_ID = "SELECT * FROM Topics WHERE id = ?";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_ID = "id";
	private static final String SQL_FIND_ALL = "SELECT * FROM Topics";
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
		Topic topic = null;
		daoFactory.open();
		try {
			List<Topic> list = findByField(daoFactory.getConnection(), SQL_FIND_TOPIC_BY_ID, 1, id);
			if (!list.isEmpty()) {
				topic = list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return topic;
	}

	@Override
	protected Topic mapToEntity(ResultSet rs) {
		Topic topic = new Topic();
		try {
			topic.setId(rs.getInt(FIELD_ID));
			topic.setName(rs.getString(FIELD_NAME));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return topic;
	}

	@Override
	protected boolean mapFromEntity(PreparedStatement ps, Topic topic) {
		return false;
	}

	@Override
	public List<Topic> findAll() {
		List<Topic> topics = new ArrayList<>();
		daoFactory.open();
		try {
			topics = findAll(daoFactory.getConnection(), SQL_FIND_ALL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return topics;
	}

}
