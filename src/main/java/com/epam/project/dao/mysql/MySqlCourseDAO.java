package com.epam.project.dao.mysql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.GenericDAO;
import com.epam.project.dao.ICourseDAO;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public class MySqlCourseDAO extends GenericDAO<Course> implements ICourseDAO {
	private static final String FIELD_NAME = "name";
	private static final String FIELD_STARTDATE = "start_date";
	private static final String FIELD_ENDDATE = "end_date";
	private static final String FIELD_STATUS = "statuses.name";
	private static final String FIELD_COURSES_ID = "Courses.id";
	private static final String FIELD_TOPIC = "Courses.topic_id";

	private static final String SQL_FIND_ALL = "SELECT * FROM Courses, Statuses WHERE Courses.status_id = Statuses.id";
	private static final String SQL_FIND_COURSE_BY_ID = "SELECT * FROM Courses, Statuses WHERE Courses.status_id = Statuses.id AND Courses.id = ?";
	private static final String SQL_UPDATE_COURSE_BY_ID = "UPDATE Courses SET name = ?, start_date = ?, end_date = ?, status_id = ?, topic_id = ?, lecturer_id = ? WHERE id = ?";
	private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM Courses";
	private static final String SQL_ADD_COURSE = "INSERT INTO Courses (`name`, `start_date`,`end_date`,`status_id`,`topic_id`,`lecturer_id`) VALUES(?, ?, ?, ?, ?, ?)";
	private static final String SQL_DELETE_COURSE_BY_ID = "DELETE FROM Courses WHERE id = ?";
	private static final String SQL_GET_COUNT_BY_LECTURER_ID = "SELECT COUNT(*) FROM Courses WHERE lecturer_id = ?";

	private static DaoFactory daoFactory;
	private static MySqlCourseDAO instance;

	private MySqlCourseDAO() {
	}

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public static ICourseDAO getInstance() {
		if (instance == null) {
			instance = new MySqlCourseDAO();
		}
		return instance;
	}

	@Override
	public List<Course> findAll() throws SQLException {
		List<Course> courses = new ArrayList<>();
		courses = findAll(daoFactory.getConnection(), SQL_FIND_ALL);
		return courses;

//		List<Course> courses = new ArrayList<>();
//		daoFactory.open();
//		try {
//			courses = findAll(daoFactory.getConnection(), SQL_FIND_ALL);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		daoFactory.close();
//		return courses;
	}

	@Override
	public Course findById(int id) throws SQLException {
		List<Course> list = findByField(daoFactory.getConnection(), SQL_FIND_COURSE_BY_ID, 1, id);
		if (!list.isEmpty())
			return list.get(0);
		return null;

//		Course course = null;
//		daoFactory.open();
//		try {
//			List<Course> list = findByField(daoFactory.getConnection(), SQL_FIND_COURSE_BY_ID, 1, id);
//			if (!list.isEmpty()) {
//				course = list.get(0);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		daoFactory.close();
//		return course;
	}

	@Override
	public boolean update(Course course) throws SQLException {
		if (update(daoFactory.getConnection(), course, SQL_UPDATE_COURSE_BY_ID, 7, course.getId())) {
			return true;
		}
		return false;

//		boolean result = false;
//		daoFactory.open();
//		try {
//			if (update(daoFactory.getConnection(), course, SQL_UPDATE_COURSE_BY_ID, 7, course.getId())) {
//				result = true;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			result = false;
//		}
//		daoFactory.close();
//		return result;
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
	protected Course mapToEntity(ResultSet rs) {
		Course course = new Course();
		try {
			course.setId(rs.getInt(FIELD_COURSES_ID));
			course.setName(rs.getString(FIELD_NAME));
			course.setStartDate(rs.getDate(FIELD_STARTDATE).toLocalDate());
			course.setEndDate(rs.getDate(FIELD_ENDDATE).toLocalDate());
			course.setStatus(CourseStatusEnum.valueOf(rs.getString(FIELD_STATUS).toUpperCase()));
			course.setTopicId(rs.getInt(FIELD_TOPIC));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return course;
	}

	@Override
	protected boolean mapFromEntity(PreparedStatement ps, Course course) {

		try {
			ps.setString(1, course.getName());
			ps.setDate(2, Date.valueOf(course.getStartDate()));
			ps.setDate(3, Date.valueOf(course.getEndDate()));
			ps.setInt(4, course.getStatus().ordinal() + 1);
			ps.setInt(5, course.getTopicId());
			ps.setInt(6, course.getLecturerId());
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}




}
