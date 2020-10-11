package com.epam.project.command;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import com.epam.project.command.impl.post.RegisterCommand;
import com.epam.project.constants.Constants;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;

public class RegisterCommandMockito extends BaseCommandMockito{

	
	/**
	 * Stub
	 */
	User user = new User();
	
	@InjectMocks
	RegisterCommand registerCommand;
	
	@Before
	public void setup() throws SQLException {
		super.setup();
		Mockito.when(session.getAttribute("user")).thenReturn(user);
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
		
		// Configuring mocks
		when(request.getParameter("username")).thenReturn("Login");
		when(request.getParameter("name")).thenReturn("Username");
		when(request.getParameter("surname")).thenReturn("Surname");
		when(request.getParameter("patronym")).thenReturn("Patronym");
		when(request.getParameter("email")).thenReturn("standinalone96@gmail.com");
		when(request.getParameter("passwordConfirmation")).thenReturn("asdQwe123");
		when(request.getParameter("password")).thenReturn("asdQwe123");
	}
	
	@Test
	public void testRegistration() {
		Mockito.when(userService.addUser(any())).thenReturn(true);
		Mockito.when(tokenService.addToken(any())).thenReturn(true);
		String redirect = registerCommand.execute(request, response);
		assertEquals(Constants.COMMAND__SUCCESS, redirect);
		
	}
}
