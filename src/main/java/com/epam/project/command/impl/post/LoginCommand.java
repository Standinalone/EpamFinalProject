package com.epam.project.command.impl.post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.l10n.Localization;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

public class LoginCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(LoginCommand.class);
	public static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);

	public static IUserService userService;
	public static ServiceFactory serviceFactory;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public List<String> validate(HttpServletRequest request) {
		List<String> errors = new ArrayList<>();
		Localization localization = new Localization((Locale) request.getSession().getAttribute("locale"));
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		User user = userService.findUserByLogin(username);
		if (user == null) {
			errors.add(localization.getMessagesParam("login.error"));
			return errors;
		}
		if (!password.equals(user.getPassword())) {
			errors.add(localization.getMessagesParam("login.error"));
			return errors;
		}
		if (user.isBlocked()) {
			errors.add(localization.getMessagesParam("login.blocked"));
			return errors;
		}
		if (!user.isEnabled()) {
			errors.add(localization.getMessagesParam("login.enabled"));
			return errors;
		}
		request.getSession().setAttribute("user", user);
		return errors;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		List<String> errors = validate(request);
		if (!errors.isEmpty()) {
			log.error("Data is not valid for a user");
			log.debug("debug error");

			request.getSession().setAttribute("errors", errors);
			return Constants.PAGE_LOGIN;
		}
		request.setAttribute("errors", null);
		return Constants.PAGE_PROFILE;
	}
}