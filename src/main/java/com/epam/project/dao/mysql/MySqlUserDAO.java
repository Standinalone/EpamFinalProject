package com.epam.project.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.GenericDAO;
import com.epam.project.dao.IUserDAO;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public final class MySqlUserDAO extends GenericDAO<User> implements IUserDAO {

	private static final Logger log = LoggerFactory.getLogger(MySqlUserDAO.class);
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
	private static final String FIELD_GRADE = "grade";

	private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM Users";
	private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM Users, Roles WHERE Users.role_id = Roles.id AND login = ?";
	private static final String SQL_ADD_USER = "INSERT INTO Users (`blocked`, `role_id`, `login`, `password`, `name`, `surname`, `patronym`, `email`, `enabled`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM Users, Roles WHERE Users.role_id = Roles.id AND users.id = ?";
	private static final String SQL_UPDATE_USER_BY_ID = "UPDATE Users SET blocked = ?, role_id = ?, login = ?, password = ?, name = ?, surname = ?, patronym = ?, email = ?, enabled = ? WHERE id = ?";
	private static final String SQL_FIND_ALL = "SELECT * FROM Users, Roles WHERE Users.role_id = Roles.id";
	private static final String SQL_FIND_USER_BY_EMAIL = "SELECT * FROM Users, Roles WHERE Users.role_id = Roles.id AND users.email = ?";
	private static final String SQL_FIND_USER_BY_ROLE = "SELECT * FROM Users, Roles WHERE Users.role_id = Roles.id AND users.role_id = ?";
	private static final String SQL_ENROLL_USER_TO_COURSE = "INSERT INTO Courses_has_users VALUES (?, ?, 0, false)";
	private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM Users WHERE id = ?";
//	private static final String SQL_GET_COURSES_WITH_USER_COUNT = "SELECT COUNT(*) FROM (" + SQL_GET_COURSES_FOR_USER
//			+ " and registered = ?) as t";

	private static final String SQL_GET_COURSES_WITH_USER_COUNT = "SELECT COUNT(*) FROM Courses_has_users WHERE user_id = ?";
	private static final String SQL_FIND_ALL_BY_COURSE_ID = "SELECT * FROM Users, Roles, Courses_has_users WHERE Users.role_id = Roles.id AND user_id = Users.id AND course_id = ?";
	private static final String SQL_GET_USERS_WITH_COURSE_COUNT = "SELECT COUNT(*) FROM Courses_has_users WHERE course_id = ?";
	private static final String SQL_DELETE_USER_FROM_COURSE = "DELETE FROM Courses_has_users WHERE course_id = ? AND user_id = ?";
	private static final String SQL_UPDATE_COURSES_HAS_USERS = "UPDATE Courses_has_users SET registered = ? WHERE course_id = ? AND user_id = ?";
	private static final String SQL_UPDATE_GRADE_FOR_USER = "UPDATE Courses_has_users SET grade = ? WHERE course_id = ? AND user_id = ?";

	private DaoFactory daoFactory;
	private static MySqlUserDAO instance;

//	static {
//		try {
//			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
//		} catch (DatabaseNotSupportedException e) {
//			log.error("DatabaseNotSupportedException", e.getMessage());
//		}
//	}

	private MySqlUserDAO() {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	/**
	 * Constructor for Mockito testing
	 * 
	 * @param daoFactory
	 */
	private MySqlUserDAO(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public static IUserDAO getInstance() {
		if (instance == null) {
			instance = new MySqlUserDAO();
		}
		return instance;
	}

	@Override
	public User findByLogin(String login) throws SQLException {
		List<User> list = findByField(daoFactory.getConnection(), SQL_FIND_USER_BY_LOGIN, 1, login);
		if (list.isEmpty())
			throw new SQLException();
		return list.get(0);
	}

	@Override
	public boolean add(User user) throws SQLException {
		int id = addToDb(daoFactory.getConnection(), SQL_ADD_USER, user);
		if (id > 0) {
			user.setId(id);
			return true;
		}
		return false;
	}

	@Override
	public User findById(int id) throws SQLException {
		List<User> list = findByField(daoFactory.getConnection(), SQL_FIND_USER_BY_ID, 1, id);
		if (list.isEmpty())
			throw new SQLException();
		return list.get(0);
	}

	@Override
	public List<User> findAll() throws SQLException {
		return findAll(daoFactory.getConnection(), SQL_FIND_ALL);
	}

	@Override
	public void update(User user) throws SQLException {
		update(daoFactory.getConnection(), user, SQL_UPDATE_USER_BY_ID, 10, user.getId());
	}

	@Override
	public User findByEmail(String email) throws SQLException {
		List<User> list = findByField(daoFactory.getConnection(), SQL_FIND_USER_BY_EMAIL, 1, email);
		if (list.isEmpty())
			throw new SQLException();
		return list.get(0);
	}

	@Override
	public List<User> findAllByRole(int roleId) throws SQLException {
		return findByField(daoFactory.getConnection(), SQL_FIND_USER_BY_ROLE, 1, roleId);
	}

	@Override
	public void addToManyToMany(int courseId, int userId) throws SQLException {
		addToManyToMany(daoFactory.getConnection(), SQL_ENROLL_USER_TO_COURSE, courseId, userId);
	}

	@Override
	public int getCoursesCountByUser(int userId, boolean enrolled) throws SQLException {
		String sql = SQL_GET_COURSES_WITH_USER_COUNT
				+ (enrolled ? " AND registered = true " : " AND registered = false");
		return getCountByField(daoFactory.getConnection(), sql, userId);
	}

	@Override
	public boolean delete(int userId) throws SQLException {
		return deleteByField(daoFactory.getConnection(), SQL_DELETE_USER_BY_ID, userId);
	}

	@Override
	public List<User> findAllFromTo(int limit, int offset) throws SQLException {
		return findFromTo(daoFactory.getConnection(), SQL_FIND_ALL, limit, offset);
	}

	@Override
	public int getCount() throws SQLException {
		return getCount(daoFactory.getConnection(), SQL_GET_COUNT);
	}

	@Override
	public List<User> findAllByCourseIdFromTo(int courseId, int limit, int offset, boolean enrolled)
			throws SQLException {
		String sql = SQL_FIND_ALL_BY_COURSE_ID + " AND registered = " + (enrolled ? "true" : "false");
		return findByFieldFromTo(daoFactory.getConnection(), sql, limit, offset, 1, courseId);
	}

	@Override
	public int getUsersWithCourseCount(int courseId, boolean enrolled) throws SQLException {
		String sql = SQL_GET_USERS_WITH_COURSE_COUNT
				+ (enrolled ? " AND registered = true " : " AND registered = false");
		return getCountByField(daoFactory.getConnection(), sql, courseId);
	}

	@Override
	public void deleteUserFromCourse(int userId, int courseId) throws SQLException {
		deleteFromManyToMany(daoFactory.getConnection(), SQL_DELETE_USER_FROM_COURSE, courseId, userId);
	}

	@Override
	public void registerInCourse(int userId, int courseId, boolean registered) throws SQLException {
		updateManyToMany(daoFactory.getConnection(), SQL_UPDATE_COURSES_HAS_USERS, registered, courseId, userId);
	}

	@Override
	public void updateGradeForUser(int courseId, int userId, int grade) throws SQLException {
		updateManyToMany(daoFactory.getConnection(), SQL_UPDATE_GRADE_FOR_USER, grade, courseId, userId);
	}

	@Override
	protected User mapToEntity(ResultSet rs) throws SQLException {
		User user = new User();
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
		try {
			user.setGrade(rs.getInt(FIELD_GRADE));
		} catch (SQLException e) {
			log.trace("`Grade` column was omitted");
		}

		return user;
	}

	@Override
	protected void mapFromEntity(PreparedStatement ps, User user) throws SQLException {
		ps.setBoolean(1, user.isBlocked());
		ps.setInt(2, user.getRole().ordinal() + 1);
		ps.setString(3, user.getLogin());
		ps.setString(4, user.getPassword());
		ps.setString(5, user.getName());
		ps.setString(6, user.getSurname());
		ps.setString(7, user.getPatronym());
		ps.setString(8, user.getEmail());
		ps.setBoolean(9, user.isEnabled());
	}

}