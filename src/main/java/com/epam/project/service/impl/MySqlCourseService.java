package com.epam.project.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.ICourseDAO;
import com.epam.project.dao.ITopicDAO;
import com.epam.project.dao.IUserDAO;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.dto.CourseHomePageDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.entity.Topic;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

public class MySqlCourseService implements ICourseService {
	private static final String SQL_GET_COURSES_FROM_TO = "SELECT * FROM Courses LIMIT ? OFFSET ?";
	private static final String SQL_GET_COURSES_COUNT = "SELECT COUNT(*) FROM (SELECT * FROM Courses) as t";
	private static final String SQL_GET_STUDENTS_COUNT_BY_ID = "SELECT COUNT(*) FROM Courses_has_users WHERE course_id = ?";
	
	private static final String FIELD_NAME = "name";
	private static final String FIELD_STARTDATE = "start_date";
	private static final String FIELD_ENDDATE = "end_date";
	private static final String FIELD_STATUS = "status";
	private static final String FIELD_COUNT = "COUNT(*)";
	private static final String FIELD_LECTURER_ID = "lecturer_id";
	private static final String FIELD_TOPIC_ID = "topic_id";
	private static final String FIELD_ID = "id";

	private static DaoFactory daoFactory;
	private static ServiceFactory serviceFactory;
	private static ICourseDAO courseDao;
	private static IUserDAO userDao;
	private static ITopicDAO topicDao;
	private static ICourseService courseService;

	private static MySqlCourseService instance;

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
			serviceFactory = ServiceFactory.getServiceFactory(DatabaseEnum.MYSQL);
			courseService = serviceFactory.getCourseService();
			
			courseDao = daoFactory.getCourseDAO();
			userDao = daoFactory.getUserDAO();
			topicDao = daoFactory.getTopicDAO();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	private MySqlCourseService() {
	}

	public static ICourseService getInstance() {
		if (instance == null) {
			instance = new MySqlCourseService();
		}
		return instance;
	}

	@Override
	public List<CourseHomePageDto> getCoursesInfoDTOFromTo(int limit, int offset) {
		List<CourseHomePageDto> courseInfoDTO = new ArrayList<>();
		daoFactory.open();
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_GET_COURSES_FROM_TO);
			ps.setInt(1, limit);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				courseInfoDTO.add(mapToCourseInfoDTO(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return courseInfoDTO;
	}

	private CourseHomePageDto mapToCourseInfoDTO(ResultSet rs) {
		CourseHomePageDto courseDto = new CourseHomePageDto();
		Course course = new Course();
		try {
			LocalDate endDate = rs.getDate(FIELD_ENDDATE).toLocalDate();
			LocalDate startDate = rs.getDate(FIELD_STARTDATE).toLocalDate();
			course.setName(rs.getString(FIELD_NAME));
			course.setStartDate(startDate);
			course.setEndDate(endDate);
			course.setId(rs.getInt(FIELD_ID));
			int studentsAmount = courseService.getStudentsCountById(course.getId());
			courseDto.setStudents(studentsAmount);
//			CourseStatusEnum se;
//			if (rs.getBoolean(FIELD_STATUS) && LocalDate.now().isAfter(endDate)) {
//				se = CourseStatusEnum.FINISHED;
//			} else {
//				se = rs.getBoolean(FIELD_STATUS) ? CourseStatusEnum.IN_PROGRESS : CourseStatusEnum.NOT_STARTED;
//			}
			//courseDto.setStatus(se);
			courseDto.setDuration(Duration.between( course.getStartDate().atStartOfDay(), course.getEndDate().atStartOfDay()).toDays());
			User lecturer = userDao.findUserById(rs.getInt(FIELD_LECTURER_ID));
			if (lecturer != null) {
				courseDto.setLecturer(lecturer.getSurname() + " " + lecturer.getName() + lecturer.getPatronym());
			}
			Topic topic = topicDao.findById(rs.getInt(FIELD_TOPIC_ID));
			if (topic != null) {
				courseDto.setTopic(topic.getName());
			}
			courseDto.setCourse(course);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courseDto;
	}

	@Override
	public int getCoursesCount() {
		int count = 0;
		daoFactory.open();
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_GET_COURSES_COUNT);
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
	public int getStudentsCountById(int id) {
		int count = 0;
		daoFactory.open();
		try {
			Connection connection = daoFactory.getConnection();
			PreparedStatement ps = connection.prepareStatement(SQL_GET_STUDENTS_COUNT_BY_ID);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(FIELD_COUNT);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		daoFactory.close();
		return count;
	}

}
