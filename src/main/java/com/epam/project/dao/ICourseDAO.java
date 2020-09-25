package com.epam.project.dao;

import java.util.List;

import com.epam.project.dto.CourseHomePageDto;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.Course;

public interface ICourseDAO {
	List<Course> findAllCourses();

	Course findById(int courseId);

	boolean update(Course course);
}
