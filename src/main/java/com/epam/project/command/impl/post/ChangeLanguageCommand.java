package com.epam.project.command.impl.post;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;

public class ChangeLanguageCommand implements ICommand {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String newLanguage = request.getParameter("newLanguage");
		String page = request.getParameter("page");
		Cookie cookie = new Cookie("language", newLanguage);
		response.addCookie(cookie);
		return request.getRequestURI() + "?command=" + page;
	}

}
