package com.epam.project.command.impl.post;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.i18n.Localization;
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
		Localization localization = (Localization) request.getSession().getAttribute("localization");
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		User user = userService.findUserByLogin(username);
		if (user == null) {
			errors.add(localization.getResourcesParam("login.error"));
			return errors;
		}
		if (!password.equals(user.getPassword())) {
			errors.add(localization.getResourcesParam("login.error"));
			return errors;
		}
		if (user.isBlocked()) {
			errors.add(localization.getResourcesParam("login.blocked"));
			return errors;
		}
		if (!user.isEnabled()) {
			errors.add(localization.getResourcesParam("login.enabled"));
			return errors;
		}
		request.getSession().setAttribute("user", user);
		return errors;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		List<String> errors = validate(request);
		if (!errors.isEmpty()) {
			log.error("Error validating user");

			request.getSession().setAttribute("errors", errors);
			return Constants.COMMAND__LOGIN;
		}
		log.info("User {} was logged in", ((User) request.getSession().getAttribute("user")).getLogin());
		return Constants.COMMAND__PROFILE;
	}
}