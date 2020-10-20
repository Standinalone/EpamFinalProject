package com.epam.project.service.impl;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.ITokenDAO;
import com.epam.project.entity.VerificationToken;
import com.epam.project.exceptions.DBTokenException;
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
			log.error("DatabaseNotSupportedException", e.getMessage());
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
	public void addToken(VerificationToken token) throws DBTokenException{
		try {
			daoFactory.beginTransation();
			tokenDao.add(token);
			daoFactory.getConnection().commit();
		} catch (SQLException e) {
			daoFactory.rollback();
			throw new DBTokenException("dberror.token.add", e);
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public VerificationToken findTokenByToken(String token) throws DBTokenException{
		try {
			daoFactory.open();
			return tokenDao.findByToken(token);
		} catch (SQLException e) {
			throw new DBTokenException("dberror.token.get", e);
		} finally {
			daoFactory.close();
		}
	}

}
