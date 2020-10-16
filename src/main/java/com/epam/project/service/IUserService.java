package com.epam.project.service;

import java.util.List;
import java.util.Map;

import com.epam.project.entity.User;
import com.epam.project.exceptions.DBCourseException;
import com.epam.project.exceptions.DBUserException;

/**
 * Interface for User service
 *
 */
public interface IUserService {

	/**
	 * Checks if the entered password matches against the stored password in the
	 * database by first hashing it with a hashing algorithm
	 * 
	 * @param user     User object
	 * @param password password retrieved from the request
	 * @return true if password was successfully confirmed or else otherwise
	 * @throws DBCourseException if SQLException occurred
	 */
	boolean confirmPassword(User user, String password);

	/**
	 * Adds a user to the database
	 * 
	 * @param user User object
	 * @throws DBCourseException if SQLException occurred
	 */
	void addUser(User user) throws DBUserException;

	/**
	 * Updates grades for users in a course
	 * 
	 * @param courseId  Course id
	 * @param userGrade map of users and their grades
	 * @throws DBCourseException if SQLException occurred
	 */
	void updateGrades(int courseId, Map<Integer, Integer> userGrade) throws DBUserException;

	/**
	 * Enrolls user to courses
	 * 
	 * @param courseIds array of Course ids
	 * @param userId    User id
	 * @throws DBCourseException if SQLException occurred
	 */
	void enrollToCourse(String[] courseIds, int userId) throws DBUserException;

	/**
	 * Updates a user
	 * 
	 * @param user User object
	 * @throws DBCourseException if SQLException occurred
	 */
	void updateUser(User user) throws DBUserException;

	/**
	 * Sets the `blocked` field of the users to true
	 * 
	 * @param userIds ids of the users to be blocked
	 * @throws DBCourseException if SQLException occurred
	 */
	void blockUsersById(String[] userIds) throws DBUserException;

	/**
	 * Sets the `blocked` field of the users to false
	 * 
	 * @param userIds ids of the users to be unblocked
	 * @throws DBCourseException if SQLException occurred
	 */
	void unblockUsersById(String[] checkedIds) throws DBUserException;

	/**
	 * Deletes a user by id
	 * 
	 * @param id User id
	 * @throws DBCourseException if SQLException occurred
	 */
	void deleteUserById(int id) throws DBUserException;

	/**
	 * Deletes records from `Courses_has_users` table for users
	 * 
	 * @param courseId Course id
	 * @param userIds  array of User ids
	 * @throws DBCourseException if SQLException occurred
	 */
	void declineRequestForIds(int courseId, String[] userIds) throws DBUserException;

	/**
	 * Sets the field `registered` in the `Courses_has_users` table for users
	 * 
	 * @param courseId   Course id
	 * @param userIds    array of User ids
	 * @param registered should the user be registered or not
	 * @throws DBCourseException if SQLException occurred
	 */
	void registerInCourseByUsersIds(int courseId, String[] userIds, boolean registered) throws DBUserException;

	/**
	 * Gets the count of courses to which a user's subscribed
	 * 
	 * @param userId   User id
	 * @param enrolled should the user be enrolled or not
	 * @return count of courses
	 * @throws DBCourseException if SQLException occurred
	 */
	int getCoursesCountForUser(int userId, boolean enrolled) throws DBUserException;

	/**
	 * Gets all users' count
	 * 
	 * @return count of users
	 * @throws DBCourseException if SQLException occurred
	 */
	int getUsersCount() throws DBUserException;

	/**
	 * Gets the count of users subscribed to a course either enrolled or not
	 * 
	 * @param courseId Course id
	 * @param enrolled should the user be enrolled or not
	 * @return count of users
	 * @throws DBCourseException if SQLException occurred
	 */
	int getUsersWithCourseCount(int courseId, boolean enrolled) throws DBUserException;

	/**
	 * Finds a user by id
	 * 
	 * @param userId User id
	 * @return found user
	 * @throws DBCourseException if SQLException occurred or nothing found
	 */
	User findUserById(int userId) throws DBUserException;

	/**
	 * Finds a user by login
	 * 
	 * @param login user's login
	 * @return found user
	 * @throws DBCourseException if SQLException occurred or nothing found
	 */
	User findUserByLogin(String login) throws DBUserException;

	/**
	 * Finds a user by email
	 * 
	 * @param email user's email
	 * @return found user
	 * @throws DBCourseException if SQLException occurred or nothing found
	 */
	User findUserByEmail(String email) throws DBUserException;

	/**
	 * Finds all users with some role
	 * 
	 * @param roleId Role id
	 * @return list of users
	 * @throws DBCourseException if SQLException occurred
	 */
	List<User> findAllUsersByRole(int roleId) throws DBUserException;

	/**
	 * Finds all users
	 * 
	 * @return list of all users
	 * @throws DBCourseException if SQLException occurred
	 */
	List<User> findAllUsers() throws DBUserException;

	/**
	 * Finds all users in a range
	 * 
	 * @param limit  LIMIT parameter or how many records to retrieve
	 * @param offset OFFSET parameter or from what record to retrieve
	 * @return list of users
	 * @throws DBCourseException if SQLException occurred
	 */
	List<User> findAllUsersFromTo(int limit, int offset) throws DBUserException;

	/**
	 * Finds all users subscribed to a course either enrolled to it or not
	 * 
	 * @param courseId Course id
	 * @param limit    LIMIT parameter or how many records to retrieve
	 * @param offset   OFFSET parameter or from what record to retrieve
	 * @param enrolled should the user be enrolled or not
	 * @return list of users
	 * @throws DBCourseException if SQLException occurred
	 */
	List<User> findAllUsersWithCourseFromTo(int courseId, int limit, int offset, boolean enrolled) throws DBUserException;
}
