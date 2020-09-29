package com.epam.project.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.epam.project.dto.CourseDto;

public interface ICourseDtoDAO {
	List<CourseDto> findAllFromTo(int limit, int offset) throws SQLException;

	CourseDto findById(int courseId) throws SQLException;

	List<CourseDto> findByLecturerId(int userId) throws SQLException;

	List<CourseDto> findAll() throws SQLException;

	List<CourseDto> findByLecturerIdFromTo(int lecturerId, int limit, int offset) throws SQLException;

	List<CourseDto> findAllFromToWithParameters(int limit, int offset, String conditions, String orderBy)
			throws SQLException;
}
