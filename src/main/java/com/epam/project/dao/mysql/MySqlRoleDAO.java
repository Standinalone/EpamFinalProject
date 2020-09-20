package com.epam.project.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.IRoleDAO;
import com.epam.project.dao.ITopicDAO;
import com.epam.project.entity.Role;
import com.epam.project.entity.Topic;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public class MySqlRoleDAO implements IRoleDAO {
	private static final String FIELD_NAME = "name";

	private static final String SQL_FIND_ROLE_BY_ID = "SELECT * FROM Roles WHERE id = ?";
	private static DaoFactory daoFactory;
	private static MySqlRoleDAO instance;

	private MySqlRoleDAO() {
	}

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public static IRoleDAO getInstance() {
		if (instance == null) {
			instance = new MySqlRoleDAO();
		}
		return instance;
	}

	@Override
	public Role findById(int id) {
		daoFactory.open();
		Role role = null;
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_FIND_ROLE_BY_ID);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				role = new Role();
				role.setId(id);
				role.setName(rs.getString(FIELD_NAME));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		daoFactory.close();
		return role;
	}

}
