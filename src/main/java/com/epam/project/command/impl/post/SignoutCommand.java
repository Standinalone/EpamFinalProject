package com.epam.project.command.impl.post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;

public class SignoutCommand implements ICommand{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession() != null)
			request.getSession().invalidate();
		return Constants.PAGE_HOME;
	}

}
