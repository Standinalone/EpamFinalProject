package com.epam.project.service;

import java.util.List;

import com.epam.project.entity.Topic;

public interface ITopicService {

	Topic findTopicById(int topicId);

	List<Topic> findAllTopics();
}
