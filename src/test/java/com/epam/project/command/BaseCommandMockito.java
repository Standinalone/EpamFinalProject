package com.epam.project.command;

import java.sql.SQLException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.epam.project.service.ICourseService;
import com.epam.project.service.ITokenService;
import com.epam.project.service.ITopicService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

public abstract class BaseCommandMockito {
	@Mock
	HttpSession session;
	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	@Mock
	ServiceFactory serviceFactory;
	@Mock
	ICourseService courseService;
	@Mock
	IUserService userService;
	@Mock
	ITopicService topicService;
	@Mock
	ITokenService tokenService;
	
	@Before
	public void setup() throws SQLException {
		MockitoAnnotations.initMocks(this);
		Mockito.when(request.getSession()).thenReturn(session);
		Mockito.when(session.getAttribute("locale")).thenReturn(new Locale("en"));
	}
}
