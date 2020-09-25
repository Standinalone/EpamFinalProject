package com.epam.project.command.impl.post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.ITokenDAO;
import com.epam.project.dao.IUserDAO;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.entity.VerificationToken;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.l10n.Localization;
import com.epam.project.mailer.Mailer;

import java.util.Locale;
import java.util.UUID;

public class RegisterCommand implements ICommand {

	public static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	public static DaoFactory daoFactory;
	public static IUserDAO userDao;
	public static ITokenDAO tokenDao;

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(db);
			userDao = daoFactory.getUserDAO();
			tokenDao = daoFactory.getTokenDAO();
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
		String name = request.getParameter("name");
		String surname = request.getParameter("surname");
		String patronym = request.getParameter("patronym");
		String email = request.getParameter("email");

		if (username == null || !Pattern.matches(Constants.REGEX_USERNAME, username)) {
			errors.add(localization.getMessagesParam("register.invalidUsername"));
		}
		if (username != null && userDao.findUserByLogin(username) != null) {
			errors.add(localization.getMessagesParam("register.userExists"));
		}
//		if (email != null && userDao.findUserByEmail(email) != null) {
//			errors.add(localization.getMessagesParam("register.emailExists"));
//		}
		if (password == null || !Pattern.matches(Constants.REGEX_PASSWORD, password)) {
			errors.add(localization.getMessagesParam("register.invalidPassword"));
		}
		if (passwordConfirmation == null || !passwordConfirmation.equals(password)) {
			errors.add(localization.getMessagesParam("register.passwordConfirmation"));
		}
		if (name == null || !Pattern.matches(Constants.REGEX_NAME, password)) {
			errors.add(localization.getMessagesParam("register.nameerror"));
		}
		if (surname == null || !Pattern.matches(Constants.REGEX_NAME, surname)) {
			errors.add(localization.getMessagesParam("register.nameerror"));
		}
		if (patronym == null || !Pattern.matches(Constants.REGEX_NAME, patronym)) {
			errors.add(localization.getMessagesParam("register.nameerror"));
		}
		if (email == null || !Pattern.matches(Constants.REGEX_EMAIL, email)) {
			errors.add(localization.getMessagesParam("register.email"));
		}
		return errors;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		List<String> errors = validate(request);
		Localization localization = new Localization((Locale) request.getSession().getAttribute("locale"));
		if (errors.size() == 0) {
			request.setAttribute("errors", null);
			User user = new User();
			user.setLogin(request.getParameter("username"));
			user.setPassword(request.getParameter("password"));
			user.setName(request.getParameter("name"));
			user.setSurname(request.getParameter("surname"));
			user.setPatronym(request.getParameter("patronym"));
			user.setEmail(request.getParameter("email"));
			user.setRole(RoleEnum.USER);
			if (userDao.addUser(user)) {
				confirmRegistration(user, request);
				String successMessage = localization.getMessagesParam("success.email");
				request.getSession().setAttribute("successMessage", successMessage);
				try {
					response.sendRedirect(Constants.PAGE_SUCCESS);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					request.setAttribute("error", "Error :(");
					response.sendRedirect(Constants.PAGE_ERROR);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				request.setAttribute("errors", errors);
				request.getRequestDispatcher(Constants.PATH_REGISTER_PAGE).forward(request, response);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void confirmRegistration(User user, HttpServletRequest request) {
		String token = UUID.randomUUID().toString();

		VerificationToken myToken = new VerificationToken(token, user);
		tokenDao.addToken(myToken);

		String recipientAddress = user.getEmail();
		String subject = "Registration Confirmation";
		String confirmationUrl = request.getRequestURL() + "?command=REGISTRATION_CONFIRM&token=" + token;

		String message = "Привет, " + user.getName();

		try {
			Mailer.sendMail(new String[] { recipientAddress }, message + "\r\n" + confirmationUrl, subject);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}
