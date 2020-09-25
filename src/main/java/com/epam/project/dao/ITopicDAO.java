package com.epam.project.dao;

import java.util.List;

import com.epam.project.entity.Topic;

public interface ITopicDAO {
	Topic findById(int id);

	List<Topic> findAll();
}
