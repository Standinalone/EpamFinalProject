package com.epam.project.command.impl.post;

import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.exceptions.DBException;

/**
 * ICommand implementation for `change language` command
 *
 */
public class ChangeLanguageCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(ChangeLanguageCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
		String newLanguage = request.getParameter("newLanguage");
		String page = request.getParameter("page");
		String localesValues = request.getServletContext().getInitParameter("locales");
		if (localesValues == null) {
			log.error("Parameter `localesValues` is empty");
			return "?" + (page == null ? "" : page);
		}
		StringTokenizer st = new StringTokenizer(localesValues);
		while (st.hasMoreTokens()) {
			String localeName = st.nextToken();
			if (localeName.equals(newLanguage)) {
				Cookie cookie = new Cookie("language", newLanguage);
				response.addCookie(cookie);
				request.getSession().setAttribute("locale", new Locale(newLanguage));
				log.info("Language changed to {}",newLanguage);
				break;
			}
		}
		return "?" + (page == null ? "" : page);
	}

}
