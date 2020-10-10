package com.epam.project.dao.mysql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.GenericDAO;
import com.epam.project.dao.ITokenDAO;
import com.epam.project.entity.VerificationToken;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public class MySqlTokenDAO extends GenericDAO<VerificationToken> implements ITokenDAO {
	private static final Logger log = LoggerFactory.getLogger(MySqlTokenDAO.class);
	private static final String FIELD_ID = "id";
	private static final String FIELD_USER_ID = "user_id";
	private static final String FIELD_TOKEN = "token";
	private static final String FIELD_EXPIRY_DATE = "expiry_date";

	private static final String SQL_FIND_TOKEN_BY_TOKEN = "SELECT * FROM Tokens WHERE token = ?";
	private static final String SQL_ADD_TOKEN = "INSERT INTO Tokens (`token`, `expiry_date`, `user_id`) VALUES (?, ?, ?)";
	private static DaoFactory daoFactory;
	private static MySqlTokenDAO instance;

	private MySqlTokenDAO() {
	}

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	public static ITokenDAO getInstance() {
		if (instance == null) {
			instance = new MySqlTokenDAO();
		}
		return instance;
	}

	@Override
	public boolean add(VerificationToken token) throws SQLException {
		int id = addToDb(daoFactory.getConnection(), SQL_ADD_TOKEN, token);
		if (id > 0) {
			token.setId(id);
			return true;
		}
		return false;
	}

	@Override
	public VerificationToken findByToken(String token) throws SQLException {
		VerificationToken verificationToken = null;
		List<VerificationToken> list = findByField(daoFactory.getConnection(), SQL_FIND_TOKEN_BY_TOKEN, 1, token);
		if (!list.isEmpty()) {
			verificationToken = list.get(0);
		}
		return verificationToken;
	}

	@Override
	protected VerificationToken mapToEntity(ResultSet rs) throws SQLException {
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setId(rs.getInt(FIELD_ID));
		verificationToken.setToken(rs.getString(FIELD_TOKEN));
		verificationToken.setExpiryDate(rs.getDate(FIELD_EXPIRY_DATE).toLocalDate());
		verificationToken.setUserId(rs.getInt(FIELD_USER_ID));

		return verificationToken;
	}

	@Override
	protected void mapFromEntity(PreparedStatement ps, VerificationToken token) throws SQLException {

		ps.setString(1, token.getToken());
		ps.setDate(2, Date.valueOf(token.getExpiryDate()));
		ps.setInt(3, token.getUserId());

	}

}
