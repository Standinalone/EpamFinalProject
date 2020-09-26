package com.epam.project.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
			e.printStackTrace();
		}
	}

	private MySqlUserService() {
	}

	public static IUserService getInstance() {
		if (instance == null) {
			instance = new MySqlUserService();
		}
		return instance;
	}

	@Override
	public int getCoursesCountForUser(int userId, boolean enrolled) {

		daoFactory.open();
		try {
			return userDao.getCoursesCountByUser(userId, enrolled);
		} catch (SQLException e) {
			log.error("Enrolling error", e);
			return -1;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public void enrollToCourse(String[] courseIds, int userId) {
		daoFactory.open();
		try {
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
			log.error("Updating user error", e);
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
			log.error("Updating user error", e);
			return null;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public boolean addUser(User user) {
		try {
			daoFactory.open();
			return userDao.add(user);
		} catch (SQLException e) {
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
			return userDao.update(user);
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
	public void blockUserById(String[] userIds) {
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
	public void unblockUserById(String[] userIds) {
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

}
