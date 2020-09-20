package com.epam.project.command.impl.post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.IUserDAO;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.l10n.Localization;

public class LoginCommand implements ICommand {

	public static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	public static DaoFactory daoFactory;
	public static IUserDAO userDao;

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(db);
			userDao = daoFactory.getUserDAO();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public List<String> validate(HttpServletRequest request) {
		List<String> errors = new ArrayList<>();
		Localization localization = new Localization((Locale) request.getSession().getAttribute("locale"));
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		User user = userDao.findUserByLogin(username);
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
		request.getSession().setAttribute("user", user);
		return errors;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		List<String> errors = validate(request);
		if (errors.isEmpty()) {
			request.getSession().setAttribute("errors", null);
			try {
				response.sendRedirect(Constants.PAGE_PROFILE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				request.getSession().setAttribute("errors", errors);
				request.getRequestDispatcher(Constants.PATH_LOGIN_PAGE).forward(request, response);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}