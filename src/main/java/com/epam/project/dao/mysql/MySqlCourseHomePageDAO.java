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
import com.epam.project.dao.ICourseDAO;
import com.epam.project.dao.ICourseHomePageDAO;
import com.epam.project.dto.CourseHomePageDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public class MySqlCourseHomePageDAO extends GenericDAO<CourseHomePageDto> implements ICourseHomePageDAO {
	private static final String FIELD_COURSE_NAME = "Courses.name";
	private static final String FIELD_STARTDATE = "start_date";
	private static final String FIELD_ENDDATE = "end_date";
	private static final String FIELD_STATUS = "Statuses.name";
	private static final String FIELD_STUDENTS = "students";
	private static final String FIELD_USER_NAME = "Users.name";
	private static final String FIELD_USER_SURNAME = "Users.surname";
	private static final String FIELD_USER_PATRONYM = "Users.patronym";
	private static final String FIELD_TOPIC_NAME = "Topics.name";
	private static final String FIELD_ID = "Courses.id";
	
	private static final String SQL_FIND_COURSES_FROM_TO = "SELECT * FROM Courses, (SELECT course_id, COUNT(*) AS 'students' FROM Courses_has_users GROUP BY course_id) AS t, Statuses, Users, Topics WHERE Courses.id = t.course_id AND Courses.lecturer_id = Users.id AND Courses.topic_id = Topics.id AND Courses.status_id = Statuses.id";
	private static final String SQL_FIND_COURSE_BY_ID = "SELECT * FROM Courses, (SELECT course_id, COUNT(*) AS 'students' FROM Courses_has_users GROUP BY course_id) AS t, Statuses, Users, Topics WHERE Courses.id = t.course_id AND Courses.lecturer_id = Users.id AND Courses.topic_id = Topics.id AND Courses.status_id = Statuses.id AND Courses.id = ?";


	private static DaoFactory daoFactory;
	private static MySqlCourseHomePageDAO instance;

	private MySqlCourseHomePageDAO() {
	}

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public static ICourseHomePageDAO getInstance() {
		if (instance == null) {
			instance = new MySqlCourseHomePageDAO();
		}
		return instance;
	}
	@Override
	public List<CourseHomePageDto> findAllCoursesHomePageFromTo(int limit, int offset) {
		List<CourseHomePageDto> courses = new ArrayList<>();
		daoFactory.open();
		try {
			courses = findFromTo(daoFactory.getConnection(), SQL_FIND_COURSES_FROM_TO, limit, offset);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return courses;
	}
	@Override
	protected CourseHomePageDto mapToEntity(ResultSet rs) {
		Course course = new Course();
		CourseHomePageDto courseHomePageDto = new CourseHomePageDto();
		try {
			course.setId(rs.getInt(FIELD_ID));
			course.setName(rs.getString(FIELD_COURSE_NAME));
			course.setStartDate(rs.getDate(FIELD_STARTDATE).toLocalDate());
			course.setEndDate(rs.getDate(FIELD_ENDDATE).toLocalDate());
			course.setStatus(CourseStatusEnum.valueOf(rs.getString(FIELD_STATUS).toUpperCase()));

			courseHomePageDto.setCourse(course);
			
			courseHomePageDto.setTopic(rs.getString(FIELD_TOPIC_NAME));
			courseHomePageDto.setLecturer(rs.getString(FIELD_USER_NAME) + " " + rs.getString(FIELD_USER_SURNAME) + "  "  + rs.getString(FIELD_USER_PATRONYM));
			courseHomePageDto.setStudents(rs.getInt(FIELD_STUDENTS));
			courseHomePageDto.setDuration(Duration.between( course.getStartDate().atStartOfDay(), course.getEndDate().atStartOfDay()).toDays());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courseHomePageDto;
	}
	@Override
	protected boolean mapFromEntity(PreparedStatement ps, CourseHomePageDto obj) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public CourseHomePageDto findById(int id) {
		CourseHomePageDto course = null;
		daoFactory.open();
		try {
			List<CourseHomePageDto> list = findByField(daoFactory.getConnection(), SQL_FIND_COURSE_BY_ID, 1, id);
			if (!list.isEmpty()) {
				course = list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return course;
	}
}
