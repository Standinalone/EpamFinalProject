package com.epam.project.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.GenericDAO;
import com.epam.project.dao.ITopicDAO;
import com.epam.project.entity.Topic;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public class MySqlTopicDAO extends GenericDAO<Topic> implements ITopicDAO {
	private static final Logger log = LoggerFactory.getLogger(MySqlTopicDAO.class);
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
			log.trace("Database not supported");
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
	public Topic findById(int id) throws SQLException {
		List<Topic> list = findByField(daoFactory.getConnection(), SQL_FIND_TOPIC_BY_ID, 1, id);
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Topic> findAll() throws SQLException {
		return findAll(daoFactory.getConnection(), SQL_FIND_ALL);
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

}
