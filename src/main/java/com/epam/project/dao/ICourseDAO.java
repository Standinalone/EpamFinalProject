package com.epam.project.dao;

import java.sql.SQLException;
import java.util.List;

import com.epam.project.entity.Course;

public interface ICourseDAO {
	List<Course> findAll() throws SQLException;

	Course findById(int courseId) throws SQLException;

	boolean update(Course courseId) throws SQLException;
	
	int getCount() throws SQLException;

	boolean add(Course course) throws SQLException;

	boolean delete(int courseId) throws SQLException;

	int getCountByLecturerId(int lecturerId) throws SQLException;

	int getCountWithParameters(String conditions) throws SQLException;
}
