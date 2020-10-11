package com.epam.project.service.impl;

import java.sql.SQLException;
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

	private DaoFactory daoFactory;
	private ICourseDAO courseDao;
	private ICourseDtoDAO courseDtoDao;
	private ICourseProfilePageDAO courseProfilePageDAO;

	private static MySqlCourseService instance;

//	static {
//		try {
//			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
//			courseDao = daoFactory.getCourseDAO();
//			courseDtoDao = daoFactory.getCourseDtoDAO();
//			courseProfilePageDAO = daoFactory.getCourseProfilePageDAO();
//		} catch (DatabaseNotSupportedException e) {
//			log.error("DatabaseNotSupportedException", e.getMessage());
//		}
//	}

	// Course Methods

	@Override
	public int getCoursesWithParametersCount(String conditions) {
		try {
			daoFactory.open();
			return courseDao.getCountWithParameters(conditions);
		} catch (SQLException e) {
			log.error("Get courses count error", e.getMessage());
			return -1;
		} finally {
			daoFactory.close();
		}
	}

	/**
	 * Constructor used in testing by Mockito
	 * 
	 * @param daoFactory           mocked DaoFactory
	 * @param courseDao            mocked ICourseDAO
	 * @param courseProfilePageDAO mocked ICourseProfilePageDAO
	 * @param courseDtoDao         mocked ICourseDtoDAO
	 */
	public MySqlCourseService(DaoFactory daoFactory, ICourseDAO courseDao, ICourseProfilePageDAO courseProfilePageDAO,
			ICourseDtoDAO courseDtoDao) {
		this.daoFactory = daoFactory;
		this.courseDao = courseDao;
		this.courseDtoDao = courseDtoDao;
		this.courseProfilePageDAO = courseProfilePageDAO;
//		log.debug(String.valueOf(this.hashCode()));
	}

	private MySqlCourseService() {
		try {
			daoFactory = DaoFactory.getDaoFactory(DatabaseEnum.MYSQL);
			courseDao = daoFactory.getCourseDAO();
			courseDtoDao = daoFactory.getCourseDtoDAO();
			courseProfilePageDAO = daoFactory.getCourseProfilePageDAO();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	public static ICourseService getInstance() {
		if (instance == null) {
			instance = new MySqlCourseService();
		}
		return instance;
	}

	@Override
	public int getCoursesCount() {
		try {
			daoFactory.open();
			return courseDao.getCount();
		} catch (SQLException e) {
			log.error("Get courses count error", e.getMessage());
			return -1;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<Course> findAllCourses() throws SQLException {
		try {
			daoFactory.open();
			return courseDao.findAll();
		} catch (SQLException e) {
			log.error("Getting courses error", e.getMessage());
			throw new SQLException("asd");
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public Course findCourseById(int courseId) throws SQLException {
		try {
			daoFactory.open();
			return courseDao.findById(courseId);
		} catch (SQLException e) {
			log.error("Getting course error", e.getMessage());
			throw new SQLException();
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public boolean updateCourse(Course courseId) {
		try {
			daoFactory.beginTransation();
			courseDao.update(courseId);
			daoFactory.getConnection().commit();
			return true;
		} catch (SQLException e) {
			log.error("Updating course error", e.getMessage());
			daoFactory.rollback();
			return false;
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public boolean deleteCourseById(int courseId) {
		try {
			daoFactory.beginTransation();
			courseDao.delete(courseId);
			daoFactory.getConnection().commit();
			return true;
		} catch (SQLException e) {
			log.error("Deleting course error", e.getMessage());
			daoFactory.rollback();
			return false;
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public boolean setLecturerForCoursesByLecturerId(int lecturerId, String[] checkedIds) {
		try {
			daoFactory.beginTransation();
			for (String id : checkedIds) {
				Course course = courseDao.findById(Integer.parseInt(id));
				course.setLecturerId(lecturerId);
				courseDao.update(course);
			}
			daoFactory.getConnection().commit();
			return true;
		} catch (SQLException e) {
			log.error("Editing courses error", e.getMessage());
			daoFactory.rollback();
			return false;
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public boolean addCourse(Course course) {
		try {
			daoFactory.beginTransation();
			courseDao.add(course);
			daoFactory.getConnection().commit();
			return true;
		} catch (SQLException e) {
			log.error("Adding course error", e.getMessage());
			daoFactory.rollback();
			return false;
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public int getCoursesWithLecturerCount(int lecturerId) {
		try {
			daoFactory.open();
			return courseDao.getCountByLecturerId(lecturerId);
		} catch (SQLException e) {
			log.error("Get courses count error", e.getMessage());
			return -1;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<CourseDto> findAllCoursesDtoWithParametersFromTo(int limit, int offset, String conditions,
			String orderBy) {
		try {
			daoFactory.open();
			return courseDtoDao.findAllFromToWithParameters(limit, offset, conditions, orderBy);
		} catch (SQLException e) {
			log.error("Getting courses error", e.getMessage());
			return null;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<CourseDto> findAllCoursesDtoByLecturerIdFromTo(int lecturerId, int limit, int offset) {
		try {
			daoFactory.open();
			return courseDtoDao.findByLecturerIdFromTo(lecturerId, limit, offset);
		} catch (SQLException e) {
			log.error("Getting courses error", e.getMessage());
			return null;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<CourseDto> findAllCoursesDto() throws SQLException {
		try {
			daoFactory.open();
			return courseDtoDao.findAll();
		} catch (SQLException e) {
			log.error("Getting courses dto error", e.getMessage());
			throw new SQLException();
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<CourseDto> findAllCoursesDtoByLecturerId(int userId) throws SQLException {
		try {
			daoFactory.open();
			return courseDtoDao.findByLecturerId(userId);
		} catch (SQLException e) {
			log.error("Getting courses dto error", e.getMessage());
			throw new SQLException();
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public CourseDto getCourseDtoByCourseId(int courseId) throws SQLException {
		try {
			daoFactory.open();
			return courseDtoDao.findById(courseId);
		} catch (SQLException e) {
			log.error("Getting courses dto error", e.getMessage());
			throw new SQLException();
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
			log.error("Getting courses dto error", e.getMessage());
			return null;
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<CourseProfilePageDto> findAllCoursesProfilePageFromTo(int limit, int offset, User user,
			boolean enrolled) {
		try {
			daoFactory.open();
			return courseProfilePageDAO.findAllFromTo(limit, offset, user, enrolled);
		} catch (SQLException e) {
			log.error("Getting courses error", e.getMessage());
			return null;
		} finally {
			daoFactory.close();
		}
	}

}
