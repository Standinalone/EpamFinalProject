package com.epam.project.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.ITopicDAO;
import com.epam.project.entity.Topic;
import com.epam.project.exceptions.DBTopicException;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ITopicService;

public class MySqlTopicService implements ITopicService{

	private static final Logger log = LoggerFactory.getLogger(MySqlTopicService.class);
	private static DaoFactory daoFactory;

	private static ITopicDAO topicDao;

	private static MySqlTopicService instance;

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
			topicDao = daoFactory.getTopicDAO();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	private MySqlTopicService() {
	}

	public static ITopicService getInstance() {
		if (instance == null) {
			instance = new MySqlTopicService();
		}
		return instance;
	}
	
	@Override
	public Topic findTopicById(int topicId) throws DBTopicException {
		try {
			daoFactory.open();
			return topicDao.findById(topicId);
		} catch (SQLException e) {
			throw new DBTopicException("dberror.topic.get", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<Topic> findAllTopics() throws DBTopicException {
		try {
			daoFactory.open();
			return topicDao.findAll();
		} catch (SQLException e) {
			log.error("Getting topics error", e);
			throw new DBTopicException("dberror.topic.findAll", e);
		} finally {
			daoFactory.close();
		}
	}

}
