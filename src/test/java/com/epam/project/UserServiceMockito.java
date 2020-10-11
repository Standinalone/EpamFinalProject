package com.epam.project;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.mysql.MySqlUserDAO;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.service.impl.MySqlUserService;

public class UserServiceMockito {
	/**
	 * Stubs
	 */
	User user = new User();

	/**
	 * Mocks
	 */
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
	MySqlUserDAO userDao;

	MySqlUserService userService;

	@Before
	public void setup() throws SQLException {
		MockitoAnnotations.initMocks(this);
		userService = new MySqlUserService(daoFactory, userDao);

		// Configuring a stub
		user.setName("name");
		user.setSurname("surname");
		user.setPatronym("patronym");
		user.setPassword("123");
		user.setBlocked(false);
		user.setEmail("email");
		user.setId(0);
		user.setLogin("login");
		user.setRole(RoleEnum.USER);
		user.setEnabled(true);

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
	public void userServiceTransactions() throws SQLException {
		Map<Integer, Integer> userGrades = new HashMap<>();
		userGrades.put(0, 0);
		doReturn(connection).when(daoFactory).getConnection();
		Assert.assertTrue(userService.addUser(user));
		Assert.assertTrue(userService.deleteUserById(0));
		Assert.assertTrue(userService.updateUser(user));

		Assert.assertTrue(userService.declineRequestForIds(0, new String[] { "1" }));
		Assert.assertTrue(userService.registerInCourseByUsersIds(0, new String[] { "1" }, true));
		Assert.assertTrue(userService.updateGrades(0, userGrades));
		Assert.assertTrue(userService.enrollToCourse(new String[] { "1" }, 0));
		Assert.assertTrue(userService.blockUsersById(new String[] {}));
		Assert.assertTrue(userService.unblockUsersById(new String[] {}));

		Mockito.verify(daoFactory, times(9)).beginTransation();
		Mockito.verify(daoFactory, times(9)).endTransaction();
		Mockito.verify(ps, times(7)).close();
		Mockito.verify(rs, times(1)).close();
	}

	@Test
	public void testCourseServiceTransactionsWithError() throws SQLException {
		doThrow(new SQLException()).when(daoFactory).beginTransation();
		Assert.assertFalse(userService.addUser(user));
		Assert.assertFalse(userService.deleteUserById(0));
		Assert.assertFalse(userService.updateUser(user));

		Assert.assertFalse(userService.declineRequestForIds(0, new String[] {}));
		Assert.assertFalse(userService.registerInCourseByUsersIds(0, new String[] {}, true));
		Assert.assertFalse(userService.updateGrades(0, new HashMap<>()));
		Assert.assertFalse(userService.enrollToCourse(new String[] {}, 0));
		Assert.assertFalse(userService.blockUsersById(new String[] {}));
		Assert.assertFalse(userService.unblockUsersById(new String[] {}));

		Mockito.verify(daoFactory, times(9)).beginTransation();
		Mockito.verify(daoFactory, times(9)).endTransaction();
		Mockito.verify(daoFactory, times(9)).rollback();
	}

	@Test
	public void testCourseService() throws SQLException {
		doReturn(connection).when(daoFactory).getConnection();
		Assert.assertEquals(1, userService.getCoursesCountForUser(0, true));
		Assert.assertEquals(1, userService.getUsersCount());
		Assert.assertEquals(1, userService.getUsersWithCourseCount(0, true));

		Assert.assertEquals(null, userService.findUserByEmail(""));
		Assert.assertEquals(null, userService.findUserById(0));
		Assert.assertEquals(null, userService.findUserByLogin(""));

		Assert.assertEquals(0, userService.findAllUsers().size());
		Assert.assertEquals(0, userService.findAllUsersByRole(0).size());

		Assert.assertEquals(0, userService.findAllUsersFromTo(0, 0).size());
		Assert.assertEquals(0, userService.findAllUsersWithCourseFromTo(0, 0, 0, true).size());

		Mockito.verify(daoFactory, times(10)).open();
		Mockito.verify(daoFactory, times(10)).close();
		Mockito.verify(ps, times(10)).close();
		Mockito.verify(rs, times(10)).close();
	}

	@Test
	public void testCourseServiceWithError() throws SQLException {
		doThrow(new SQLException()).when(daoFactory).open();
		doReturn(connection).when(daoFactory).getConnection();

		Assert.assertEquals(-1, userService.getCoursesCountForUser(0, true));
		Assert.assertEquals(-1, userService.getUsersCount());
		Assert.assertEquals(-1, userService.getUsersWithCourseCount(0, true));

		Assert.assertEquals(null, userService.findAllUsersFromTo(0, 0));
		Assert.assertEquals(null, userService.findAllUsersWithCourseFromTo(0, 0, 0, true));

		try {
			userService.findAllUsers();
		} catch (SQLException e) {

		}
		try {
			userService.findAllUsersByRole(0);
		} catch (SQLException e) {

		}
		try {
			userService.findUserByEmail("");
		} catch (SQLException e) {

		}
		try {
			userService.findUserById(0);
		} catch (SQLException e) {

		}
		try {
			userService.findUserByLogin("");
		} catch (SQLException e) {

		}

		Mockito.verify(daoFactory, times(10)).open();
		Mockito.verify(daoFactory, times(10)).close();
	}
}
