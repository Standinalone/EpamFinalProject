package com.epam.project.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.IUserDAO;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.IUserService;

public class MySqlUserService implements IUserService {
	private static final Logger log = LoggerFactory.getLogger(MySqlUserService.class);

	private static DaoFactory daoFactory;

	private static IUserDAO userDao;

	private static MySqlUserService instance;

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
			userDao = daoFactory.getUserDAO();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	public static void setDaoFactory(DaoFactory daoFactory) {
		MySqlUserService.daoFactory = daoFactory;
	}

	public static void setUserDao(IUserDAO userDao) {
		MySqlUserService.userDao = userDao;
	}

	private MySqlUserService() {
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
//			StringBuilder sb2 = new StringBuilder();
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

//				sb2.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
//				sb2.append(Integer.toHexString(0xFF & bytes[i]));
		}
		// Get complete hashed password in hex format
		generatedPassword = sb.toString();

		return generatedPassword;
	}

	@Override
	public int getCoursesCountForUser(int userId, boolean enrolled) {

		daoFactory.open();
		try {
			return userDao.getCoursesCountByUser(userId, enrolled);
		} catch (SQLException e) {
			log.error("Getting count error", e);
			return -1;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public void enrollToCourse(String[] courseIds, int userId) {
		try {
			daoFactory.open();
			for (String id : courseIds) {
				userDao.addToManyToMany(Integer.parseInt(id), userId);
			}
		} catch (SQLException | NumberFormatException e) {
			log.error("Enrolling error", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public User findUserByLogin(String login) {
		try {
			daoFactory.open();
			return userDao.findByLogin(login);
		} catch (SQLException e) {
			log.error("Getting user error", e);
			return null;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public User findUserById(int userId) {
		try {
			daoFactory.open();
			return userDao.findById(userId);
		} catch (SQLException e) {
			log.error("Getting user error", e);
			return null;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public boolean addUser(User user) {
		try {
			user.setPassword(hashedPwd(user.getPassword()));
			daoFactory.open();
			return userDao.add(user);
		} catch (SQLException | NoSuchAlgorithmException e) {
			log.error("Adding user error", e);
			return false;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<User> findAllUsers() {
		try {
			daoFactory.open();
			return userDao.findAll();
		} catch (SQLException e) {
			log.error("Getting users error", e);
			return new ArrayList<>();
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public boolean updateUser(User user) {
		try {
			daoFactory.open();
			userDao.update(user);
			return true;
		} catch (SQLException e) {
			log.error("Updating user error", e);
			return false;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public User findUserByEmail(String email) {
		try {
			daoFactory.open();
			return userDao.findByEmail(email);
		} catch (SQLException e) {
			log.error("Getting user error", e);
			return null;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<User> findAllUsersByRole(int roleId) {
		try {
			daoFactory.open();
			return userDao.findAllByRole(roleId);
		} catch (SQLException e) {
			log.error("Getting users error", e);
			return new ArrayList<>();
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public void blockUsersById(String[] userIds) {
		try {
			daoFactory.open();
			for (String id : userIds) {
				User user = userDao.findById(Integer.parseInt(id));
				user.setBlocked(true);
				userDao.update(user);
			}
		} catch (SQLException e) {
			log.error("Blocking users error", e);
		} finally {
			daoFactory.close();
		}

	}

	@Override
	public void unblockUsersById(String[] userIds) {
		try {
			daoFactory.open();
			for (String id : userIds) {
				User user = userDao.findById(Integer.parseInt(id));
				user.setBlocked(false);
				userDao.update(user);
			}
		} catch (SQLException e) {
			log.error("Unblocking users error", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public void deleteUserById(int userId) {
		try {
			daoFactory.open();
			userDao.delete(userId);
		} catch (SQLException e) {
			log.error("Deleting user error", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<User> findAllUsersFromTo(int limit, int offset) {
		try {
			daoFactory.open();
			return userDao.findAllFromTo(limit, offset);
		} catch (SQLException e) {
			log.error("Getting users error", e);
			return new ArrayList<>();
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public int getUsersCount() {
		daoFactory.open();
		try {
			return userDao.getCount();
		} catch (SQLException e) {
			log.error("Getting count error", e);
			return 0;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<User> findAllUsersWithCourseFromTo(int courseId, int limit, int offset, boolean enrolled) {
		try {
			daoFactory.open();
			return userDao.findAllByCourseIdFromTo(courseId, limit, offset, enrolled);
		} catch (SQLException e) {
			log.error("Getting users error", e);
			return new ArrayList<>();
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public int getUsersWithCourseCount(int courseId, boolean enrolled) {
		daoFactory.open();
		try {
			return userDao.getUsersWithCourseCount(courseId, enrolled);
		} catch (SQLException e) {
			log.error("Getting users error", e);
			return 0;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public void declineRequestForIds(int courseId, String[] userIds) {
		try {
			daoFactory.open();
			for (String userId : userIds) {
				userDao.deleteUserFromCourse(Integer.parseInt(userId), courseId);
			}
		} catch (SQLException e) {
			log.error("Declining users error", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public void registerInCourseByUsersIds(int courseId, String[] userIds, boolean registered) {
		try {
			daoFactory.open();
			for (String userId : userIds) {
				userDao.registerInCourse(Integer.parseInt(userId), courseId, registered);
			}
		} catch (SQLException e) {
			log.error("Registering in course error", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public void updateGrades(int courseId, Map<Integer, Integer> userGrade) {
		try {
			daoFactory.open();
			for (Map.Entry<Integer, Integer> entry : userGrade.entrySet()) {
				userDao.updateGradeForUser(courseId, entry.getKey(), entry.getValue());
			}
		} catch (SQLException e) {
			log.error("Updating grades error", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public boolean confirmPassword(User user, String password) {
		String hashedPwd = "";
		try {
			hashedPwd = hashedPwd(password);
		} catch (NoSuchAlgorithmException e) {
			log.error("Hashing error", e);
		}
		return hashedPwd.equals(user.getPassword());
	}

//	public static void main(String[] args) throws NoSuchAlgorithmException {
//		System.out.println("performing ");
//		System.out.println(hashedPwd("admin"));
//		System.out.println(hashedPwd("lecturer"));
//		System.out.println(hashedPwd("user"));
//		System.out.println(hashedPwd("blocked_user"));
//	}
}
