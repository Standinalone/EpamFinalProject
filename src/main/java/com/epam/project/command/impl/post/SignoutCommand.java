package com.epam.project.command.impl.post;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;

public class SignoutCommand implements ICommand{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		try {
			response.sendRedirect(Constants.PAGE_HOME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
