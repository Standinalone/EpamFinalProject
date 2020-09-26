package com.epam.project.dao;

import java.sql.SQLException;
import java.util.List;

import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.User;

public interface ICourseProfilePageDAO {
	List<CourseProfilePageDto> findAllFromTo(int limit, int offset, User user, boolean enrolled) throws SQLException;
}
