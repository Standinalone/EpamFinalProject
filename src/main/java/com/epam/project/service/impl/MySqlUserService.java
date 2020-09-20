package com.epam.project.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.ICourseDAO;
import com.epam.project.dao.ITopicDAO;
import com.epam.project.dao.IUserDAO;
import com.epam.project.dao.mysql.MySqlUserDAO;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.Topic;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.IUserService;

public class MySqlUserService implements IUserService {

	private static final String FIELD_LECTURER_ID = "lecturer_id";
	private static final String FIELD_TOPIC_ID = "topic_id";
	private static final String FIELD_GRADE = "grade";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_STARTDATE = "start_date";
	private static final String FIELD_ENDDATE = "end_date";
	private static final String FIELD_STATUS = "status";
	private static final String FIELD_COUNT = "COUNT(*)";
	private static final String FIELD_ROLE_ID = "role_id";

	private static final String SQL_GET_COURSES_FOR_USER = "SELECT * FROM Courses_has_users, Courses WHERE course_id = id and user_id = ?";
	private static final String SQL_GET_COURSES_WITH_USER_COUNT = "SELECT COUNT(*) FROM (" + SQL_GET_COURSES_FOR_USER
			+ " and registered = ?) as t";
	private static final String SQL_GET_ENROLLED_COURSES_FOR_USER_FROM_TO = SQL_GET_COURSES_FOR_USER + " and registered = 1 " + " LIMIT ? OFFSET ?";
	private static final String SQL_GET_NOT_ENROLLED_COURSES_FOR_USER_FROM_TO = SQL_GET_COURSES_FOR_USER + " and registered = 0 " + " LIMIT ? OFFSET ?";
	
	private static final String SQL_GET_ROLE_BY_USER = "SELECT Roles.name FROM Users, Roles WHERE role_id = roles.id and Users.id = ?";
	private static final String SQL_BLOCK_USER = "UPDATE Users SET blocked = true WHERE id = ?";
	private static final String SQL_UNBLOCK_USER = "UPDATE Users SET blocked = false WHERE id = ?";
	private static final String SQL_ENROLL_USER = "INSERT INTO Courses_has_users VALUES (?, ?, 0, false)";



	private static DaoFactory daoFactory;

	private static IUserDAO userDao;
	private static ITopicDAO topicDao;

	private static MySqlUserService instance;

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);

			topicDao = daoFactory.getTopicDAO();
			userDao = daoFactory.getUserDAO();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	private MySqlUserService() {
	}

	public static IUserService getInstance() {
		if (instance == null) {
			instance = new MySqlUserService();
		}
		return instance;
	}

	@Override
	public int getCoursesDTOCount(User user, boolean enrolled) {
		int count = 0;
		daoFactory.open();
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_GET_COURSES_WITH_USER_COUNT);
			ps.setInt(1, user.getId());
			ps.setBoolean(2, enrolled);
			ResultSet rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(FIELD_COUNT);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return count;
	}

	@Override
	public List<CourseProfilePageDto> getCoursesDTO(User user) {
		List<CourseProfilePageDto> coursesDTO = new ArrayList<>();
		daoFactory.open();
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_GET_COURSES_FOR_USER);
			ps.setInt(1, user.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				coursesDTO.add(mapToCourseDTO(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return coursesDTO;
	}

	@Override
	public List<CourseProfilePageDto> getNotEnrolledCoursesDTOFromTo(User user, int limit, int offset) {
		List<CourseProfilePageDto> coursesDTO = new ArrayList<>();
		daoFactory.open();
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_GET_NOT_ENROLLED_COURSES_FOR_USER_FROM_TO);
			ps.setInt(1, user.getId());
			ps.setInt(2, limit);
			ps.setInt(3, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				coursesDTO.add(mapToCourseDTO(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return coursesDTO;
	}

	private CourseProfilePageDto mapToCourseDTO(ResultSet rs) {
		CourseProfilePageDto courseDTO = new CourseProfilePageDto();
		try {
			courseDTO.setStartDate(rs.getDate(FIELD_STARTDATE).toLocalDate());
			LocalDate endDate = rs.getDate(FIELD_ENDDATE).toLocalDate();
			courseDTO.setEndDate(endDate);
			courseDTO.setCourseName(rs.getString(FIELD_NAME));
			courseDTO.setGrade(rs.getInt(FIELD_GRADE));
			CourseStatusEnum se;
			if (rs.getBoolean(FIELD_STATUS) && LocalDate.now().isAfter(endDate)) {
				se = CourseStatusEnum.FINISHED;
			} else {
				se = rs.getBoolean(FIELD_STATUS) ? CourseStatusEnum.IN_PROGRESS : CourseStatusEnum.NOT_STARTED;
			}
			courseDTO.setStatus(se);
			Topic topic = topicDao.findById(rs.getInt(FIELD_TOPIC_ID));
			if (topic != null) {
				courseDTO.setTopic(topic.getName());
			}
			User lecturer = userDao.findUserById(rs.getInt(FIELD_LECTURER_ID));
			if (lecturer != null) {
				courseDTO.setLecturer(lecturer.getSurname() + " " + lecturer.getName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courseDTO;
	}

	@Override
	public void blockUser(int id) {
		daoFactory.open();
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_BLOCK_USER);
			ps.setInt(1, id);
			ps.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
	}

	@Override
	public void unblockUser(int id) {
		daoFactory.open();
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_UNBLOCK_USER);
			ps.setInt(1, id);
			ps.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
	}

	@Override
	public void enrollToCourse(int course_id, int user_id) {
		daoFactory.open();
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_ENROLL_USER);
			ps.setInt(1, course_id);
			ps.setInt(2, user_id);
			ps.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
	}

	@Override
	public List<CourseProfilePageDto> getEnrolledCoursesDTOFromTo(User user, int limit, int offset) {
		List<CourseProfilePageDto> coursesDTO = new ArrayList<>();
		daoFactory.open();
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_GET_ENROLLED_COURSES_FOR_USER_FROM_TO);
			ps.setInt(1, user.getId());
			ps.setInt(2, limit);
			ps.setInt(3, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				coursesDTO.add(mapToCourseDTO(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return coursesDTO;
	}

}
