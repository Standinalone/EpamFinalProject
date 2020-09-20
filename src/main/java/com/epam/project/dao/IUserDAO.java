package com.epam.project.dao;

import java.util.List;
import java.util.Optional;

import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.User;

public interface IUserDAO {
	
	User findUserByLogin(String login);
	
	User findUserById(int lecturerId);

	boolean addUser(User user);

	List<User> findAll();

}
