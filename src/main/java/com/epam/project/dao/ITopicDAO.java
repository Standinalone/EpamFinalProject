package com.epam.project.dao;

import java.sql.SQLException;
import java.util.List;

import com.epam.project.entity.Topic;

/**
 * Interface for topic DAO
 *
 */
public interface ITopicDAO {
	/**
	 * Finds token by id
	 * 
	 * @param id Topic id
	 * @return Topic object
	 * @throws SQLException
	 */
	Topic findById(int id) throws SQLException;

	/**
	 * Finds all tokens
	 * 
	 * @return list of tokens
	 * @throws SQLException
	 */
	List<Topic> findAll() throws SQLException;
}
