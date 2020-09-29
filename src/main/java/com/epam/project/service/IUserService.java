package com.epam.project.service;

import java.util.List;
import java.util.Map;

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

	void blockUsersById(String[] checkedIds);

	void unblockUsersById(String[] checkedIds);

	void deleteUserById(int id);

	List<User> findAllUsersFromTo(int limit, int offset);

	int getUsersCount();
	
	List<User> findAllUsersWithCourseFromTo(int courseId, int limit, int offset, boolean enrolled);

	int getUsersWithCourseCount(int courseId, boolean enrolled);

	void declineRequestForIds(int id, String[] checkedIds);

	void registerInCourseByUsersIds(int id, String[] checkedIds, boolean registered);

	void updateGrades(int courseId, Map<Integer, Integer> userGrade);
}
