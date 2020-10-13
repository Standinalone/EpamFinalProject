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
	public void test() throws SQLException {
		doReturn(connection).when(daoFactory).getConnection();
		Assert.assertTrue(courseService.addCourse(course));
		Assert.assertTrue(courseService.deleteCourseById(0));
		Assert.assertTrue(courseService.updateCourse(course));
		courseService.setLecturerForCoursesByLecturerId(0, new String[] {});

		Mockito.verify(daoFactory, times(4)).beginTransation();
		Mockito.verify(daoFactory, times(4)).endTransaction();
		Mockito.verify(ps, times(3)).close();
		Mockito.verify(rs, times(1)).close();
	}

	@Test
	public void testCourseServiceTransactionsWithError() throws SQLException {
		doThrow(new SQLException()).when(daoFactory).beginTransation();
		Assert.assertFalse(courseService.addCourse(course));
		Assert.assertFalse(courseService.deleteCourseById(0));
		courseService.setLecturerForCoursesByLecturerId(0, new String[] {});
		Assert.assertFalse(courseService.updateCourse(course));

		Mockito.verify(daoFactory, times(4)).beginTransation();
		Mockito.verify(daoFactory, times(4)).endTransaction();
		Mockito.verify(daoFactory, times(4)).rollback();
	}

	@Test
	public void testCourseService() throws SQLException {
		doReturn(connection).when(daoFactory).getConnection();
		Assert.assertEquals(1, courseService.getCoursesWithParametersCount(""));
		Assert.assertEquals(1, courseService.getCoursesCount());
		Assert.assertEquals(1, courseService.getCoursesWithLecturerCount(0));

		Assert.assertEquals(null, courseService.findCourseById(0));
		Assert.assertEquals(null, courseService.getCourseDtoByCourseId(0));

		Assert.assertEquals(0, courseService.findAllCourses().size());
		Assert.assertEquals(0, courseService.findAllCoursesDtoWithParametersFromTo(0, 0, "", "").size());
		Assert.assertEquals(0, courseService.findAllCoursesDtoByLecturerIdFromTo(0, 0, 0).size());
		Assert.assertEquals(0, courseService.findAllCoursesDto().size());
		Assert.assertEquals(0, courseService.findAllCoursesDtoByLecturerId(0).size());
		Assert.assertEquals(0, courseService.findAllCoursesDtoFromTo(0, 0).size());
		Assert.assertEquals(0, courseService.findAllCoursesProfilePageFromTo(0, 0, new User(), false).size());

		Mockito.verify(daoFactory, times(12)).open();
//		Mockito.verify(daoFactory, times(12)).daoFactory.close();
		Mockito.verify(ps, times(12)).close();
		Mockito.verify(rs, times(12)).close();
	}

	@Test
	public void testCourseServiceWithError() throws SQLException {
		doThrow(new SQLException()).when(daoFactory).open();
		doReturn(connection).when(daoFactory).getConnection();

		Assert.assertEquals(-1, courseService.getCoursesWithParametersCount(""));
		Assert.assertEquals(-1, courseService.getCoursesCount());
		Assert.assertEquals(-1, courseService.getCoursesWithLecturerCount(0));
		
		Assert.assertEquals(null, courseService.findAllCoursesDtoWithParametersFromTo(0, 0, "", ""));
		Assert.assertEquals(null, courseService.findAllCoursesDtoByLecturerIdFromTo(0, 0, 0));
		Assert.assertEquals(null, courseService.findAllCoursesDtoFromTo(0, 0));
		Assert.assertEquals(null, courseService.findAllCoursesProfilePageFromTo(0, 0, new User(), false));

		try {
			courseService.findAllCoursesDto();
		} catch (SQLException e) {

		}
		try {
			courseService.findCourseById(0);
		} catch (SQLException e) {

		}
		try {
			courseService.getCourseDtoByCourseId(0);
		} catch (SQLException e) {

		}
		try {
			courseService.findAllCourses();
		} catch (SQLException e) {

		}
		try {
			courseService.findAllCoursesDtoByLecturerId(0);
		} catch (SQLException e) {

		}

		Mockito.verify(daoFactory, times(12)).open();
//		Mockito.verify(daoFactory, times(12)).daoFactory.close();
	}
}
