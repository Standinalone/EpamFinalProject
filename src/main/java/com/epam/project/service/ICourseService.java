package com.epam.project.service;

import java.sql.SQLException;
import java.util.List;

import com.epam.project.dto.CourseDto;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.User;

/**
 * Interface for Course service
 *
 */
public interface ICourseService {
	/**
	 * Adds new course
	 * 
	 * @param course Course object
	 * @return true if the course was added or false otherwise
	 */
	boolean addCourse(Course course);

	/**
	 * Updates a course
	 * 
	 * @param course Course object
	 * @return true if the course was updated or false otherwise
	 */
	boolean updateCourse(Course course);

	/**
	 * Gets count of courses with additional condition parameters
	 * 
	 * @param conditions condition parameters (extra query string) e.g. `WHERE
	 *                   lecturer_id = 2 AND topic_id = 1`
	 * @return count of found courses or -1 if there was an exception
	 */
	int getCoursesWithParametersCount(String conditions);

	/**
	 * Gets count of courses with lecturer
	 * 
	 * @param lecturerId User id
	 * @return count of courses or -1 if there was an exception
	 */
	int getCoursesWithLecturerCount(int lecturerId);

	/**
	 * Gets count of all courses
	 * 
	 * @return courses count or -1 if exception occurred
	 */
	int getCoursesCount();

	/**
	 * Finds a course by id
	 * 
	 * @param courseId Course id
	 * @return found course or null if it wasn't found
	 * @throws SQLException
	 */
	Course findCourseById(int courseId) throws SQLException;

	/**
	 * Finds a course by id
	 * 
	 * @param courseId Course id
	 * @return found course or null if it wasn't found
	 * @throws SQLException
	 */
	CourseDto getCourseDtoByCourseId(int courseId) throws SQLException;

	/**
	 * Delete a course by id
	 * 
	 * @param id Course id
	 * @return true if the course was deleted or else otherwise
	 */
	boolean deleteCourseById(int id);

	/**
	 * Sets a lecturer for specified courses
	 * 
	 * @param lecturerId User id
	 * @param coursesIds array of Course ids
	 * @return true if the lecturer was set or false otherwise
	 */
	boolean setLecturerForCoursesByLecturerId(int lecturerId, String[] coursesIds);

	/**
	 * Finds all courses
	 * 
	 * @return list of all courses
	 * @throws SQLException
	 */
	List<Course> findAllCourses() throws SQLException;

	/**
	 * Finds all courses belonging to a specific lecturer
	 * 
	 * @param userId Lecturer id
	 * @return list of all courses
	 * @throws SQLException
	 */
	List<CourseDto> findAllCoursesDtoByLecturerId(int userId) throws SQLException;

	/**
	 * Finds all courses
	 * 
	 * @return list of all courses
	 * @throws SQLException
	 */
	List<CourseDto> findAllCoursesDto() throws SQLException;

	/**
	 * Finds all courses by lecturer in a range
	 * 
	 * @param lecturerId User id
	 * @param limit      LIMIT parameter or how many records to retrieve
	 * @param offset     OFFSET parameter or from what record to retrieve
	 * @return list of all found courses in a range or null if exception occurred
	 */
	List<CourseDto> findAllCoursesDtoByLecturerIdFromTo(int lecturerId, int limit, int offset);

	/**
	 * Finds all courses to which a user is subscribed
	 * 
	 * @param limit    LIMIT parameter or how many records to retrieve
	 * @param offset   OFFSET parameter or from what record to retrieve
	 * @param user     User object
	 * @param enrolled should the user be enrolled or not
	 * @return list of all found courses in a range or null if exception occurred
	 */
	List<CourseProfilePageDto> findAllCoursesProfilePageFromTo(int limit, int offset, User user, boolean enrolled);

	/**
	 * Finds all courses
	 * 
	 * @param limit  LIMIT parameter or how many records to retrieve
	 * @param offset OFFSET parameter or from what record to retrieve
	 * @return list of all found courses in a range or null if exception occurred
	 */
	List<CourseDto> findAllCoursesDtoFromTo(int limit, int offset);

	/**
	 * Finds all courses in a range with additional condition parameters
	 * 
	 * @param limit      LIMIT parameter or how many records to retrieve
	 * @param offset     OFFSET parameter or from what record to retrieve
	 * @param conditions condition parameters (extra query string) e.g. `WHERE
	 *                   lecturer_id = 2 AND topic_id = 1`
	 * @param orderBy    extra order by query string e.g. `ORDER BY lecturer_id
	 *                   DESC`
	 * @return list of all found courses in a range or null if exception occurred
	 */
	List<CourseDto> findAllCoursesDtoWithParametersFromTo(int limit, int offset, String conditions, String orderBy);
}
