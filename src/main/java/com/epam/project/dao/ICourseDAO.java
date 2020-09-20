package com.epam.project.dao;

import java.util.List;

import com.epam.project.entity.Course;

public interface ICourseDAO {
	List<Course> findAllCourses();
}
