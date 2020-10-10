package com.epam.project.service;

import java.util.List;
import java.util.Map;

import com.epam.project.entity.User;

/**
 * Interface for User service
 *
 */
public interface IUserService {

	/**
	 * Enrolls user to courses
	 * 
	 * @param courseIds array of Course ids
	 * @param userId    User id
	 */
	void enrollToCourse(String[] courseIds, int userId);

	/**
	 * Finds a user by login
	 * 
	 * @param login user's login
	 * @return found user or null if it wasn't found
	 */
	User findUserByLogin(String login);

	/**
	 * Finds a user by id
	 * 
	 * @param userId User id
	 * @return found user or null if it wasn't found
	 */
	User findUserById(int userId);

	/**
	 * Adds a user to the database
	 * 
	 * @param user User object
	 * @return true if the user was found or null otherwise
	 */
	boolean addUser(User user);

	/**
	 * Finds all users
	 * 
	 * @return list of all users or an empty list if exception occurred
	 */
	List<User> findAllUsers();

	/**
	 * Updates a user
	 * 
	 * @param user User object
	 * @return true if the user was updated or false otherwise
	 */
	boolean updateUser(User user);

	/**
	 * Finds a user by email
	 * 
	 * @param email user's email
	 * @return found user or null if it wasn't found
	 */
	User findUserByEmail(String email);

	/**
	 * Finds all users with some role
	 * 
	 * @param roleId Role id
	 * @return list of users or an empty list if exception occurred
	 */
	List<User> findAllUsersByRole(int roleId);

	/**
	 * Gets the count of courses to which a user's subscribed
	 * 
	 * @param userId   User id
	 * @param enrolled should the user be enrolled or not
	 * @return count of courses
	 */
	int getCoursesCountForUser(int userId, boolean enrolled);

	/**
	 * Sets the `blocked` field of the users to true
	 * 
	 * @param userIds ids of the users to be blocked
	 */
	void blockUsersById(String[] userIds);

	/**
	 * Sets the `blocked` field of the users to false
	 * 
	 * @param userIds ids of the users to be unblocked
	 */
	void unblockUsersById(String[] checkedIds);

	/**
	 * Deletes a user by id
	 * 
	 * @param id User id
	 */
	void deleteUserById(int id);

	/**
	 * Finds all users in a range
	 * 
	 * @param limit  LIMIT parameter or how many records to retrieve
	 * @param offset OFFSET parameter or from what record to retrieve
	 * @return list of users or an empty list if exception occurred
	 */
	List<User> findAllUsersFromTo(int limit, int offset);

	/**
	 * Gets all users' count
	 * 
	 * @return count of users
	 */
	int getUsersCount();

	/**
	 * Finds all users subscribed to a course either enrolled to it or not
	 * 
	 * @param courseId Course id
	 * @param limit    LIMIT parameter or how many records to retrieve
	 * @param offset   OFFSET parameter or from what record to retrieve
	 * @param enrolled should the user be enrolled or not
	 * @return
	 */
	List<User> findAllUsersWithCourseFromTo(int courseId, int limit, int offset, boolean enrolled);

	/**
	 * Gets the count of users subscribed to a course either enrolled or not
	 * 
	 * @param courseId Course id
	 * @param enrolled should the user be enrolled or not
	 * @return count of users
	 */
	int getUsersWithCourseCount(int courseId, boolean enrolled);

	/**
	 * Deletes records from `Courses_has_users` table for users
	 * 
	 * @param courseId Course id
	 * @param userIds  array of User ids
	 */
	void declineRequestForIds(int courseId, String[] userIds);

	/**
	 * Sets the field `registered` in the `Courses_has_users` table for users
	 * 
	 * @param courseId   Course id
	 * @param userIds    array of User ids
	 * @param registered should the user be registered or not
	 */
	void registerInCourseByUsersIds(int courseId, String[] userIds, boolean registered);

	/**
	 * Updates grades for users in a course
	 * 
	 * @param courseId  Course id
	 * @param userGrade map of users and their grades
	 */
	void updateGrades(int courseId, Map<Integer, Integer> userGrade);

	/**
	 * Checks if the entered password matches against the stored password in the
	 * database by first hashing it with a hashing algorithm
	 * 
	 * @param user     User object
	 * @param password password retrieved from the request
	 * @return true if the passwords match or false otherwise
	 */
	boolean confirmPassword(User user, String password);
}
