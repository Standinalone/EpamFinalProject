package com.epam.project;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.mysql.MySqlCourseDAO;
import com.epam.project.dao.mysql.MySqlCourseDtoDAO;
import com.epam.project.dao.mysql.MySqlCourseProfilePageDAO;
import com.epam.project.dto.CourseDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DBCourseException;
import com.epam.project.exceptions.DBException;
import com.epam.project.service.impl.MySqlCourseService;

public class CourseServiceMockito {

	/**
	 * Stubs
	 */
	Course course = new Course();

	/**
	 * Mocks
	 */
	@Mock
	CourseDto courseDto = new CourseDto();
	@Mock
	PreparedStatement ps;
	@Mock
	ResultSet rs;
	@Mock
	Connection connection;
	@Mock
	DaoFactory daoFactory;

	/**
	 * DaoFactory will be injected to subsequent classes
	 */
	@InjectMocks
	MySqlCourseDAO courseDao;
	@InjectMocks
	MySqlCourseDtoDAO courseDtoDao;
	@InjectMocks
	MySqlCourseProfilePageDAO courseProfilePageDAO;

	MySqlCourseService courseService;

	@Before
	public void setup() throws SQLException {
		MockitoAnnotations.initMocks(this);
		courseService = new MySqlCourseService(daoFactory, courseDao, courseProfilePageDAO, courseDtoDao);

		// Configuring a stub
		course.setName("name");
		course.setId(1);
		course.setEndDate(LocalDate.now());
		course.setStartDate(LocalDate.now());
		course.setLecturerId(1);
		course.setStatus(CourseStatusEnum.FINISHED);
		course.setTopicId(1);

		// Configuring the mocks
		doReturn(ps).when(connection).prepareStatement(anyString());
		doReturn(ps).when(connection).prepareStatement(anyString(), anyInt());
		doReturn(rs).when(ps).executeQuery();
		doReturn(rs).when(ps).getGeneratedKeys();
		Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
		doReturn(1).when(rs).getInt(1);
		doReturn(1).when(ps).executeUpdate();
		doReturn(Date.valueOf(LocalDate.now())).when(rs).getDate(anyString());
		doReturn("FINISHED").when(rs).getString("statuses.name");
	}



	@Test
	public void test() throws SQLException, DBCourseException {
		doReturn(connection).when(daoFactory).getConnection();
		courseService.addCourse(course);
		courseService.deleteCourseById(0);
		courseService.updateCourse(course);
		courseService.setLecturerForCoursesByLecturerId(0, new String[] {});

		Mockito.verify(daoFactory, times(4)).beginTransation();
		Mockito.verify(daoFactory, times(4)).endTransaction();
		Mockito.verify(ps, times(3)).close();
		Mockito.verify(rs, times(1)).close();
	}

	@Test
	public void testCourseServiceTransactionsWithError() throws SQLException, DBCourseException {
		doThrow(new SQLException()).when(daoFactory).beginTransation();
		try {
			courseService.addCourse(course);
		} catch (DBException e) {

		}
		try {
			courseService.deleteCourseById(0);
		} catch (DBException e) {

		}
		try {
			courseService.setLecturerForCoursesByLecturerId(0, new String[] {});
		} catch (DBException e) {

		}
		try {
			courseService.updateCourse(course);
		} catch (DBException e) {

		}

		Mockito.verify(daoFactory, times(4)).beginTransation();
		Mockito.verify(daoFactory, times(4)).endTransaction();
		Mockito.verify(daoFactory, times(4)).rollback();
	}

	@Test
	public void testCourseService() throws DBCourseException, SQLException {
		doReturn(connection).when(daoFactory).getConnection();
		Assert.assertEquals(1, courseService.findAllCoursesWithParametersCount(""));
		Assert.assertEquals(1, courseService.getCoursesCount());
		Assert.assertEquals(1, courseService.getCoursesWithLecturerCount(0));

		try {
			courseService.findCourseById(0);
		} catch (DBCourseException e) {

		}
		try {
			courseService.getCourseDtoByCourseId(0);
		} catch (DBCourseException e) {

		}

		try {
			courseService.findAllCourses().size();
		} catch (DBCourseException e) {

		}
		try {
			courseService.findAllCoursesDtoWithParametersFromTo(0, 0, "", "").size();
		} catch (DBCourseException e) {

		}
		try {
			courseService.findAllCoursesDtoByLecturerIdFromTo(0, 0, 0).size();
		} catch (DBCourseException e) {

		}
		try {
			courseService.findAllCoursesDto().size();
		} catch (DBCourseException e) {

		}
		try {
			courseService.findAllCoursesDtoByLecturerId(0).size();
		} catch (DBCourseException e) {

		}
		try {
			courseService.findAllCoursesDtoFromTo(0, 0).size();
		} catch (DBCourseException e) {

		}
		try {
			courseService.findAllCoursesProfilePageFromTo(0, 0, new User(), false).size();
		} catch (DBCourseException e) {

		}

		Mockito.verify(daoFactory, times(12)).open();
//		Mockito.verify(daoFactory, times(12)).daoFactory.close();
		Mockito.verify(ps, times(12)).close();
		Mockito.verify(rs, times(12)).close();
	}

	@Test
	public void testCourseServiceWithError() throws SQLException, DBCourseException {
		doThrow(new SQLException()).when(daoFactory).open();
		doReturn(connection).when(daoFactory).getConnection();

		try {
			Assert.assertEquals(-1, courseService.findAllCoursesWithParametersCount(""));
		} catch (DBException e) {

		}
		try {
			Assert.assertEquals(-1, courseService.getCoursesCount());
		} catch (DBException e) {

		}
		try {
			Assert.assertEquals(-1, courseService.getCoursesWithLecturerCount(0));
		} catch (DBException e) {

		}
		try {
			Assert.assertEquals(null, courseService.findAllCoursesDtoWithParametersFromTo(0, 0, "", ""));
		} catch (DBException e) {

		}
		try {
			Assert.assertEquals(null, courseService.findAllCoursesDtoByLecturerIdFromTo(0, 0, 0));
		} catch (DBException e) {

		}
		try {
			Assert.assertEquals(null, courseService.findAllCoursesDtoFromTo(0, 0));
		} catch (DBException e) {

		}
		try {
			Assert.assertEquals(null, courseService.findAllCoursesProfilePageFromTo(0, 0, new User(), false));
		} catch (DBException e) {

		}

		try {
			courseService.findAllCoursesDto();
		} catch (DBCourseException e) {

		}
		try {
			courseService.findCourseById(0);
		} catch (DBCourseException e) {

		}
		try {
			courseService.getCourseDtoByCourseId(0);
		} catch (DBCourseException e) {

		}
		try {
			courseService.findAllCourses();
		} catch (DBCourseException e) {

		}
		try {
			courseService.findAllCoursesDtoByLecturerId(0);
		} catch (DBCourseException e) {

		}

		Mockito.verify(daoFactory, times(12)).open();
		Mockito.verify(daoFactory, times(12)).close();
	}
}
