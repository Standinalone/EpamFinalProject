package com.epam.project.service;

import java.util.List;

import com.epam.project.dto.CourseDto;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DBCourseException;

/**
 * Interface for Course service
 *
 */
public interface ICourseService {
	/**
	 * Adds new course
	 * 
	 * @param course Course object
	 * @throws DBCourseException if SQLException occurred
	 */
	void addCourse(Course course) throws DBCourseException;

	/**
	 * Updates a course
	 * 
	 * @param course Course object
	 * @throws DBCourseException if SQLException occurred
	 */
	void updateCourse(Course course) throws DBCourseException;

	/**
	 * Gets count of courses with additional condition parameters
	 * 
	 * @param conditions condition parameters (extra query string) e.g. `WHERE
	 *                   lecturer_id = 2 AND topic_id = 1`
	 * @return count of found courses
	 * @throws DBCourseException if SQLException occurred
	 */
	int findAllCoursesWithParametersCount(String conditions) throws DBCourseException;

	/**
	 * Gets count of courses with lecturer
	 * 
	 * @param lecturerId User id
	 * @return count of courses
	 * @throws DBCourseException if SQLException occurred
	 */
	int getCoursesWithLecturerCount(int lecturerId) throws DBCourseException;

	/**
	 * Gets count of all courses
	 * 
	 * @return courses count
	 * @throws DBCourseException if SQLException occurred
	 */
	int getCoursesCount() throws DBCourseException;

	/**
	 * Finds a course by id
	 * 
	 * @param courseId Course id
	 * @return found course
	 * @throws DBCourseException if SQLException occurred or nothing found
	 */
	Course findCourseById(int courseId) throws DBCourseException;

	/**
	 * Finds a course by id
	 * 
	 * @param courseId Course id
	 * @return found course
	 * @throws DBCourseException if SQLException occurred or nothing found
	 */
	CourseDto getCourseDtoByCourseId(int courseId) throws DBCourseException;

	/**
	 * Delete a course by id
	 * 
	 * @param id Course id
	 * @throws DBCourseException if SQLException occurred
	 */
	void deleteCourseById(int id) throws DBCourseException;

	/**
	 * Sets a lecturer for specified courses
	 * 
	 * @param lecturerId User id
	 * @param coursesIds array of Course ids
	 * @throws DBCourseException if SQLException occurred
	 */
	void setLecturerForCoursesByLecturerId(int lecturerId, String[] coursesIds) throws DBCourseException;

	/**
	 * Finds all courses
	 * 
	 * @return list of all courses
	 * @throws DBCourseException if SQLException occurred
	 */
	List<Course> findAllCourses() throws DBCourseException;

	/**
	 * Finds all courses belonging to a specific lecturer
	 * 
	 * @param userId Lecturer id
	 * @return list of all courses
	 * @throws DBCourseException if SQLException occurred
	 */
	List<CourseDto> findAllCoursesDtoByLecturerId(int userId) throws DBCourseException;

	/**
	 * Finds all courses
	 * 
	 * @return list of all courses
	 * @throws DBCourseException if SQLException occurred
	 */
	List<CourseDto> findAllCoursesDto() throws DBCourseException;

	/**
	 * Finds all courses by lecturer in a range
	 * 
	 * @param lecturerId User id
	 * @param limit      LIMIT parameter or how many records to retrieve
	 * @param offset     OFFSET parameter or from what record to retrieve
	 * @return list of all found courses in a range
	 * @throws DBCourseException if SQLException occurred
	 */
	List<CourseDto> findAllCoursesDtoByLecturerIdFromTo(int lecturerId, int limit, int offset) throws DBCourseException;

	/**
	 * Finds all courses to which a user is subscribed
	 * 
	 * @param limit    LIMIT parameter or how many records to retrieve
	 * @param offset   OFFSET parameter or from what record to retrieve
	 * @param user     User object
	 * @param enrolled should the user be enrolled or not
	 * @return list of all found courses in a range
	 * @throws DBCourseException if SQLException occurred
	 */
	List<CourseProfilePageDto> findAllCoursesProfilePageFromTo(int limit, int offset, User user, boolean enrolled)
			throws DBCourseException;

	/**
	 * Finds all courses
	 * 
	 * @param limit  LIMIT parameter or how many records to retrieve
	 * @param offset OFFSET parameter or from what record to retrieve
	 * @return list of all found courses in a range
	 * @throws DBCourseException if SQLException occurred
	 */
	List<CourseDto> findAllCoursesDtoFromTo(int limit, int offset) throws DBCourseException;

	/**
	 * Finds all courses in a range with additional condition parameters
	 * 
	 * @param limit      LIMIT parameter or how many records to retrieve
	 * @param offset     OFFSET parameter or from what record to retrieve
	 * @param conditions condition parameters (extra query string) e.g. `WHERE
	 *                   lecturer_id = 2 AND topic_id = 1`
	 * @param orderBy    extra order by query string e.g. `ORDER BY lecturer_id
	 *                   DESC`
	 * @param userId     User id used when setting the `inCourse` boolean field
	 * @return list of all found courses in a range
	 * @throws DBCourseException if SQLException occurred
	 */
	List<CourseDto> findAllCoursesDtoWithParametersFromTo(int limit, int offset, String conditions, String orderBy,
			int userId) throws DBCourseException;

	/**
	 * Finds all courses in a range with additional condition parameters
	 * 
	 * @param limit      LIMIT parameter or how many records to retrieve
	 * @param offset     OFFSET parameter or from what record to retrieve
	 * @param conditions condition parameters (extra query string) e.g. `WHERE
	 *                   lecturer_id = 2 AND topic_id = 1`
	 * @param orderBy    extra order by query string e.g. `ORDER BY lecturer_id
	 *                   DESC`
	 * @return list of all found courses in a range
	 * @throws DBCourseException if SQLException occurred
	 */
	List<CourseDto> findAllCoursesDtoWithParametersFromTo(int limit, int offset, String conditions, String orderBy)
			throws DBCourseException;
}
