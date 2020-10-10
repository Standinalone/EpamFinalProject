package com.epam.project.service;

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
	 */
	Topic findTopicById(int topicId);

	/**
	 * Finds all topics
	 * 
	 * @return list of topics or an empty list if exception occurred
	 */
	List<Topic> findAllTopics();
}
