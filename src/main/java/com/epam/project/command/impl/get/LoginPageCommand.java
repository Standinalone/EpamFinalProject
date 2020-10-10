package com.epam.project.command.impl.get;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;

/**
 * ICommand implementation for getting a login page
 *
 */
public class LoginPageCommand implements ICommand{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		return Constants.PAGE__LOGIN;
	}

}
