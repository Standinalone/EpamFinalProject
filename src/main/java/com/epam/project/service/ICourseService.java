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
	 * Gets count of all courses
	 * 
	 * @return courses count or 0 if exception occurred
	 */
	int getCoursesCount();

	/**
	 * Finds all courses
	 * 
	 * @return list of all courses or an empty list if exception occurred
	 */
	List<Course> findAllCourses();

	/**
	 * Finds a course by id
	 * 
	 * @param courseId Course id
	 * @return found course or null if it wasn't found
	 */
	Course findCourseById(int courseId);

	/**
	 * Updates a course
	 * 
	 * @param course Course object
	 * @return true if the course was updated or false otherwise
	 * @throws SQLException
	 */
	boolean updateCourse(Course course) throws SQLException;

	/**
	 * Finds a course by id
	 * 
	 * @param courseId Course id
	 * @return found course or null if it wasn't found
	 */
	CourseDto getCourseDtoByCourseId(int courseId);

	/**
	 * Finds all courses
	 * 
	 * @param limit  LIMIT parameter or how many records to retrieve
	 * @param offset OFFSET parameter or from what record to retrieve
	 * @return list of courses in a range or an empty list if nothing was found
	 */
	List<CourseDto> findAllCoursesDtoFromTo(int limit, int offset);

	/**
	 * Finds all courses to which a user is subscribed
	 * 
	 * @param limit    LIMIT parameter or how many records to retrieve
	 * @param offset   OFFSET parameter or from what record to retrieve
	 * @param user     User object
	 * @param enrolled should the user be enrolled or not
	 * @return list of all courses in a range or an empty list if nothing was found
	 */
	List<CourseProfilePageDto> findAllCoursesProfilePageFromTo(int limit, int offset, User user, boolean enrolled);

	/**
	 * Adds new course
	 * 
	 * @param course Course object
	 * @return true if the course was added or false otherwise
	 * @throws SQLException
	 */
	boolean addCourse(Course course) throws SQLException;

	/**
	 * Delete a course by id
	 * 
	 * @param id Course id
	 * @return true if the course was deleted or else otherwise
	 * @throws SQLException
	 */
	boolean deleteCourseById(int id) throws SQLException;

	/**
	 * Finds all courses belonging to a specific lecturer
	 * 
	 * @param userId Lecturer id
	 * @return list of all courses or empty list if nothing was found
	 */
	List<CourseDto> findAllCoursesDtoByLecturerId(int userId);

	/**
	 * Finds all courses
	 * 
	 * @return list of all courses or empty list if nothing was found
	 */
	List<CourseDto> findAllCoursesDto();

	/**
	 * Sets a lecturer for specified courses
	 * 
	 * @param lecturerId User id
	 * @param coursesIds array of Course ids
	 * @throws SQLException
	 */
	void setLecturerForCoursesByLecturerId(int lecturerId, String[] coursesIds) throws SQLException;

	/**
	 * Finds all courses by lecturer in a range
	 * 
	 * @param lecturerId User id
	 * @param limit      LIMIT parameter or how many records to retrieve
	 * @param offset     OFFSET parameter or from what record to retrieve
	 * @return list of all found courses or empty list if nothing was found
	 */
	List<CourseDto> findAllCoursesDtoByLecturerIdFromTo(int lecturerId, int limit, int offset);

	/**
	 * Gets count of courses with lecturer
	 * 
	 * @param lecturerId User id
	 * @return count of courses or 0 if there was an exception
	 */
	int getCoursesWithLecturerCount(int lecturerId);

	/**
	 * Finds all courses in a range with additional condition parameters
	 * 
	 * @param limit      LIMIT parameter or how many records to retrieve
	 * @param offset     OFFSET parameter or from what record to retrieve
	 * @param conditions condition parameters (extra query string) e.g. `WHERE lecturer_id = 2 AND topic_id = 1`
	 * @param orderBy    extra order by query string e.g. `ORDER BY lecturer_id DESC`
	 * @return list of all found courses or empty list if nothing was found
	 */
	List<CourseDto> findAllCoursesDtoFromToWithParameters(int limit, int offset, String conditions, String orderBy);

	/**
	 * Gets count of courses with additional condition parameters
	 * 
	 * @param conditions condition parameters (extra query string) e.g. `WHERE lecturer_id = 2 AND topic_id = 1`
	 * @return count of found courses or 0 if there was an exception
	 */
	int getCoursesWithParametersCount(String conditions);
}
