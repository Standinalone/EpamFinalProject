package com.epam.project.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epam.project.Util.Util;
import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.IRoleDAO;
import com.epam.project.dao.ITopicDAO;
import com.epam.project.dao.IUserDAO;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.entity.Role;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.Topic;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public final class MySqlUserDAO implements IUserDAO {
	private static final String FIELD_ID = "id";
	private static final String FIELD_LOGIN = "login";
	private static final String FIELD_PASSWORD = "password";
	private static final String FIELD_BLOCKED = "blocked";
	private static final String FIELD_LECTURER_ID = "lecturer_id";
	private static final String FIELD_SURNAME = "surname";
	private static final String FIELD_PATRONYM = "patronym";
	private static final String FIELD_TOPIC_ID = "topic_id";
	private static final String FIELD_GRADE = "grade";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_STARTDATE = "start_date";
	private static final String FIELD_ENDDATE = "end_date";
	private static final String FIELD_STATUS = "status";
	private static final String FIELD_ROLE_ID = "role_id";
	private static final String FIELD_EMAIL = "email";

	private static final String SQL_GET_COURSE_FOR_USER = "SELECT * FROM Courses_has_users, Courses WHERE course_id = id and user_id = ? and course_id = ?";
	private static final String SQL_GET_COURSES_FOR_USER = "SELECT * FROM Courses_has_users, Courses WHERE course_id = id and user_id = ?";
	private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM Users WHERE login = ?";
	private static final String SQL_ADD_USER = "INSERT INTO Users (`login`, `password`, `role_id`) VALUES (?, ?, 1)";
	private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM Users WHERE id = ?";
	private static final String SQL_FIND_ALL_USERS = "SELECT * FROM Users";


	private static DaoFactory daoFactory;
	private static IRoleDAO roleDAO;
	private static MySqlUserDAO instance;

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
			roleDAO = daoFactory.getRoleDAO();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	private MySqlUserDAO() {
	}

	@Override
	public User findUserByLogin(String login) {
		System.out.println("mysql#findUserByLogin");
		daoFactory.open();
		User user = null;
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_FIND_USER_BY_LOGIN);
			ps.setString(1, login);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setId(rs.getInt(FIELD_ID));
				user.setLogin(rs.getString(FIELD_LOGIN));
				user.setPassword(rs.getString(FIELD_PASSWORD));
				user.setBlocked(rs.getBoolean(FIELD_BLOCKED));
				user.setName(rs.getString(FIELD_NAME));
				user.setSurname(rs.getString(FIELD_SURNAME));
				user.setPatronym(rs.getString(FIELD_PATRONYM));
				user.setEmail(rs.getString(FIELD_EMAIL));
				int roleId = rs.getInt(FIELD_ROLE_ID);
				IRoleDAO roleDAO = daoFactory.getRoleDAO();
				Role role = roleDAO.findById(roleId);
				if (role != null) {
					user.setRole(RoleEnum.valueOf(role.getName().toUpperCase()));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		daoFactory.close();
		return user;
	}

	public static IUserDAO getInstance() {
		if (instance == null) {
			instance = new MySqlUserDAO();
		}
		return instance;
	}

	@Override
	public boolean addUser(User user) {
		System.out.println("mysql#addUser");
		daoFactory.open();
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_ADD_USER, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getLogin());
			ps.setString(2, user.getPassword());

			if (ps.executeUpdate() > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					user.setId(rs.getInt(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		daoFactory.close();
		return true;
	}

	@Override
	public User findUserById(int id) {
		System.out.println("mysql#findUserById");
		daoFactory.open();
		User user = null;
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_FIND_USER_BY_ID);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setId(rs.getInt(FIELD_ID));
				user.setLogin(rs.getString(FIELD_LOGIN));
				user.setPassword(rs.getString(FIELD_PASSWORD));
				user.setBlocked(rs.getBoolean(FIELD_BLOCKED));
				user.setName(rs.getString(FIELD_NAME));
				user.setSurname(rs.getString(FIELD_SURNAME));
				user.setPatronym(rs.getString(FIELD_PATRONYM));
				user.setEmail(rs.getString(FIELD_EMAIL));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		daoFactory.close();
		return user;
	}

	@Override
	public List<User> findAll() {
		List<User> users = new ArrayList<>();
		daoFactory.open();
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL_USERS);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt(FIELD_ID));
				user.setLogin(rs.getString(FIELD_LOGIN));
				user.setPassword(rs.getString(FIELD_PASSWORD));
				user.setBlocked(rs.getBoolean(FIELD_BLOCKED));
				user.setName(rs.getString(FIELD_NAME));
				user.setSurname(rs.getString(FIELD_SURNAME));
				user.setPatronym(rs.getString(FIELD_PATRONYM));
				user.setEmail(rs.getString(FIELD_EMAIL));
				Role role = roleDAO.findById(rs.getInt(FIELD_ROLE_ID));
				if (role != null) {
					user.setRole(RoleEnum.valueOf(role.getName().toUpperCase()));
				}
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return users;
	}
}