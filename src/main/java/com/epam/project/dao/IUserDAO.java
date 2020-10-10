package com.epam.project.dao;

import java.sql.SQLException;
import java.util.List;
import com.epam.project.entity.User;

/**
 * Interface for user DAO
 *
 */
public interface IUserDAO {

	/**
	 * Finds user by login
	 * 
	 * @param login login of the user
	 * @return found User or null if it wasn't found
	 * @throws SQLException
	 */
	User findByLogin(String login) throws SQLException;

	/**
	 * Finds user by id
	 * 
	 * @param userId User id
	 * @return found User or null if it wasn't found
	 * @throws SQLException
	 */
	User findById(int userId) throws SQLException;

	/**
	 * Adds new user to the database
	 * 
	 * @param user User object
	 * @return true if the user was added or false otherwise
	 * @throws SQLException
	 */
	boolean add(User user) throws SQLException;

	/**
	 * Finds all users
	 * 
	 * @return list of all users
	 * @throws SQLException
	 */
	List<User> findAll() throws SQLException;

	/**
	 * Updates a user in the database
	 * 
	 * @param user User object
	 * @return true if User was updated or false otherwise
	 * @throws SQLException
	 */
	void update(User user) throws SQLException;

	/**
	 * Finds a user by email
	 * 
	 * @param email email of a user
	 * @return found user or null if it wasn't found
	 * @throws SQLException
	 */
	User findByEmail(String email) throws SQLException;

	/**
	 * Finds all users with role
	 * 
	 * @param roleId Role id
	 * @return list of users with specific role
	 * @throws SQLException
	 */
	List<User> findAllByRole(int roleId) throws SQLException;

	/**
	 * Adds new record to Courses_has_Users table
	 * 
	 * @param courseId id of a Course object
	 * @param userId   id of a User object
	 * @throws SQLException
	 */
	void addToManyToMany(int courseId, int userId) throws SQLException;

	/**
	 * Gets count of all courses belonging to a specific user where he's either
	 * enrolled or not enrolled
	 * 
	 * @param userId   id of a user
	 * @param enrolled is user enrolled
	 * @return count of courses
	 * @throws SQLException
	 */
	int getCoursesCountByUser(int userId, boolean enrolled) throws SQLException;

	/**
	 * Deletes user by id
	 * 
	 * @param userId User id
	 * @return true if the User was deleted or false otherwise
	 * @throws SQLException
	 */
	boolean delete(int userId) throws SQLException;

	/**
	 * Finds all users in a range
	 * 
	 * @param limit  LIMIT parameter or how many records to retrieve
	 * @param offset OFFSET parameter or from what record to retrieve
	 * @return list of all users in a range
	 * @throws SQLException
	 */
	List<User> findAllFromTo(int limit, int offset) throws SQLException;

	/**
	 * Gets users count
	 * 
	 * @return users count
	 * @throws SQLException
	 */
	int getCount() throws SQLException;

	/**
	 * Finds all users that are either enrolled or not to a specific course and
	 * subscribed to this course in a range
	 * 
	 * @param courseId Course id
	 * @param limit    LIMIT parameter or how many records to retrieve
	 * @param offset   OFFSET parameter or from what record to retrieve
	 * @param enrolled should the users be enrolled or not
	 * @return list of users in a range
	 * @throws SQLException
	 */
	List<User> findAllByCourseIdFromTo(int courseId, int limit, int offset, boolean enrolled) throws SQLException;

	/**
	 * Gets count of all users that are either enrolled or not to a specific course
	 * and subscribed to this course
	 * 
	 * @param courseId Course id
	 * @param enrolled should the users be enrolled or not
	 * @return count of all users subscribed to specific course
	 * @throws SQLException
	 */
	int getUsersWithCourseCount(int courseId, boolean enrolled) throws SQLException;

	/**
	 * Deletes a user from course
	 * 
	 * @param userId   User id
	 * @param courseId Course id
	 * @throws SQLException
	 */
	void deleteUserFromCourse(int userId, int courseId) throws SQLException;

	/**
	 * Sets the `registered` status of a user in a course
	 * 
	 * @param userId     User id
	 * @param courseId   Course id
	 * @param registered `registered` status
	 * @throws SQLException
	 */
	void registerInCourse(int userId, int courseId, boolean registered) throws SQLException;

	/**
	 * Updates user's grade in a course
	 * 
	 * @param courseId Course id
	 * @param userId   User id
	 * @param grade    grade
	 * @throws SQLException
	 */
	void updateGradeForUser(int courseId, int userId, int grade) throws SQLException;

}
