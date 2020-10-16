package com.epam.project.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.IUserDAO;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DBUserException;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.IUserService;

public class MySqlUserService implements IUserService {
	private static final Logger log = LoggerFactory.getLogger(MySqlUserService.class);

	private DaoFactory daoFactory;

	private IUserDAO userDao;

	private static MySqlUserService instance;

//	static {
//		try {
//			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
//			userDao = daoFactory.getUserDAO();
//		} catch (DatabaseNotSupportedException e) {
//			log.error("DatabaseNotSupportedException", e.getMessage().getMessage());
//		}
//	}

	private MySqlUserService() {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
			userDao = daoFactory.getUserDAO();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	/**
	 * Constructor used in testing by Mockito
	 * 
	 * @param daoFactory mocked DaoFactory
	 * @param userDao    mocked IUserDAO
	 */
	public MySqlUserService(DaoFactory daoFactory, IUserDAO userDao) {
		this.daoFactory = daoFactory;
		this.userDao = userDao;
	}

	public static IUserService getInstance() {
		if (instance == null) {
			instance = new MySqlUserService();
		}
		return instance;
	}

	private static String hashedPwd(String pwd) throws NoSuchAlgorithmException {
		String generatedPassword = null;
		// Create MessageDigest instance for MD5
		MessageDigest md = MessageDigest.getInstance("MD5");
		// Add password bytes to digest
		md.update(pwd.getBytes());
		// Get the hash's bytes
		byte[] bytes = md.digest();
		// This bytes[] has bytes in decimal format;
		// Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			int upper = (bytes[i] >> 4) & 0xF;
			int lower = bytes[i] & 0xF;

			if (upper > 9)
				sb.append((char) ('a' + upper - 10));
			else
				sb.append(upper);

			if (lower > 9)
				sb.append((char) ('a' + lower - 10));
			else
				sb.append(lower);
		}
		// Get complete hashed password in hex format
		generatedPassword = sb.toString();

		return generatedPassword;
	}

	@Override
	public int getCoursesCountForUser(int userId, boolean enrolled) throws DBUserException {

		try {
			daoFactory.open();
			return userDao.getCoursesCountByUser(userId, enrolled);
		} catch (SQLException e) {
			throw new DBUserException("dberror.user.getCount", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public void enrollToCourse(String[] courseIds, int userId) throws DBUserException {
		try {
			daoFactory.beginTransation();
			for (String id : courseIds) {
				userDao.addToManyToMany(Integer.parseInt(id), userId);
			}
		} catch (SQLException | NumberFormatException e) {
			daoFactory.rollback();
			throw new DBUserException("dberror.user.enroll", e);
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public User findUserByLogin(String login) throws DBUserException {
		try {
			daoFactory.open();
			return userDao.findByLogin(login);
		} catch (SQLException e) {
			throw new DBUserException("dberror.user.get", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public User findUserById(int userId) throws DBUserException {
		try {
			daoFactory.open();
			return userDao.findById(userId);
		} catch (SQLException e) {
			throw new DBUserException("dberror.user.get", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public void addUser(User user) throws DBUserException {
		try {
			user.setPassword(hashedPwd(user.getPassword()));
			daoFactory.beginTransation();
			userDao.add(user);
		} catch (SQLException | NoSuchAlgorithmException e) {
			daoFactory.rollback();
			throw new DBUserException("dberror.user.add", e);
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public List<User> findAllUsers() throws DBUserException {
		try {
			daoFactory.open();
			return userDao.findAll();
		} catch (SQLException e) {
			throw new DBUserException("dberror.user.findAll", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public void updateUser(User user)  throws DBUserException {
		try {
			daoFactory.beginTransation();
			userDao.update(user);
		} catch (SQLException e) {
			daoFactory.rollback();
			throw new DBUserException("dberror.user.update", e);
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public User findUserByEmail(String email) throws DBUserException {
		try {
			daoFactory.open();
			return userDao.findByEmail(email);
		} catch (SQLException e) {
			throw new DBUserException("dberror.user.get", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<User> findAllUsersByRole(int roleId) throws DBUserException {
		try {
			daoFactory.open();
			return userDao.findAllByRole(roleId);
		} catch (SQLException e) {
			throw new DBUserException("dberror.user.findAll", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public void blockUsersById(String[] userIds) throws DBUserException {
		try {
			daoFactory.beginTransation();
			for (String id : userIds) {
				User user = userDao.findById(Integer.parseInt(id));
				user.setBlocked(true);
				userDao.update(user);
			}
		} catch (SQLException e) {
			daoFactory.rollback();
			throw new DBUserException("dberror.user.block", e);
		} finally {
			daoFactory.endTransaction();
		}

	}

	@Override
	public void unblockUsersById(String[] userIds) throws DBUserException {
		try {
			daoFactory.beginTransation();
			for (String id : userIds) {
				User user = userDao.findById(Integer.parseInt(id));
				user.setBlocked(false);
				userDao.update(user);
			}
		} catch (SQLException e) {
			daoFactory.rollback();
			throw new DBUserException("dberror.user.unblock", e);
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public void deleteUserById(int userId) throws DBUserException {
		try {
			daoFactory.beginTransation();
			userDao.delete(userId);
		} catch (SQLException e) {
			daoFactory.rollback();
			throw new DBUserException("dberror.user.delete", e);
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public List<User> findAllUsersFromTo(int limit, int offset) throws DBUserException {
		try {
			daoFactory.open();
			return userDao.findAllFromTo(limit, offset);
		} catch (SQLException e) {
			throw new DBUserException("dberror.user.findAll", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public int getUsersCount() throws DBUserException {
		try {
			daoFactory.open();
			return userDao.getCount();
		} catch (SQLException e) {
			throw new DBUserException("dberror.user.getCount", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<User> findAllUsersWithCourseFromTo(int courseId, int limit, int offset, boolean enrolled) throws DBUserException {
		try {
			daoFactory.open();
			return userDao.findAllByCourseIdFromTo(courseId, limit, offset, enrolled);
		} catch (SQLException e) {
			throw new DBUserException("dberror.user.findAll", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public int getUsersWithCourseCount(int courseId, boolean enrolled) throws DBUserException {
		try {
			daoFactory.open();
			return userDao.getUsersWithCourseCount(courseId, enrolled);
		} catch (SQLException e) {
			throw new DBUserException("dberror.user.getCount", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public void declineRequestForIds(int courseId, String[] userIds)  throws DBUserException {
		try {
			daoFactory.beginTransation();
			for (String userId : userIds) {
				userDao.deleteUserFromCourse(Integer.parseInt(userId), courseId);
			}
		} catch (SQLException e) {
			daoFactory.rollback();
			throw new DBUserException("dberror.user.decline", e);
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public void registerInCourseByUsersIds(int courseId, String[] userIds, boolean registered) throws DBUserException {
		try {
			daoFactory.beginTransation();
			for (String userId : userIds) {
				userDao.registerInCourse(Integer.parseInt(userId), courseId, registered);
			}
		} catch (SQLException e) {
			daoFactory.rollback();
			throw new DBUserException("dberror.user.register", e);
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public void updateGrades(int courseId, Map<Integer, Integer> userGrade) throws DBUserException {
		try {
			daoFactory.beginTransation();
			for (Map.Entry<Integer, Integer> entry : userGrade.entrySet()) {
				userDao.updateGradeForUser(courseId, entry.getKey(), entry.getValue());
			}
		} catch (SQLException e) {
			daoFactory.rollback();
			throw new DBUserException("dberror.user.grades", e);
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public boolean confirmPassword(User user, String password) {
		String hashedPwd = "";
		try {
			hashedPwd = hashedPwd(password);
		} catch (NoSuchAlgorithmException e) {
			log.error("Hashing error", e.getMessage());
		}
		return hashedPwd.equals(user.getPassword());
	}
}
