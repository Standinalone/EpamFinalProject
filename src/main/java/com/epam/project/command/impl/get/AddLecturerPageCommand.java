package com.epam.project.command.impl.get;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.exceptions.DBException;

/**
 * ICommand implementation for getting a page to add a lecturer
 *
 */
public class AddLecturerPageCommand implements ICommand {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
		return Constants.PAGE__ADD_LECTURER;
	}

}
