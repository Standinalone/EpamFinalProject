package com.epam.project.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.epam.project.exceptions.DBCourseException;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;

public class MySqlCourseService implements ICourseService {
	private static final Logger log = LoggerFactory.getLogger(MySqlCourseService.class);

	private DaoFactory daoFactory;
	private ICourseDAO courseDao;
	private ICourseDtoDAO courseDtoDao;
	private ICourseProfilePageDAO courseProfilePageDAO;

	private static MySqlCourseService instance;

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
	public int findAllCoursesWithParametersCount(String conditions) throws DBCourseException {
		try {
			daoFactory.open();
			return courseDao.getCountWithParameters(conditions);
		} catch (SQLException e) {
			throw new DBCourseException("dberror.course.findAll", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public int getCoursesCount() throws DBCourseException {
		try {
//			throw new SQLException();
			daoFactory.open();
			return courseDao.getCount();
		} catch (SQLException e) {
			throw new DBCourseException("dberror.course.getCount", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<Course> findAllCourses() throws DBCourseException {
		try {
			daoFactory.open();
			return courseDao.findAll();
		} catch (SQLException e) {
			throw new DBCourseException("dberror.course.findAll", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public Course findCourseById(int courseId) throws DBCourseException {
		try {
			daoFactory.open();
			return courseDao.findById(courseId);
		} catch (SQLException e) {
			throw new DBCourseException("dberror.course.get", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public void updateCourse(Course courseId) throws DBCourseException {
		try {
			daoFactory.beginTransation();
			courseDao.update(courseId);
			daoFactory.getConnection().commit();
		} catch (SQLException e) {
			daoFactory.rollback();
			throw new DBCourseException("dberror.course.update", e);
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public void deleteCourseById(int courseId) throws DBCourseException {
		try {
			daoFactory.beginTransation();
			courseDao.delete(courseId);
			daoFactory.getConnection().commit();
		} catch (SQLException e) {
			daoFactory.rollback();
			throw new DBCourseException("dberror.course.delete", e);
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public void setLecturerForCoursesByLecturerId(int lecturerId, String[] checkedIds) throws DBCourseException {
		try {
			daoFactory.beginTransation();
			for (String id : checkedIds) {
				Course course = courseDao.findById(Integer.parseInt(id));
				course.setLecturerId(lecturerId);
				courseDao.update(course);
			}
			daoFactory.getConnection().commit();
		} catch (SQLException e) {
			daoFactory.rollback();
			throw new DBCourseException("dberror.course.update", e);
		} finally {
			daoFactory.endTransaction();
		}
	}
	private Set<String> idempotencyList = new HashSet<>();
	private boolean checkIdempotencyId(String id) {
		if (idempotencyList.contains(id))
			return false;
		if (idempotencyList.size() > 200) {
			idempotencyList = new HashSet<>();
		}
		return true;
	}
	@Override
	public void addCourse(Course course) throws DBCourseException {
		if (course.getIdempotencyId() == null)
			throw new DBCourseException("dberror.course.add");
		try {
			daoFactory.beginTransation();
			//courseDao.findByIdempotencyId(course.getIdempotencyId());
			if (!checkIdempotencyId(course.getIdempotencyId()))
				throw new DBCourseException("dberror.course.exists");
			courseDao.add(course);
			idempotencyList.add(course.getIdempotencyId());
			daoFactory.getConnection().commit();
		} catch (SQLException e) {
			daoFactory.rollback();
			throw new DBCourseException("dberror.course.add", e);
		} finally {
			daoFactory.endTransaction();
		}
	}

	@Override
	public int getCoursesWithLecturerCount(int lecturerId) throws DBCourseException {
		try {
			daoFactory.open();
			return courseDao.getCountByLecturerId(lecturerId);
		} catch (SQLException e) {
			throw new DBCourseException("dberror.course.getCount", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<CourseDto> findAllCoursesDtoWithParametersFromTo(int limit, int offset, String conditions,
			String orderBy) throws DBCourseException {
		return findAllCoursesDtoWithParametersFromTo(limit, offset, conditions, orderBy, 0);
	}

	@Override
	public List<CourseDto> findAllCoursesDtoWithParametersFromTo(int limit, int offset, String conditions,
			String orderBy, int userId) throws DBCourseException {
		try {
			daoFactory.open();
			return courseDtoDao.findAllFromToWithParameters(limit, offset, conditions, orderBy, userId);
		} catch (SQLException e) {
			throw new DBCourseException("dberror.course.findAll", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<CourseDto> findAllCoursesDtoByLecturerIdFromTo(int lecturerId, int limit, int offset)
			throws DBCourseException {
		try {
			daoFactory.open();
			return courseDtoDao.findByLecturerIdFromTo(lecturerId, limit, offset);
		} catch (SQLException e) {
			throw new DBCourseException("dberror.course.findAll", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<CourseDto> findAllCoursesDto() throws DBCourseException {
		try {
			daoFactory.open();
			return courseDtoDao.findAll();
		} catch (SQLException e) {
			throw new DBCourseException("dberror.course.findAll", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<CourseDto> findAllCoursesDtoByLecturerId(int userId) throws DBCourseException {
		try {
			daoFactory.open();
			return courseDtoDao.findByLecturerId(userId);
		} catch (SQLException e) {
			throw new DBCourseException("dberror.course.findAll", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public CourseDto getCourseDtoByCourseId(int courseId) throws DBCourseException {
		try {
			daoFactory.open();
			return courseDtoDao.findById(courseId);
		} catch (SQLException e) {
			throw new DBCourseException("dberror.course.get", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<CourseDto> findAllCoursesDtoFromTo(int limit, int offset) throws DBCourseException {
		try {
			daoFactory.open();
			return courseDtoDao.findAllFromTo(limit, offset);
		} catch (SQLException e) {
			throw new DBCourseException("dberror.course.findAll", e);
		} finally {
			daoFactory.close();
		}
	}

	@Override
	public List<CourseProfilePageDto> findAllCoursesProfilePageFromTo(int limit, int offset, User user,
			boolean enrolled) throws DBCourseException {
		try {
			daoFactory.open();
			return courseProfilePageDAO.findAllFromTo(limit, offset, user, enrolled);
		} catch (SQLException e) {
			throw new DBCourseException("dberror.course.findAll", e);
		} finally {
			daoFactory.close();
		}
	}

}
