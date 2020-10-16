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
import com.epam.project.dao.ICourseDAO;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public class MySqlCourseDAO extends GenericDAO<Course> implements ICourseDAO {
	private static final Logger log = LoggerFactory.getLogger(MySqlCourseDAO.class);
	private static final String FIELD_NAME = "name";
	private static final String FIELD_STARTDATE = "start_date";
	private static final String FIELD_ENDDATE = "end_date";
	private static final String FIELD_STATUS = "statuses.name";
	private static final String FIELD_COURSES_ID = "Courses.id";
	private static final String FIELD_TOPIC = "Courses.topic_id";
	private static final String FIELD_LECTURER_ID = "Courses.lecturer_id";

	private static final String SQL_FIND_ALL = "SELECT * FROM Courses, Statuses WHERE Courses.status_id = Statuses.id";
	private static final String SQL_FIND_COURSE_BY_ID = "SELECT * FROM Courses, Statuses WHERE Courses.status_id = Statuses.id AND Courses.id = ?";
	private static final String SQL_UPDATE_COURSE_BY_ID = "UPDATE Courses SET name = ?, start_date = ?, end_date = ?, status_id = ?, topic_id = ?, lecturer_id = ? WHERE id = ?";
	private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM Courses";
	private static final String SQL_ADD_COURSE = "INSERT INTO Courses (`name`, `start_date`,`end_date`,`status_id`,`topic_id`,`lecturer_id`) VALUES(?, ?, ?, ?, ?, ?)";
	private static final String SQL_DELETE_COURSE_BY_ID = "DELETE FROM Courses WHERE id = ?";
	private static final String SQL_GET_COUNT_BY_LECTURER_ID = "SELECT COUNT(*) FROM Courses WHERE lecturer_id = ?";

	private  DaoFactory daoFactory;
	private static MySqlCourseDAO instance;

	private MySqlCourseDAO() {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}
	
	/**
	 * Constructor for Mockito testing
	 * 
	 * @param daoFactory
	 */
	private MySqlCourseDAO(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

//	static {
//		try {
//			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
//		} catch (DatabaseNotSupportedException e) {
//			log.error("DatabaseNotSupportedException", e.getMessage());
//		}
//	}

	public static ICourseDAO getInstance() {
		if (instance == null) {
			instance = new MySqlCourseDAO();
		}
		return instance;
	}

	@Override
	public List<Course> findAll() throws SQLException {
		return findAll(daoFactory.getConnection(), SQL_FIND_ALL);
	}

	@Override
	public Course findById(int id) throws SQLException {
		List<Course> list = findByField(daoFactory.getConnection(), SQL_FIND_COURSE_BY_ID, 1, id);
		if (list.isEmpty())
			throw new SQLException();
		return list.get(0);
	}

	@Override
	public void update(Course course) throws SQLException {
		update(daoFactory.getConnection(), course, SQL_UPDATE_COURSE_BY_ID, 7, course.getId());
	}

	@Override
	public int getCount() throws SQLException {
		return getCount(daoFactory.getConnection(), SQL_GET_COUNT);
	}

	@Override
	public boolean add(Course course) throws SQLException {
		int id = addToDb(daoFactory.getConnection(), SQL_ADD_COURSE, course);
		if (id > 0) {
			course.setId(id);
			return true;
		}
		return false;
	}

	@Override
	public boolean delete(int courseId) throws SQLException {
		return deleteByField(daoFactory.getConnection(), SQL_DELETE_COURSE_BY_ID, courseId);
	}

	@Override
	public int getCountByLecturerId(int lecturerId) throws SQLException {
		return getCountByField(daoFactory.getConnection(), SQL_GET_COUNT_BY_LECTURER_ID, lecturerId);
	}

	@Override
	public int getCountWithParameters(String conditions) throws SQLException {
		String appendix = (conditions.length() == 0 ? "" : " WHERE " + conditions);
		return getCount(daoFactory.getConnection(), SQL_GET_COUNT + appendix);
	}

	@Override
	protected Course mapToEntity(ResultSet rs) throws SQLException {
		Course course = new Course();
		course.setId(rs.getInt(FIELD_COURSES_ID));
		course.setName(rs.getString(FIELD_NAME));
		course.setStartDate(rs.getDate(FIELD_STARTDATE).toLocalDate());
		course.setEndDate(rs.getDate(FIELD_ENDDATE).toLocalDate());
		course.setStatus(CourseStatusEnum.valueOf(rs.getString(FIELD_STATUS).toUpperCase()));
		course.setTopicId(rs.getInt(FIELD_TOPIC));
		course.setLecturerId(rs.getInt(FIELD_LECTURER_ID));
		return course;

	}

	@Override
	protected void mapFromEntity(PreparedStatement ps, Course course) throws SQLException {

		ps.setString(1, course.getName());
		ps.setDate(2, Date.valueOf(course.getStartDate()));
		ps.setDate(3, Date.valueOf(course.getEndDate()));
		ps.setInt(4, course.getStatus().ordinal() + 1);
		ps.setInt(5, course.getTopicId());
		ps.setInt(6, course.getLecturerId());
	}
}
