package com.epam.project.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.GenericDAO;
import com.epam.project.dao.ICourseDtoDAO;
import com.epam.project.dto.CourseDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public class MySqlCourseDtoDAO extends GenericDAO<CourseDto> implements ICourseDtoDAO {
	private static final Logger log = LoggerFactory.getLogger(MySqlCourseDtoDAO.class);
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
	private static final String FIELD_DURATION = "duration";
	private static final String FIELD_IN_COURSE = "in_course";

	private static final String SQL_FIND_COURSES_FROM_TO = "SELECT *, datediff(end_date, start_date) as duration FROM Courses, (SELECT Courses.id, COALESCE(students, 0) AS `students` FROM Courses LEFT JOIN (SELECT course_id, COUNT(*) AS 'students'  FROM Courses_has_users GROUP BY course_id) AS t1 ON Courses.id = t1.course_id) AS t, Statuses, Users, Topics WHERE Courses.id = t.id AND Courses.lecturer_id = Users.id AND Courses.topic_id = Topics.id AND Courses.status_id = Statuses.id";
	private static final String SQL_FIND_COURSES_WITH_INCOURSE_COLUMN_FROM_TO = "SELECT *, IF(? in (SELECT user_id FROM Courses_has_users WHERE course_id = Courses.id), true, false) AS in_course, datediff(end_date, start_date) as duration FROM Courses, (SELECT Courses.id, COALESCE(students, 0) AS `students` FROM Courses LEFT JOIN (SELECT course_id, COUNT(*) AS 'students'  FROM Courses_has_users GROUP BY course_id) AS t1 ON Courses.id = t1.course_id) AS t, Statuses, Users, Topics WHERE Courses.id = t.id AND Courses.lecturer_id = Users.id AND Courses.topic_id = Topics.id AND Courses.status_id = Statuses.id";
	private static final String SQL_FIND_COURSE_BY_ID = "SELECT *, datediff(end_date, start_date) as duration  FROM Courses, (SELECT Courses.id, COALESCE(students, 0) AS `students` FROM Courses LEFT JOIN (SELECT course_id, COUNT(*) AS 'students' FROM Courses_has_users GROUP BY course_id) AS t1 ON Courses.id = t1.course_id) AS t, Statuses, Users, Topics WHERE Courses.id = t.id AND Courses.lecturer_id = Users.id AND Courses.topic_id = Topics.id AND Courses.status_id = Statuses.id AND Courses.id = ?";
	private static final String SQL_FIND_COURSES_BY_LECTURER_ID = "SELECT *, datediff(end_date, start_date) as duration  FROM Courses, (SELECT Courses.id, COALESCE(students, 0) AS `students` FROM Courses LEFT JOIN (SELECT course_id, COUNT(*) AS 'students' FROM Courses_has_users GROUP BY course_id) AS t1 ON Courses.id = t1.course_id) AS t, Statuses, Users, Topics WHERE Courses.id = t.id AND Courses.lecturer_id = Users.id AND Courses.topic_id = Topics.id AND Courses.status_id = Statuses.id AND Courses.lecturer_id = ?";
	private static final String SQL_FIND_ALL = "SELECT *, datediff(end_date, start_date) as duration  FROM Courses, (SELECT Courses.id, COALESCE(students, 0) AS `students` FROM Courses LEFT JOIN (SELECT course_id, COUNT(*) AS 'students' FROM Courses_has_users GROUP BY course_id) AS t1 ON Courses.id = t1.course_id) AS t, Statuses, Users, Topics WHERE Courses.id = t.id AND Courses.lecturer_id = Users.id AND Courses.topic_id = Topics.id AND Courses.status_id = Statuses.id";


	private DaoFactory daoFactory;
	private static MySqlCourseDtoDAO instance;

	private MySqlCourseDtoDAO() {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

//	static {
//		try {
//			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
//		} catch (DatabaseNotSupportedException e) {
//			log.error("DatabaseNotSupportedException", e.getMessage());
//		}
//	}
	
	/**
	 * Constructor for Mockito testing
	 * 
	 * @param daoFactory
	 */
	private MySqlCourseDtoDAO(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
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
		if (list.isEmpty())
			throw new SQLException();
		return list.get(0);
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
		return findByFieldFromTo(daoFactory.getConnection(), SQL_FIND_COURSES_BY_LECTURER_ID, limit, offset, 1,
				lecturerId);
	}

	@Override
	public List<CourseDto> findAllFromToWithParameters(int limit, int offset, String conditions, String orderBy, int userId)
			throws SQLException {
		String appendix = (conditions.length() == 0 ? "" : " AND " + conditions);
		return findByFieldFromTo(daoFactory.getConnection(), SQL_FIND_COURSES_WITH_INCOURSE_COLUMN_FROM_TO + appendix + " " + orderBy, limit, offset, 1, userId);
//		return findFromTo(daoFactory.getConnection(), SQL_FIND_COURSES_FROM_TO + appendix + " " + orderBy, limit,
//				offset);
	}

	@Override
	protected CourseDto mapToEntity(ResultSet rs) throws SQLException {
		Course course = new Course();
		CourseDto courseHomePageDto = new CourseDto();
		course.setId(rs.getInt(FIELD_ID));
		course.setName(rs.getString(FIELD_COURSE_NAME));
		course.setStartDate(rs.getDate(FIELD_STARTDATE).toLocalDate());
		course.setEndDate(rs.getDate(FIELD_ENDDATE).toLocalDate());
		course.setStatus(CourseStatusEnum.valueOf(rs.getString(FIELD_STATUS).toUpperCase()));
		course.setLecturerId(rs.getInt(FIELD_LECTURER_ID));
		course.setTopicId(rs.getInt(FIELD_TOPIC_ID));

		courseHomePageDto.setCourse(course);
		try {
			courseHomePageDto.setInCourse(rs.getBoolean(FIELD_IN_COURSE));
		}
		catch(SQLException e) {
			log.info("Field `in_course` ignored");
		}

		courseHomePageDto.setTopic(rs.getString(FIELD_TOPIC_NAME));
		courseHomePageDto.setLecturer(rs.getString(FIELD_USER_NAME) + " " + rs.getString(FIELD_USER_SURNAME) + "  "
				+ rs.getString(FIELD_USER_PATRONYM));
		courseHomePageDto.setStudents(rs.getInt(FIELD_STUDENTS));
//			courseHomePageDto.setDuration(Duration
//					.between(course.getStartDate().atStartOfDay(), course.getEndDate().atStartOfDay()).toDays());
		courseHomePageDto.setDuration(rs.getInt(FIELD_DURATION));

		return courseHomePageDto;
	}

	@Override
	protected void mapFromEntity(PreparedStatement ps, CourseDto obj) {
		// TODO Auto-generated method stub
	}

}
