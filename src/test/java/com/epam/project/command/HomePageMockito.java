package com.epam.project.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

import org.mockito.Mockito;

import org.junit.Test;
import org.mockito.InjectMocks;

import com.epam.project.command.impl.get.HomePageCommand;
import com.epam.project.constants.Constants;
import com.epam.project.exceptions.DBException;
import com.epam.project.exceptions.DBUserException;

public class HomePageMockito extends BaseCommandMockito {

	@InjectMocks
	HomePageCommand homePage;

	@Test
	public void testHomePage() throws DBException {
		String forward = homePage.execute(request, response);

		verify(request).setAttribute(eq("page"), any());
		verify(request).setAttribute(eq("lecturers"), any());
		verify(request).setAttribute(eq("topics"), any());
		verify(request).setAttribute(eq("statuses"), any());
		assertEquals(Constants.PAGE__HOME, forward);

	}

	@Test
	public void testHomePageWithError() throws DBException {
		Mockito.when(userService.findAllUsersByRole(anyInt())).thenThrow(DBUserException.class);
		String forward = null;
		try {
			forward = homePage.execute(request, response);
		} catch (DBException e) {

		}
		assertEquals(null, forward);
	}
}
