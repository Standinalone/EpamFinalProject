package com.epam.project.service;

import java.util.List;

import com.epam.project.entity.User;

public interface IUserService {

	void enrollToCourse(String[] checkedIds, int user_id);
	
	User findUserByLogin(String login);
	
	User findUserById(int lecturerId);

	boolean addUser(User user);

	List<User> findAllUsers();

	boolean updateUser(User user);

	User findUserByEmail(String email);

	List<User> findAllUsersByRole(int roleId);

	int getCoursesCountForUser(int userId, boolean enrolled);

	void blockUserById(String[] checkedIds);

	void unblockUserById(String[] checkedIds);

	void deleteUserById(int id);

}
