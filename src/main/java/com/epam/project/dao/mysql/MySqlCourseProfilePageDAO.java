package com.epam.project.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.GenericDAO;
import com.epam.project.dao.ICourseDtoDAO;
import com.epam.project.dao.ICourseProfilePageDAO;
import com.epam.project.dto.CourseDto;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public class MySqlCourseProfilePageDAO extends GenericDAO<CourseProfilePageDto> implements ICourseProfilePageDAO{
	private static final String FIELD_COURSE_NAME = "Courses.name";
	private static final String FIELD_STARTDATE = "start_date";
	private static final String FIELD_ENDDATE = "end_date";
	private static final String FIELD_USER_NAME = "Users.name";
	private static final String FIELD_USER_SURNAME = "Users.surname";
	private static final String FIELD_USER_PATRONYM = "Users.patronym";
	private static final String FIELD_TOPIC_NAME = "Topics.name";
	private static final String FIELD_GRADE = "grade";
	private static final String FIELD_ID = "Courses.id";
	private static final String FIELD_LECTURER_ID = "Courses.lecturer_id";
	private static final String FIELD_TOPIC_ID = "Courses.topic_id";
	private static final String FIELD_STATUS = "Statuses.name";
	
	private static final String SQL_FIND_COURSES_FROM_TO = "SELECT * FROM Courses_has_users, Courses, Topics, Users, Statuses WHERE course_id = Courses.id AND user_id = ? AND Topics.id = Courses.topic_id AND Users.id = Courses.lecturer_id AND Statuses.id = Courses.status_id";


	

	private static DaoFactory daoFactory;
	private static MySqlCourseProfilePageDAO instance;

	private MySqlCourseProfilePageDAO() {
	}

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public static ICourseProfilePageDAO getInstance() {
		if (instance == null) {
			instance = new MySqlCourseProfilePageDAO();
		}
		return instance;
	}
	@Override
	public List<CourseProfilePageDto> findAllFromTo(int limit, int offset, User user, boolean enrolled) throws SQLException {

		String sql = SQL_FIND_COURSES_FROM_TO;
		sql += enrolled ? " AND registered = true " : " AND registered = false ";
		
		return findByFieldFromTo(daoFactory.getConnection(), sql, limit, offset, 1, user.getId());
	}

	@Override
	protected CourseProfilePageDto mapToEntity(ResultSet rs) {
		Course course = new Course();
		CourseProfilePageDto courseProfilePageDto = new CourseProfilePageDto();
		try {
			course.setId(rs.getInt(FIELD_ID));
			course.setName(rs.getString(FIELD_COURSE_NAME));
			course.setStartDate(rs.getDate(FIELD_STARTDATE).toLocalDate());
			course.setEndDate(rs.getDate(FIELD_ENDDATE).toLocalDate());
			course.setLecturerId(rs.getInt(FIELD_LECTURER_ID));
			course.setTopicId(rs.getInt(FIELD_TOPIC_ID));
			course.setStatus(CourseStatusEnum.valueOf(rs.getString(FIELD_STATUS).toUpperCase()));
			
			courseProfilePageDto.setCourse(course);
			
			courseProfilePageDto.setTopic(rs.getString(FIELD_TOPIC_NAME));
			courseProfilePageDto.setLecturer(rs.getString(FIELD_USER_NAME) + " " + rs.getString(FIELD_USER_SURNAME) + "  "  + rs.getString(FIELD_USER_PATRONYM));
			courseProfilePageDto.setGrade(rs.getInt(FIELD_GRADE));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courseProfilePageDto;
	}

	@Override
	protected boolean mapFromEntity(PreparedStatement ps, CourseProfilePageDto obj) {
		// TODO Auto-generated method stub
		return false;
	}

}
