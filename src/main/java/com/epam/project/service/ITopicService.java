package com.epam.project.service;

import java.util.List;

import com.epam.project.entity.Topic;
import com.epam.project.exceptions.DBTopicException;

/**
 * Interface for Topic service
 *
 */
public interface ITopicService {

	/**
	 * Finds a topic by id
	 * 
	 * @param topicId Topic id
	 * @return Topic object if it was found
	 * @throws DBTopicException if SQLException occurred or nothing found
	 */
	Topic findTopicById(int topicId) throws DBTopicException;

	/**
	 * Finds all topics
	 * 
	 * @return list of topics
	 * @throws DBTopicException if SQLException occurred
	 */
	List<Topic> findAllTopics() throws DBTopicException;
}
