package com.epam.project.service.impl;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.ITokenDAO;
import com.epam.project.entity.VerificationToken;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ITokenService;

public class MySqlTokenService implements ITokenService {
	private static final Logger log = LoggerFactory.getLogger(MySqlTokenService.class);
	private static DaoFactory daoFactory;

	private static ITokenDAO tokenDao;

	private static MySqlTokenService instance;

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
			tokenDao = daoFactory.getTokenDAO();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	private MySqlTokenService() {
	}

	public static ITokenService getInstance() {
		if (instance == null) {
			instance = new MySqlTokenService();
		}
		return instance;
	}

	@Override
	public boolean addToken(VerificationToken token) {
		try {
			daoFactory.open();
			return tokenDao.add(token);
		} catch (SQLException e) {
			log.error("Adding token error", e);
			return false;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public VerificationToken findTokenByToken(String token) {
		try {
			daoFactory.open();
			return tokenDao.findByToken(token);
		} catch (SQLException e) {
			log.error("Getting token error", e);
			return null;
		} finally {
			daoFactory.close();
		}
	}

}
