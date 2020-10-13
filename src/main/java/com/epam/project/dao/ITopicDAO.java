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
	 * @return Topic object or null if it wasn't found
	 * @throws SQLException
	 */
	Topic findById(int id) throws SQLException;

	/**
	 * Finds all tokens
	 * 
	 * @return list of tokens or an empty list if nothing was found
	 * @throws SQLException
	 */
	List<Topic> findAll() throws SQLException;
}
