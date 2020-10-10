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

/**
 * This abstract class is used to perform main CRUD operations that are common
 * for all DAOs. It doesn't perform transactions but instead sticks to basic
 * operations
 *
 * @param <T> one of the entity classes, representing one of the DB's table
 */
public abstract class GenericDAO<T> {
	private static final Logger log = LoggerFactory.getLogger(GenericDAO.class);

	/**
	 * Abstract method for binding a ResultSet object to <T> entity
	 * 
	 * @param rs ResultSet object retrieved from a prepared statement
	 * @return <T> entity representing object retrieved from database
	 * @throws SQLException
	 */
	protected abstract T mapToEntity(ResultSet rs) throws SQLException;

	/**
	 * @param ps  PreparedStatement object used to store entity's info
	 * @param obj <T> entity to get info from
	 * @throws SQLException
	 */
	protected abstract void mapFromEntity(PreparedStatement ps, T obj) throws SQLException;

	/**
	 * Method for updating a record in a `many to many` relationship table
	 * 
	 * @param <V>        type of foreign keys values
	 * @param <R>        type of a field that's being updated
	 * @param connection Connection object
	 * @param sql        SQL String
	 * @param value1     updating field's value
	 * @param value2     first foreign key's value
	 * @param value3     second foreign key's value
	 * @throws SQLException
	 */
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

	/**
	 * Method for deleting an entry from a `many to many` relationship table
	 * 
	 * @param <V>        type of foreign keys values
	 * @param connection Connection object
	 * @param sql        SQL String
	 * @param value1     First ForeignKey's value
	 * @param value2     Second ForeignKey's value
	 * @throws SQLException
	 */
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

	/**
	 * Method for deleting an entry by field
	 * 
	 * @param <V>        type of field
	 * @param connection Connection object
	 * @param sql        SQL String
	 * @param value      field value
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Method for adding a new entry to a `many to many` relationship table
	 * 
	 * @param <V>        type of foreign key value
	 * @param connection Connection object
	 * @param sql        SQL String
	 * @param id1        first foreign key's value
	 * @param id2        second foreign key's value
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Method for getting count
	 * 
	 * @param connection Connection object
	 * @param sql        SQL string
	 * @return count obtained after executing SQL query
	 * @throws SQLException
	 */
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

	/**
	 * Method for getting count by field
	 * 
	 * @param <V>        type of field's value
	 * @param connection Connection object
	 * @param sql        SQL string
	 * @param value      field used in WHERE clause to filter records
	 * @return count obtained after executing SQL query
	 * @throws SQLException
	 */
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

	/**
	 * Method for getting records by field in specific range
	 * 
	 * @param <V>            type of field's value
	 * @param connection     Connection object
	 * @param sql            SQL string
	 * @param limit          LIMIT parameter or how many records to retrieve
	 * @param offset         OFFSET parameter or from what record to retrieve
	 * @param parameterIndex index of a parameter that will be inserted
	 * @param value          field's value
	 * @return list of <T> entries obtained by executing an SQL query
	 * @throws SQLException
	 */
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

	/**
	 * Method for updating a record
	 * 
	 * @param <V>            type of field's value
	 * @param connection     Connection object
	 * @param item           <T> entity that's being updated
	 * @param sql            SQL string
	 * @param parameterIndex index of a parameter that will be inserted
	 * @param value          field's value
	 * @throws SQLException if record wasn't updated
	 */
	protected <V> void update(Connection connection, T item, String sql, int parameterIndex, V value)
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
			if (ps.executeUpdate() == 0) {
				log.debug("Record's not updated");
				throw new SQLException("Not updated");
			}
		} finally {
			closeStatementAndResultSet(ps, rs);
		}
	}

	/**
	 * Method for getting records in specific range
	 * 
	 * @param connection Connection object
	 * @param sql        SQL string
	 * @param LIMIT      LIMIT parameter or how many records to retrieve
	 * @param offset     OFFSET parameter or from what record to retrieve
	 * @return list of <T> entries obtained by executing an SQL query
	 * @throws SQLException
	 */
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

	/**
	 * Method for getting all records in table
	 * 
	 * @param connection Connection object
	 * @param sql        SQL string
	 * @return list of <T> entries obtained by executing an SQL query
	 * @throws SQLException
	 */
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

	/**
	 * Method for getting a record by field
	 * 
	 * @param <V>            type of field's value
	 * @param connection     Connection object
	 * @param sql            SQL string
	 * @param parameterIndex index of a parameter that will be inserted
	 * @param value          field's value
	 * @return list of <T> entries obtained by executing an SQL query
	 * @throws SQLException
	 */
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

	/**
	 * Method for adding a new record
	 * 
	 * @param connection Connection object
	 * @param sql        SQL string
	 * @param item       <T> entity that's being added
	 * @return id of the newly added record or 0 if the record wasn't added
	 * @throws SQLException
	 */
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
