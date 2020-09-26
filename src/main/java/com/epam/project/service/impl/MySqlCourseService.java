package com.epam.project.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.ICourseDAO;
import com.epam.project.dao.ICourseDtoDAO;
import com.epam.project.dao.ICourseProfilePageDAO;
import com.epam.project.dto.CourseDto;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;

public class MySqlCourseService implements ICourseService {
	private static final Logger log = LoggerFactory.getLogger(MySqlCourseService.class);

	private static DaoFactory daoFactory;
	private static ICourseDAO courseDao;
	private static ICourseDtoDAO courseDtoDao;
	private static ICourseProfilePageDAO courseProfilePageDAO;

	private static MySqlCourseService instance;

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
			courseDao = daoFactory.getCourseDAO();
			courseDtoDao = daoFactory.getCourseDtoDAO();
			courseProfilePageDAO = daoFactory.getCourseProfilePageDAO();
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

	// Course Methods

	@Override
	public int getCoursesCount() {
		try {
			daoFactory.open();
			return courseDao.getCount();
		} catch (SQLException e) {
			log.error("Get courses count error", e);
			return 0;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<Course> findAllCourses() {
		try {
			daoFactory.open();
			return courseDao.findAll();
		} catch (SQLException e) {
			log.error("Getting courses error", e);
			return new ArrayList<>();
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public Course findCourseById(int courseId) {
		try {
			daoFactory.open();
			return courseDao.findById(courseId);
		} catch (SQLException e) {
			log.error("Getting course error", e);
			return null;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public boolean updateCourse(Course courseId) {
		try {
			daoFactory.open();
			return courseDao.update(courseId);
		} catch (SQLException e) {
			log.error("Get courses count error", e);
			return false;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public boolean deleteCourseById(int courseId) {
		try {
			daoFactory.open();
			return courseDao.delete(courseId);
		} catch (SQLException e) {
			log.error("Deleting course error", e);
			return false;
		} finally {
			daoFactory.close();
		}
	}
	
	// CourseDto Methods

	@Override
	public CourseDto getCourseDtoByCourseId(int courseId) {
		try {
			daoFactory.open();
			return courseDtoDao.findById(courseId);
		} catch (SQLException e) {
			log.error("Getting courses dto error", e);
			return null;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<CourseDto> findAllCoursesDtoFromTo(int limit, int offset) {
		try {
			daoFactory.open();
			return courseDtoDao.findAllFromTo(limit, offset);
		} catch (SQLException e) {
			log.error("Getting courses error", e);
			return new ArrayList<>();
		} finally {
			daoFactory.close();
		}
	}

	// CourseProfilePageDto Methods

	@Override
	public List<CourseProfilePageDto> findAllCoursesProfilePageFromTo(int limit, int offset, User user,
			boolean enrolled) {
		try {
			daoFactory.open();
			return courseProfilePageDAO.findAllFromTo(limit, offset, user, enrolled);
		} catch (SQLException e) {
			log.error("Getting courses error", e);
			return new ArrayList<>();
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public boolean addCourse(Course course) {
		try {
			daoFactory.open();
			return courseDao.add(course);
		} catch (SQLException e) {
			log.error("Adding course error", e);
			return false;
		} finally {
			daoFactory.close();
		}
	}


}
