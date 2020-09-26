package com.epam.project.dao;

import java.sql.SQLException;
import java.util.List;

import com.epam.project.dto.CourseDto;

public interface ICourseDtoDAO {
	List<CourseDto> findAllFromTo(int limit, int offset) throws SQLException;

	CourseDto findById(int courseId) throws SQLException;
}
