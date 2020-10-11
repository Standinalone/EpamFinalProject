package com.epam.project.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.epam.project.entity.User;

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
	 * @return true if the passwords match or false otherwise
	 */
	boolean confirmPassword(User user, String password);

	/**
	 * Adds a user to the database
	 * 
	 * @param user User object
	 * @return true if the user was added or false otherwise
	 */
	boolean addUser(User user);

	/**
	 * Updates grades for users in a course
	 * 
	 * @param courseId  Course id
	 * @param userGrade map of users and their grades
	 * @return true if the grades were updated or false otherwise
	 */
	boolean updateGrades(int courseId, Map<Integer, Integer> userGrade);

	/**
	 * Enrolls user to courses
	 * 
	 * @param courseIds array of Course ids
	 * @param userId    User id
	 * @return true if the user was enrolled or false otherwise
	 */
	boolean enrollToCourse(String[] courseIds, int userId);

	/**
	 * Updates a user
	 * 
	 * @param user User object
	 * @return true if the user was updated or false otherwise
	 */
	boolean updateUser(User user);

	/**
	 * Sets the `blocked` field of the users to true
	 * 
	 * @param userIds ids of the users to be blocked
	 * @return true if the users were blocked or false otherwise
	 */
	boolean blockUsersById(String[] userIds);

	/**
	 * Sets the `blocked` field of the users to false
	 * 
	 * @param userIds ids of the users to be unblocked
	 * @return true if the users were unblocked or false otherwise
	 */
	boolean unblockUsersById(String[] checkedIds);

	/**
	 * Deletes a user by id
	 * 
	 * @param id User id
	 * @return true if the user was deleted or false otherwise
	 */
	boolean deleteUserById(int id);

	/**
	 * Deletes records from `Courses_has_users` table for users
	 * 
	 * @param courseId Course id
	 * @param userIds  array of User ids
	 * @return true if the requests were declined or false otherwise
	 */
	boolean declineRequestForIds(int courseId, String[] userIds);

	/**
	 * Sets the field `registered` in the `Courses_has_users` table for users
	 * 
	 * @param courseId   Course id
	 * @param userIds    array of User ids
	 * @param registered should the user be registered or not
	 * @return true if the `registered` field was set for the course or false
	 *         otherwise
	 */
	boolean registerInCourseByUsersIds(int courseId, String[] userIds, boolean registered);

	/**
	 * Gets the count of courses to which a user's subscribed
	 * 
	 * @param userId   User id
	 * @param enrolled should the user be enrolled or not
	 * @return count of courses or -1 if exception occurred
	 */
	int getCoursesCountForUser(int userId, boolean enrolled);

	/**
	 * Gets all users' count
	 * 
	 * @return count of users or -1 if exception occurred
	 */
	int getUsersCount();

	/**
	 * Gets the count of users subscribed to a course either enrolled or not
	 * 
	 * @param courseId Course id
	 * @param enrolled should the user be enrolled or not
	 * @return count of users or -1 if exception occurred
	 */
	int getUsersWithCourseCount(int courseId, boolean enrolled);

	/**
	 * Finds a user by id
	 * 
	 * @param userId User id
	 * @return found user or null if it wasn't found
	 * @throws SQLException
	 */
	User findUserById(int userId) throws SQLException;

	/**
	 * Finds a user by login
	 * 
	 * @param login user's login
	 * @return found user or null if it wasn't found
	 * @throws SQLException
	 */
	User findUserByLogin(String login) throws SQLException;

	/**
	 * Finds a user by email
	 * 
	 * @param email user's email
	 * @return found user or null if it wasn't found
	 * @throws SQLException
	 */
	User findUserByEmail(String email) throws SQLException;

	/**
	 * Finds all users with some role
	 * 
	 * @param roleId Role id
	 * @return list of users
	 * @throws SQLException
	 */
	List<User> findAllUsersByRole(int roleId) throws SQLException;

	/**
	 * Finds all users
	 * 
	 * @return list of all users
	 * @throws SQLException
	 */
	List<User> findAllUsers() throws SQLException;

	/**
	 * Finds all users in a range
	 * 
	 * @param limit  LIMIT parameter or how many records to retrieve
	 * @param offset OFFSET parameter or from what record to retrieve
	 * @return list of users or null if exception occurred
	 */
	List<User> findAllUsersFromTo(int limit, int offset);

	/**
	 * Finds all users subscribed to a course either enrolled to it or not
	 * 
	 * @param courseId Course id
	 * @param limit    LIMIT parameter or how many records to retrieve
	 * @param offset   OFFSET parameter or from what record to retrieve
	 * @param enrolled should the user be enrolled or not
	 * @return list of users or null if exception occurred
	 */
	List<User> findAllUsersWithCourseFromTo(int courseId, int limit, int offset, boolean enrolled);
}
