package com.epam.project.dao;

import java.sql.SQLException;
import java.util.List;
import com.epam.project.entity.User;

public interface IUserDAO {

	User findByLogin(String login) throws SQLException;

	User findById(int userId) throws SQLException;

	boolean add(User user) throws SQLException;

	List<User> findAll() throws SQLException;

	boolean update(User user) throws SQLException;

	User findByEmail(String email) throws SQLException;

	List<User> findAllByRole(int roleId) throws SQLException;

	void addToManyToMany(int courseId, int userId) throws SQLException;

	int getCoursesCountByUser(int userId, boolean enrolled) throws SQLException;

	boolean delete(int userId) throws SQLException;

	List<User> findAllFromTo(int limit, int offset) throws SQLException;

	int getCount() throws SQLException;

	List<User> findAllByCourseIdFromTo(int courseId, int limit, int offset, boolean enrolled) throws SQLException;

	int getUsersWithCourseCount(int courseId, boolean enrolled) throws SQLException;

	void deleteUserFromCourse(int parseInt, int courseId) throws SQLException;

	void registerInCourse(int parseInt, int courseId, boolean registered) throws SQLException;

	void updateGradeForUser(int courseId, int userId, int grade) throws SQLException;

}
