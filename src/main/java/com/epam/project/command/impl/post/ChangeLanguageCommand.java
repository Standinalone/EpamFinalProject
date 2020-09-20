package com.epam.project.command.impl.post;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;

public class ChangeLanguageCommand implements ICommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String newLanguage = request.getParameter("newLanguage");
		String page = request.getParameter("page");
		Cookie cookie = new Cookie("language", newLanguage);
		response.addCookie(cookie);
		String url = request.getRequestURI();
		try {
			response.sendRedirect(url + "?command=" + page);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
