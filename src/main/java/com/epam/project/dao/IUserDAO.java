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

}
