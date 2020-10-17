package com.epam.project.dao;

import java.sql.SQLException;
import java.util.List;

import com.epam.project.entity.Course;

/**
 * Interface for course DAO
 *
 */
public interface ICourseDAO {
	/**
	 * Finds all courses
	 * 
	 * @return list of courses
	 * @throws SQLException
	 */
	List<Course> findAll() throws SQLException;

	/**
	 * Finds a course by id
	 * 
	 * @param courseId a course id
	 * @return a course found
	 * @throws SQLException
	 */
	Course findById(int courseId) throws SQLException;

	/**
	 * Updates a course by id
	 * 
	 * @param courseId course id
	 * @throws SQLException
	 */
	void update(Course courseId) throws SQLException;

	/**
	 * Gets total count of courses
	 * 
	 * @return count of courses
	 * @throws SQLException
	 */
	int getCount() throws SQLException;

	/**
	 * Adds new course to database
	 * 
	 * @param course Course object
	 * @return true if course was added and false otherwise
	 * @throws SQLException
	 */
	boolean add(Course course) throws SQLException;

	/**
	 * Removes course by id
	 * 
	 * @param courseId course id
	 * @return true if course was removed and false otherwise
	 * @throws SQLException
	 */
	boolean delete(int courseId) throws SQLException;

	/**
	 * Gets count of courses by lecturer id
	 * 
	 * @param lecturerId user id
	 * @return count of courses with specific lecturer
	 * @throws SQLException
	 */
	int getCountByLecturerId(int lecturerId) throws SQLException;

	/**
	 * Gets count of courses with extra conditions provided
	 * 
	 * @param conditions string query with conditions
	 * @return count of courses
	 * @throws SQLException
	 */
	int getCountWithParameters(String conditions) throws SQLException;

}
