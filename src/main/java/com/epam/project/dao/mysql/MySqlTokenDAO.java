package com.epam.project.dao.mysql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.GenericDAO;
import com.epam.project.dao.ITokenDAO;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.entity.VerificationToken;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public class MySqlTokenDAO extends GenericDAO<VerificationToken> implements ITokenDAO {
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
			e.printStackTrace();
		}
	}

	public static ITokenDAO getInstance() {
		if (instance == null) {
			instance = new MySqlTokenDAO();
		}
		return instance;
	}

	@Override
	public boolean add(VerificationToken token) {
		boolean result = false;
		daoFactory.open();
		try {
			int id = addToDb(daoFactory.getConnection(), SQL_ADD_TOKEN, token);
			if (id > 0) {
				token.setId(id);
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return result;
	}

	@Override
	public VerificationToken findByToken(String token) {
		VerificationToken verificationToken = null;
		daoFactory.open();
		try {
			List<VerificationToken> list = findByField(daoFactory.getConnection(), SQL_FIND_TOKEN_BY_TOKEN, 1, token);
			if (!list.isEmpty()) {
				verificationToken = list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return verificationToken;
	}

	@Override
	protected VerificationToken mapToEntity(ResultSet rs) {
		VerificationToken verificationToken = new VerificationToken();
		try {
			verificationToken.setId(rs.getInt(FIELD_ID));
			verificationToken.setToken(rs.getString(FIELD_TOKEN));
			verificationToken.setExpiryDate(rs.getDate(FIELD_EXPIRY_DATE).toLocalDate());
			verificationToken.setUserId(rs.getInt(FIELD_USER_ID));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return verificationToken;
	}

	@Override
	protected boolean mapFromEntity(PreparedStatement ps, VerificationToken token) {
		try {
			ps.setString(1, token.getToken());
			ps.setDate(2, Date.valueOf(token.getExpiryDate()));
			ps.setInt(3, token.getUserId());
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
