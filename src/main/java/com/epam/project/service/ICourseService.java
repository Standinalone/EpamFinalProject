package com.epam.project.service;

import java.util.List;

import com.epam.project.dto.CourseDto;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.User;

public interface ICourseService {
	int getCoursesCount();
	
	List<Course> findAllCourses();

	Course findCourseById(int courseId);

	boolean updateCourse(Course course);

	CourseDto getCourseDtoByCourseId(int courseId);
	
	List<CourseDto> findAllCoursesDtoFromTo(int limit, int offset);
	
	List<CourseProfilePageDto> findAllCoursesProfilePageFromTo(int limit, int offset, User user, boolean enrolled);

	boolean addCourse(Course course);

	boolean deleteCourseById(int id);
}
