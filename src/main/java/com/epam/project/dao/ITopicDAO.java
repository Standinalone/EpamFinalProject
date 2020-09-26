package com.epam.project.dao;

import java.sql.SQLException;
import java.util.List;

import com.epam.project.entity.Topic;

public interface ITopicDAO {
	Topic findById(int id) throws SQLException;

	List<Topic> findAll() throws SQLException;
}
