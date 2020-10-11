package com.epam.project.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

import java.sql.SQLException;
import org.mockito.Mockito;

import org.junit.Test;
import org.mockito.InjectMocks;

import com.epam.project.command.impl.get.HomePageCommand;
import com.epam.project.constants.Constants;

public class HomePageMockito extends BaseCommandMockito{

	@InjectMocks
	HomePageCommand homePage;
	
	@Test
	public void testHomePage() {
		String forward = homePage.execute(request, response);

		verify(request).setAttribute(eq("page"), any());
		verify(request).setAttribute(eq("lecturers"), any());
		verify(request).setAttribute(eq("topics"), any());
		verify(request).setAttribute(eq("statuses"), any());
		assertEquals(Constants.PAGE__HOME, forward);

	}

	@Test
	public void testHomePageWithError() throws SQLException {
		Mockito.when(userService.findAllUsersByRole(anyInt())).thenThrow(SQLException.class);
		String forward = homePage.execute(request, response);
		assertEquals(Constants.PAGE__ERROR, forward);
	}

}
