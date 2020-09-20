package com.epam.project.command.impl.post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
import java.util.Locale;

public class RegisterCommand implements ICommand {

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
		String passwordConfirmation = request.getParameter("passwordConfirmation");

		if (username == null || !Pattern.matches(Constants.REGEX_USERNAME, username)) {
			errors.add(localization.getMessagesParam("register.invalidUsername"));
		}
		if (username != null && userDao.findUserByLogin(username) != null) {
			errors.add(localization.getMessagesParam("register.userExists"));
		}
		if (password == null || !Pattern.matches(Constants.REGEX_PASSWORD, password)) {
			errors.add(localization.getMessagesParam("register.invalidPassword"));
		}
		if (passwordConfirmation == null || !passwordConfirmation.equals(password)) {
			errors.add(localization.getMessagesParam("register.passwordConfirmation"));
		}
		return errors;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		List<String> errors = validate(request);
		if (errors.size() == 0) {
			request.getSession().setAttribute("errors", null);
			User user = new User();
			user.setLogin(request.getParameter("username"));
			user.setPassword(request.getParameter("password"));
			if (userDao.addUser(user)) {
				request.getSession().setAttribute("user", user);
				try {
					response.sendRedirect(Constants.PAGE_HOME);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					request.getSession().setAttribute("error", "Error :(");
					response.sendRedirect(Constants.PAGE_ERROR);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				request.getSession().setAttribute("errors", errors);
				request.getRequestDispatcher(Constants.PATH_REGISTER_PAGE).forward(request, response);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		}
	}

}
