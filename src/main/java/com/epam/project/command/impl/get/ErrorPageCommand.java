package com.epam.project.command.impl.get;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;

/**
 * ICommand implementation for getting an error page
 *
 */
public class ErrorPageCommand implements ICommand{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		return Constants.PAGE__ERROR;
	}

}
