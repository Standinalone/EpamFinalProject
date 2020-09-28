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
import com.epam.project.dao.ICourseDtoDAO;
import com.epam.project.dto.CourseDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public class MySqlCourseDtoDAO extends GenericDAO<CourseDto> implements ICourseDtoDAO {
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
	private static final String FIELD_LECTURER_ID = "Courses.lecturer_id";
	private static final String FIELD_TOPIC_ID = "Courses.topic_id";

//	private static final String SQL_FIND_COURSES_FROM_TO = "SELECT * FROM Courses, (SELECT course_id, COUNT(*) AS 'students' FROM Courses_has_users GROUP BY course_id) AS t, Statuses, Users, Topics WHERE Courses.id = t.course_id AND Courses.lecturer_id = Users.id AND Courses.topic_id = Topics.id AND Courses.status_id = Statuses.id";
	private static final String SQL_FIND_COURSES_FROM_TO = "SELECT * FROM Courses, (SELECT Courses.id, COALESCE(students, 0) AS `students` FROM Courses LEFT JOIN (SELECT course_id, COUNT(*) AS 'students' FROM Courses_has_users GROUP BY course_id) AS t1 ON Courses.id = t1.course_id) AS t, Statuses, Users, Topics WHERE Courses.id = t.id AND Courses.lecturer_id = Users.id AND Courses.topic_id = Topics.id AND Courses.status_id = Statuses.id";
	//private static final String SQL_FIND_COURSE_BY_ID = "SELECT * FROM Courses, (SELECT course_id, COUNT(*) AS 'students' FROM Courses_has_users GROUP BY course_id) AS t, Statuses, Users, Topics WHERE Courses.id = t.course_id AND Courses.lecturer_id = Users.id AND Courses.topic_id = Topics.id AND Courses.status_id = Statuses.id AND Courses.id = ?";
	private static final String SQL_FIND_COURSE_BY_ID = "SELECT * FROM Courses, (SELECT Courses.id, COALESCE(students, 0) AS `students` FROM Courses LEFT JOIN (SELECT course_id, COUNT(*) AS 'students' FROM Courses_has_users GROUP BY course_id) AS t1 ON Courses.id = t1.course_id) AS t, Statuses, Users, Topics WHERE Courses.id = t.id AND Courses.lecturer_id = Users.id AND Courses.topic_id = Topics.id AND Courses.status_id = Statuses.id AND Courses.id = ?";
	private static final String SQL_FIND_COURSES_BY_LECTURER_ID = "SELECT * FROM Courses, (SELECT Courses.id, COALESCE(students, 0) AS `students` FROM Courses LEFT JOIN (SELECT course_id, COUNT(*) AS 'students' FROM Courses_has_users GROUP BY course_id) AS t1 ON Courses.id = t1.course_id) AS t, Statuses, Users, Topics WHERE Courses.id = t.id AND Courses.lecturer_id = Users.id AND Courses.topic_id = Topics.id AND Courses.status_id = Statuses.id AND Courses.lecturer_id = ?";
	private static final String SQL_FIND_ALL = "SELECT * FROM Courses, (SELECT Courses.id, COALESCE(students, 0) AS `students` FROM Courses LEFT JOIN (SELECT course_id, COUNT(*) AS 'students' FROM Courses_has_users GROUP BY course_id) AS t1 ON Courses.id = t1.course_id) AS t, Statuses, Users, Topics WHERE Courses.id = t.id AND Courses.lecturer_id = Users.id AND Courses.topic_id = Topics.id AND Courses.status_id = Statuses.id";

	private static DaoFactory daoFactory;
	private static MySqlCourseDtoDAO instance;

	private MySqlCourseDtoDAO() {
	}

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public static ICourseDtoDAO getInstance() {
		if (instance == null) {
			instance = new MySqlCourseDtoDAO();
		}
		return instance;
	}

	@Override
	public List<CourseDto> findAllFromTo(int limit, int offset) throws SQLException {
		return findFromTo(daoFactory.getConnection(), SQL_FIND_COURSES_FROM_TO, limit, offset);
	}

	@Override
	public CourseDto findById(int id) throws SQLException {
		List<CourseDto> list = findByField(daoFactory.getConnection(), SQL_FIND_COURSE_BY_ID, 1, id);
		if (!list.isEmpty())
			return list.get(0);
		return null;
	}

	@Override
	public List<CourseDto> findByLecturerId(int userId) throws SQLException {
		return findByField(daoFactory.getConnection(), SQL_FIND_COURSES_BY_LECTURER_ID, 1, userId);
	}

	@Override
	public List<CourseDto> findAll() throws SQLException {
		return findAll(daoFactory.getConnection(), SQL_FIND_ALL);
	}

	@Override
	public List<CourseDto> findByLecturerIdFromTo(int lecturerId, int limit, int offset) throws SQLException {
		return findByFieldFromTo(daoFactory.getConnection(), SQL_FIND_COURSES_BY_LECTURER_ID, limit, offset, 1, lecturerId);
	}
	
	@Override
	protected CourseDto mapToEntity(ResultSet rs) {
		Course course = new Course();
		CourseDto courseHomePageDto = new CourseDto();
		try {
			course.setId(rs.getInt(FIELD_ID));
			course.setName(rs.getString(FIELD_COURSE_NAME));
			course.setStartDate(rs.getDate(FIELD_STARTDATE).toLocalDate());
			course.setEndDate(rs.getDate(FIELD_ENDDATE).toLocalDate());
			course.setStatus(CourseStatusEnum.valueOf(rs.getString(FIELD_STATUS).toUpperCase()));
			course.setLecturerId(rs.getInt(FIELD_LECTURER_ID));
			course.setTopicId(rs.getInt(FIELD_TOPIC_ID));

			courseHomePageDto.setCourse(course);

			courseHomePageDto.setTopic(rs.getString(FIELD_TOPIC_NAME));
			courseHomePageDto.setLecturer(rs.getString(FIELD_USER_NAME) + " " + rs.getString(FIELD_USER_SURNAME) + "  "
					+ rs.getString(FIELD_USER_PATRONYM));
			courseHomePageDto.setStudents(rs.getInt(FIELD_STUDENTS));
			courseHomePageDto.setDuration(Duration
					.between(course.getStartDate().atStartOfDay(), course.getEndDate().atStartOfDay()).toDays());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courseHomePageDto;
	}

	@Override
	protected boolean mapFromEntity(PreparedStatement ps, CourseDto obj) {
		// TODO Auto-generated method stub
		return false;
	}



}
