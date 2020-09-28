package com.epam.project.command.impl.post;

import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.l10n.Localization;

public class ChangeLanguageCommand implements ICommand {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String newLanguage = request.getParameter("newLanguage");
		String page = request.getParameter("page");
		String localesValues =  request.getServletContext().getInitParameter("locales");
		
		StringTokenizer st = new StringTokenizer(localesValues);
		while (st.hasMoreTokens()) {
			String localeName = st.nextToken();
			if (localeName.equals(newLanguage)) {
				Cookie cookie = new Cookie("language", newLanguage);
				response.addCookie(cookie);
				request.getSession().setAttribute("locale", new Locale(newLanguage));
				request.getSession().setAttribute("localization", new Localization(new Locale(newLanguage)));
				break;
			}
		}
		return "?" + (page == null ? "" : page);
	}

}
