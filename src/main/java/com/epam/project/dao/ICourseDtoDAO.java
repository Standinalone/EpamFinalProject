package com.epam.project.dao;

import java.sql.SQLException;
import java.util.List;

import com.epam.project.dto.CourseDto;

/**
 * Interface for courseDTO DAO
 *
 */
public interface ICourseDtoDAO {

	/**
	 * Finds all courses in a specific range with additional values specific to
	 * courseDto class attached
	 * 
	 * @param limit  LIMIT parameter or how many records to retrieve
	 * @param offset OFFSET parameter or from what record to retrieve
	 * @return list of CourseDto objects
	 * @throws SQLException
	 */
	List<CourseDto> findAllFromTo(int limit, int offset) throws SQLException;

	/**
	 * Finds course by id
	 * 
	 * @param courseId Course id
	 * @return courseDto object
	 * @throws SQLException
	 */
	CourseDto findById(int courseId) throws SQLException;

	/**
	 * Finds all courses with specific lecturer
	 * 
	 * @param userId id of a lecturer
	 * @return list of CourseDto objects
	 * @throws SQLException
	 */
	List<CourseDto> findByLecturerId(int userId) throws SQLException;

	/**
	 * Finds all courses
	 * 
	 * @return list of all CourseDto objects
	 * @throws SQLException
	 */
	List<CourseDto> findAll() throws SQLException;

	/**
	 * Finds all courses with specific lecturer in a range
	 * 
	 * @param lecturerId User id
	 * @param limit      LIMIT parameter or how many records to retrieve
	 * @param offset     OFFSET parameter or from what record to retrieve
	 * @return list of all courses with specific lecturer in a range
	 * @throws SQLException
	 */
	List<CourseDto> findByLecturerIdFromTo(int lecturerId, int limit, int offset) throws SQLException;

	List<CourseDto> findAllFromToWithParameters(int limit, int offset, String conditions, String orderBy)
			throws SQLException;
}
