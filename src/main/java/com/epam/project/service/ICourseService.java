package com.epam.project.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.epam.project.dto.CourseDto;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.User;

public interface ICourseService {
	int getCoursesCount();
	
	List<Course> findAllCourses();

	Course findCourseById(int courseId);

	boolean updateCourse(Course course) throws SQLException;

	CourseDto getCourseDtoByCourseId(int courseId);
	
	List<CourseDto> findAllCoursesDtoFromTo(int limit, int offset);
	
	List<CourseProfilePageDto> findAllCoursesProfilePageFromTo(int limit, int offset, User user, boolean enrolled);

	boolean addCourse(Course course) throws SQLException;

	boolean deleteCourseById(int id) throws SQLException;

	List<CourseDto> findAllCoursesDtoByLecturerId(int userId);

	List<CourseDto> findAllCoursesDto();

	void setLecturerForCoursesByLecturerId(int lecturerId, String[] checkedIds) throws SQLException;

	void deleteLecturerForCoursesByLecturerId(int lecturerId, String[] checkedIds) throws SQLException;

	List<CourseDto> findAllCoursesDtoByLecturerIdFromTo(int lecturerId, int limit, int offset);

	int getCoursesWithLecturerCount(int lecturerId);
	
	List<CourseDto> findAllCoursesDtoFromToWithParameters(int limit, int offset, String conditions, String orderBy);
	
	int getCoursesWithParametersCount(String conditions);
}
