package com.epam.project.dao;

import java.sql.SQLException;
import java.util.List;

import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.User;

/**
 * Interface for courseProfilePageDto DAO
 *
 */
public interface ICourseProfilePageDAO {
	/**
	 * Finds all courses for a specific user where he is either enrolled or
	 * not enrolled
	 * 
	 * @param limit    LIMIT parameter or how many records to retrieve
	 * @param offset   OFFSET parameter or from what record to retrieve
	 * @param user     User object
	 * @param enrolled is user enrolled
	 * @return list of all courses in range for specific user
	 * @throws SQLException
	 */
	List<CourseProfilePageDto> findAllFromTo(int limit, int offset, User user, boolean enrolled) throws SQLException;
}
