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
import com.epam.project.dto.CourseHomePageDto;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public class MySqlCourseDAO extends GenericDAO<Course> implements ICourseDAO {
	private static final String FIELD_NAME = "name";
	private static final String FIELD_STARTDATE = "start_date";
	private static final String FIELD_ENDDATE = "end_date";
	private static final String FIELD_STATUS = "statuses.name";
	private static final String FIELD_COURSES_ID = "Courses.id";

	private static final String SQL_FIND_ALL = "SELECT * FROM Courses, Statuses WHERE Courses.status_id = Statuses.id";
	private static final String SQL_FIND_COURSE_BY_ID = "SELECT * FROM Courses, Statuses WHERE Courses.status_id = Statuses.id AND Courses.id = ?";
	private static final String SQL_UPDATE_COURSE_BY_ID = "UPDATE Courses SET name = ?, start_date = ?, end_date = ?, status_id = ?, topic_id = ?, lecturer_id = ? WHERE id = ?";
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
	public List<Course> findAllCourses() {
		List<Course> courses = new ArrayList<>();
		daoFactory.open();
		try {
			courses = findAll(daoFactory.getConnection(), SQL_FIND_ALL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return courses;
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
			ps.setInt(4, course.getStatus().ordinal()+1);
			ps.setInt(5, course.getTopic_id());
			ps.setInt(6, course.getLecturer_id());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Course findById(int id) {
		Course course = null;
		daoFactory.open();
		try {
			List<Course> list = findByField(daoFactory.getConnection(), SQL_FIND_COURSE_BY_ID, 1, id);
			if (!list.isEmpty()) {
				course = list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return course;
	}

	@Override
	public boolean update(Course course) {
		boolean result = false;
		daoFactory.open();
		try {
			if (update(daoFactory.getConnection(), course, SQL_UPDATE_COURSE_BY_ID, 7, course.getId())) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		daoFactory.close();
		return result;
	}

}
