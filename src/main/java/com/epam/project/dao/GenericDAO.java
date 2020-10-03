package com.epam.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GenericDAO<T> {
	private static final Logger log = LoggerFactory.getLogger(GenericDAO.class);
	
	protected abstract T mapToEntity(ResultSet rs) throws SQLException;

	protected abstract void mapFromEntity(PreparedStatement ps, T obj) throws SQLException;

	protected <V, R> void updateManyToMany(Connection connection, String sql, R value1, V value2, V value3)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			switch (value1.getClass().getSimpleName()) {
			case "Integer":
				ps.setInt(1, (Integer) value1);
				break;
			case "String":
				ps.setString(1, (String) value1);
				break;
			case "Boolean":
				ps.setBoolean(1, (Boolean) value1);
				break;
			default:
				log.trace("Uknown format");
				throw new IllegalArgumentException();
			}
			switch (value3.getClass().getSimpleName()) {
			case "Integer":
				ps.setInt(2, (Integer) value2);
				ps.setInt(3, (Integer) value3);
				break;
			case "String":
				ps.setString(2, (String) value2);
				ps.setString(3, (String) value3);
				break;
			default:
				log.trace("Uknown format");
				throw new IllegalArgumentException();
			}
			ps.executeUpdate();
		} finally {
			closeStatementAndResultSet(ps, rs);
		}
	}

	protected <V> void deleteFromManyToMany(Connection connection, String sql, V value1, V value2) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			switch (value1.getClass().getSimpleName()) {
			case "Integer":
				ps.setInt(1, (Integer) value1);
				ps.setInt(2, (Integer) value2);
				break;
			case "String":
				ps.setString(1, (String) value1);
				ps.setString(1, (String) value2);
				break;
			default:
				log.trace("Uknown format");
				throw new IllegalArgumentException();
			}
			ps.executeUpdate();
		} finally {
			closeStatementAndResultSet(ps, rs);
		}
	}

	protected <V> boolean deleteByField(Connection connection, String sql, V value) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			switch (value.getClass().getSimpleName()) {
			case "Integer":
				ps.setInt(1, (Integer) value);
				break;
			case "String":
				ps.setString(1, (String) value);
				break;
			default:
				log.trace("Uknown format");
				throw new IllegalArgumentException();
			}
			if (ps.executeUpdate() > 0) {
				return true;
			}
			return false;
		} finally {
			closeStatementAndResultSet(ps, rs);
		}
	}

	protected <V> boolean addToManyToMany(Connection connection, String sql, V id1, V id2) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			switch (id1.getClass().getSimpleName()) {
			case "Integer":
				ps.setInt(1, (Integer) id1);
				ps.setInt(2, (Integer) id2);
				break;
			case "String":
				ps.setString(1, (String) id1);
				ps.setString(1, (String) id2);
				break;
			default:
				log.trace("Uknown format");
				throw new IllegalArgumentException();
			}
			if (ps.executeUpdate() > 0) {
				return true;
			}
			return false;
		} finally {
			closeStatementAndResultSet(ps, rs);
		}
	}

	protected int getCount(Connection connection, String sql) throws SQLException {
		int count = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
		} finally {
			log.trace("Uknown format");
			closeStatementAndResultSet(ps, rs);
		}
		return count;
	}

	protected <V> int getCountByField(Connection connection, String sql, V value) throws SQLException {
		int count = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			switch (value.getClass().getSimpleName()) {
			case "Integer":
				ps.setInt(1, (Integer) value);
				break;
			case "String":
				ps.setString(1, (String) value);
				break;
			default:
				log.trace("Uknown format");
				throw new IllegalArgumentException();
			}
			rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
		} finally {
			closeStatementAndResultSet(ps, rs);
		}
		return count;
	}

	protected <V> List<T> findByFieldFromTo(Connection connection, String sql, int limit, int offset,
			int parameterIndex, V value) throws SQLException {
		List<T> list = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql + " LIMIT " + limit + " OFFSET " + offset);
			switch (value.getClass().getSimpleName()) {
			case "Integer":
				ps.setInt(parameterIndex, (Integer) value);
				break;
			case "String":
				ps.setString(parameterIndex, (String) value);
				break;
			default:
				log.trace("Uknown format");
				throw new IllegalArgumentException();
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapToEntity(rs));
			}
		} finally {
			closeStatementAndResultSet(ps, rs);
		}
		return list;
	}

	protected <V> boolean update(Connection connection, T item, String sql, int parameterIndex, V value)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			switch (value.getClass().getSimpleName()) {
			case "Integer":
				ps.setInt(parameterIndex, (Integer) value);
				break;
			case "String":
				ps.setString(parameterIndex, (String) value);
				break;
			default:
				log.trace("Uknown format");
				throw new IllegalArgumentException();
			}
			mapFromEntity(ps, item);
			if (ps.executeUpdate() > 0) {
				return true;
			}
		} finally {
			closeStatementAndResultSet(ps, rs);
		}
		return false;
	}

	protected List<T> findFromTo(Connection connection, String sql, int limit, int offset) throws SQLException {
		List<T> list = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql + " LIMIT " + limit + " OFFSET " + offset);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapToEntity(rs));
			}
		} finally {
			closeStatementAndResultSet(ps, rs);
		}
		return list;
	}

	protected List<T> findAll(Connection connection, String sql) throws SQLException {
		List<T> list = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapToEntity(rs));
			}
		} finally {
			closeStatementAndResultSet(ps, rs);
		}

		return list;
	}

	protected <V> List<T> findByField(Connection connection, String sql, int parameterIndex, V value)
			throws SQLException {
		List<T> list = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			switch (value.getClass().getSimpleName()) {
			case "Integer":
				ps.setInt(parameterIndex, (Integer) value);
				break;
			case "String":
				ps.setString(parameterIndex, (String) value);
				break;
			default:
				log.trace("Uknown format");
				throw new IllegalArgumentException();
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapToEntity(rs));
			}
		} finally {
			closeStatementAndResultSet(ps, rs);
		}
		return list;
	}

	protected int addToDb(Connection connection, String sql, T item) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			mapFromEntity(ps, item);
			if (ps.executeUpdate() > 0) {
				rs = ps.getGeneratedKeys();
				if (rs.next()) {
					return rs.getInt(1);
				}
			}

		} finally {
			closeStatementAndResultSet(ps, rs);
		}
		return 0;
	}

	private void closeStatementAndResultSet(PreparedStatement ps, ResultSet rs) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
