package com.epam.project.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.GenericDAO;
import com.epam.project.dao.IUserDAO;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public final class MySqlUserDAO extends GenericDAO<User> implements IUserDAO {

	private static final String FIELD_ID = "id";
	private static final String FIELD_LOGIN = "login";
	private static final String FIELD_PASSWORD = "password";
	private static final String FIELD_BLOCKED = "blocked";
	private static final String FIELD_SURNAME = "surname";
	private static final String FIELD_PATRONYM = "patronym";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_EMAIL = "email";
	private static final String FIELD_ROLE_NAME = "roles.name";
	private static final String FIELD_ENABLED = "enabled";

	private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM Users, Roles WHERE Users.role_id = Roles.id AND login = ?";
	private static final String SQL_ADD_USER = "INSERT INTO Users (`blocked`, `role_id`, `login`, `password`, `name`, `surname`, `patronym`, `email`, `enabled`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM Users, Roles WHERE Users.role_id = Roles.id AND users.id = ?";
	private static final String SQL_UPDATE_USER_BY_ID = "UPDATE Users SET blocked = ?, role_id = ?, login = ?, password = ?, name = ?, surname = ?, patronym = ?, email = ?, enabled = ? WHERE id = ?";
	private static final String SQL_FIND_ALL = "SELECT * FROM Users, Roles WHERE Users.role_id = Roles.id";
	private static final String SQL_FIND_USER_BY_EMAIL = "SELECT * FROM Users, Roles WHERE Users.role_id = Roles.id AND users.email = ?";
	private static final String SQL_FIND_USER_BY_ROLE = "SELECT * FROM Users, Roles WHERE Users.role_id = Roles.id AND users.role_id = ?";



	private static DaoFactory daoFactory;
	private static MySqlUserDAO instance;

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	private MySqlUserDAO() {
	}

	public static IUserDAO getInstance() {
		if (instance == null) {
			instance = new MySqlUserDAO();
		}
		return instance;
	}

	@Override
	public User findUserByLogin(String login) {
		User user = null;
		daoFactory.open();
		try {
			List<User> list = findByField(daoFactory.getConnection(), SQL_FIND_USER_BY_LOGIN, 1, login);
			if (!list.isEmpty()) {
				user = list.get(0);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return user;
	}

	@Override
	public boolean addUser(User user) {
		boolean result = false;
		daoFactory.open();
		try {
			int id = addToDb(daoFactory.getConnection(), SQL_ADD_USER, user);
			if (id > 0) {
				user.setId(id);
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return result;
	}

	@Override
	public User findUserById(int id) {
		User user = null;
		daoFactory.open();
		try {
			List<User> list = findByField(daoFactory.getConnection(), SQL_FIND_USER_BY_ID, 1, id);
			if (!list.isEmpty()) {
				user = list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return user;
	}

	@Override
	public List<User> findAllUsers() {
		List<User> users = new ArrayList<>();
		daoFactory.open();
		try {
			users = findAll(daoFactory.getConnection(), SQL_FIND_ALL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return users;
	}

	@Override
	public boolean updateUser(User user) {
		boolean result = false;
		daoFactory.open();
		try {
			if (update(daoFactory.getConnection(), user, SQL_UPDATE_USER_BY_ID, 10, user.getId())) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		daoFactory.close();
		return result;
	}
	
	@Override
	protected User mapToEntity(ResultSet rs) {
		User user = new User();
		try {
			user.setId(rs.getInt(FIELD_ID));
			user.setLogin(rs.getString(FIELD_LOGIN));
			user.setPassword(rs.getString(FIELD_PASSWORD));
			user.setBlocked(rs.getBoolean(FIELD_BLOCKED));
			user.setName(rs.getString(FIELD_NAME));
			user.setSurname(rs.getString(FIELD_SURNAME));
			user.setPatronym(rs.getString(FIELD_PATRONYM));
			user.setEmail(rs.getString(FIELD_EMAIL));
			user.setEnabled(rs.getBoolean(FIELD_ENABLED));
			user.setRole(RoleEnum.valueOf(rs.getString(FIELD_ROLE_NAME).toUpperCase()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	protected boolean mapFromEntity(PreparedStatement ps, User user) {
		try {
			ps.setBoolean(1, user.isBlocked());
			ps.setInt(2, user.getRole().ordinal());
			ps.setString(3, user.getLogin());
			ps.setString(4, user.getPassword());
			ps.setString(5, user.getName());
			ps.setString(6, user.getSurname());
			ps.setString(7, user.getPatronym());
			ps.setString(8, user.getEmail());
			ps.setBoolean(9, user.isEnabled());
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public User findUserByEmail(String email) {
		User user = null;
		daoFactory.open();
		try {
			List<User> list = findByField(daoFactory.getConnection(), SQL_FIND_USER_BY_EMAIL, 1, email);
			if (!list.isEmpty()) {
				user = list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return user;
	}

	@Override
	public List<User> findAllByRole(int roleId) {
		List<User> users = null;
		daoFactory.open();
		try {
			users = findByField(daoFactory.getConnection(), SQL_FIND_USER_BY_ROLE, 1, roleId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return users;
	}

}