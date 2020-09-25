package com.epam.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDAO<T> {
	protected abstract T mapToEntity(ResultSet rs);

	protected abstract boolean mapFromEntity(PreparedStatement ps, T obj);

	protected <V> List<T> findByFieldFromTo(Connection connection, String sql, int limit, int offset, int parameterIndex, V value) {
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
				throw new IllegalArgumentException();
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapToEntity(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatementAndResultSet(ps, rs);
		}
		return list;
	}
	
	protected <V> boolean update(Connection connection, T item, String sql, int parameterIndex, V value) {
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
				throw new IllegalArgumentException();
			}
			mapFromEntity(ps, item);
			if (ps.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			return false;
		} finally {
			closeStatementAndResultSet(ps, rs);
		}
		return false;
	}
	
	protected List<T> findFromTo(Connection connection, String sql, int limit, int offset) {
		List<T> list = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql + " LIMIT " + limit + " OFFSET " + offset);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapToEntity(rs));
			}
		} catch (SQLException e) {

		} finally {
			closeStatementAndResultSet(ps, rs);
		}
		return list;
	}

	protected List<T> findAll(Connection connection, String sql) {
		List<T> list = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapToEntity(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatementAndResultSet(ps, rs);
		}

		return list;
	}

	protected <V> List<T> findByField(Connection connection, String sql, int parameterIndex, V value) {
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
				throw new IllegalArgumentException();
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapToEntity(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatementAndResultSet(ps, rs);
		}
		return list;
	}

	protected int addToDb(Connection connection, String sql, T item) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			if (!mapFromEntity(ps, item)) {
				return 0;
			}
			if (ps.executeUpdate() > 0) {
				rs = ps.getGeneratedKeys();
				if (rs.next()) {
					return rs.getInt(1);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
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
