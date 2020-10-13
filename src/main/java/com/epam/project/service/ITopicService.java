package com.epam.project.service;

import java.sql.SQLException;
import java.util.List;

import com.epam.project.entity.Topic;

/**
 * Interface for Topic service
 *
 */
public interface ITopicService {

	/**
	 * Finds a topic by id
	 * 
	 * @param topicId Topic id
	 * @return Topic object if it was found or null otherwise
	 * @throws SQLException 
	 */
	Topic findTopicById(int topicId) throws SQLException;

	/**
	 * Finds all topics
	 * 
	 * @return list of topics
	 * @throws SQLException 
	 */
	List<Topic> findAllTopics() throws SQLException;
}
