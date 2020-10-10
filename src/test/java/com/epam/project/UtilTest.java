package com.epam.project;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntSupplier;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.ICourseDAO;
import com.epam.project.dao.ICourseDtoDAO;
import com.epam.project.dao.ICourseProfilePageDAO;
import com.epam.project.dao.mysql.MySqlUserDAO;
import com.epam.project.entity.Course;
import com.epam.project.service.impl.MySqlCourseService;
import com.epam.project.util.Page;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.*;

//@RunWith(MockitoJUnitRunner.class)
public class UtilTest {
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

//	@Mock
//	BiFunction<Integer, Integer, List<Course>> getList;
//	@Mock
//	IntSupplier getCount;
//
//	@Test
//	public void getCoursesPage() {
//		HttpServletRequest request = mock(HttpServletRequest.class);
//		when(getList.apply(anyInt(), anyInt())).thenReturn(new ArrayList<>());
//		when(getCount.getAsInt()).thenReturn(3);
//		Page<Course> page = new Page<>(request, getList, getCount);
//		assertEquals(0, page.getList().size());
//	}
//
//	@Test
//	public void getCoursesPageWithParameters() {
//		Page<Course> page;
//		HttpServletRequest request = mock(HttpServletRequest.class);
//		when(getList.apply(anyInt(), anyInt())).thenReturn(new ArrayList<>());
//		when(getCount.getAsInt()).thenReturn(3);
//		when(request.getParameter("pagenum")).thenReturn("1");
//		page = new Page<>("pagenum", "startIndex", "totalPages", request, getList, getCount);
//		assertEquals(0, page.getList().size());
//	}
//
//	@Test
//	public void getCoursesPageWithBadIndex() {
//		Page<Course> page;
//		HttpServletRequest request = mock(HttpServletRequest.class);
//		when(getList.apply(anyInt(), anyInt())).thenReturn(new ArrayList<>());
//		when(getCount.getAsInt()).thenReturn(3);
//		when(request.getParameter(anyString())).thenReturn("badIndex");
//		page = new Page<>("pagenum", "startIndex", "totalPages", request, getList, getCount);
//		assertEquals(null, page.getList());
//	}

	@Mock Connection connection;
	@Mock
	Course course;
	@Mock
	ICourseDAO courseDao;
	@Mock
	DaoFactory daoFactory;
	@Mock
	ICourseDtoDAO courseDtoDao;
	@Mock
	ICourseProfilePageDAO courseProfilePageDAO;
	@InjectMocks
	MySqlCourseService courseService;

	@Test
	public void getConditionQuery() throws SQLException {
		when(daoFactory.getConnection()).thenReturn(connection);
		courseService.addCourse(course);
	}
}
