package com.epam.project.command.impl.post;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.entity.VerificationToken;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.l10n.Localization;
import com.epam.project.mailer.Mailer;
import com.epam.project.service.ITokenService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

import java.util.Locale;
import java.util.UUID;

public class RegisterCommand implements ICommand {

	public static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);

	public static ServiceFactory serviceFactory;
	public static IUserService userService;
	public static ITokenService tokenService;

	public boolean isLecturer = false;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
			tokenService = serviceFactory.getTokenService();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public List<String> validate(HttpServletRequest request) {
		List<String> errors = new ArrayList<>();

//		Localization localization = new Localization((Locale) request.getSession().getAttribute("locale"));

		Localization localization = (Localization) request.getSession().getAttribute("localization");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String passwordConfirmation = request.getParameter("passwordConfirmation");
		String name = request.getParameter("name");
		String surname = request.getParameter("surname");
		String patronym = request.getParameter("patronym");
		String email = request.getParameter("email");

		if (username == null || !Pattern.matches(Constants.REGEX__USERNAME, username)) {
			errors.add(localization.getResourcesParam("register.invalidUsername"));
		}
		if (username != null && userService.findUserByLogin(username) != null) {
			errors.add(localization.getResourcesParam("register.userExists"));
		}
//		if (email != null && userDao.findUserByEmail(email) != null) {
//			errors.add(localization.getMessagesParam("register.emailExists"));
//		}
		if (password == null || !Pattern.matches(Constants.REGEX__PASSWORD, password)) {
			errors.add(localization.getResourcesParam("register.invalidPassword"));
		}
		if (passwordConfirmation == null || !passwordConfirmation.equals(password)) {
			errors.add(localization.getResourcesParam("register.passwordConfirmation"));
		}
		if (name == null || !Pattern.matches(Constants.REGEX__NAME, password)) {
			errors.add(localization.getResourcesParam("register.nameerror"));
		}
		if (surname == null || !Pattern.matches(Constants.REGEX__NAME, surname)) {
			errors.add(localization.getResourcesParam("register.nameerror"));
		}
		if (patronym == null || !Pattern.matches(Constants.REGEX__NAME, patronym)) {
			errors.add(localization.getResourcesParam("register.nameerror"));
		}
		if (email == null || !Pattern.matches(Constants.REGEX__EMAIL, email)) {
			errors.add(localization.getResourcesParam("register.email"));
		}
		return errors;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		if (request.getParameter("lecturer") != null && user != null && user.getRole() == RoleEnum.ADMIN) {
			isLecturer = true;
		}
		List<String> errors = validate(request);
		Localization localization = new Localization((Locale) request.getSession().getAttribute("locale"));
		if (!errors.isEmpty()) {
			request.getSession().setAttribute("errors", errors);
			if (isLecturer)
				return Constants.COMMAND__ADD_LECTURER;
			return Constants.COMMAND__REGISTER;
		}
		request.getSession().setAttribute("errors", null);
		user = mapToUser(request);

		if (!userService.addUser(user)) {
			request.getSession().setAttribute("error", "Cannot add user :(");
			return Constants.COMMAND__ERROR;
		}

		if (!isLecturer) {
			try {
				sendConfirmationEmail(user, request);
			} catch (MessagingException e) {
				request.getSession().setAttribute("error", "Cannot send confirmation email :(");
				userService.deleteUserById(user.getId());
				return Constants.COMMAND__ERROR;
			} catch (SQLException e) {
				request.getSession().setAttribute("error", "Cannot create token :(");
				userService.deleteUserById(user.getId());
				return Constants.COMMAND__ERROR;
			}
			String successMessage = localization.getResourcesParam("success.email");
			request.getSession().setAttribute("successMessage", successMessage);
			return Constants.COMMAND__SUCCESS;
		}
		return Constants.COMMAND__MANAGE_STUDENTS;
	}

	private User mapToUser(HttpServletRequest request) {
		User user = new User();
		try {
			user.setLogin(request.getParameter("username"));
			user.setPassword(request.getParameter("password"));
			user.setName(request.getParameter("name"));
			user.setSurname(request.getParameter("surname"));
			user.setPatronym(request.getParameter("patronym"));
			user.setEmail(request.getParameter("email"));
			if (!isLecturer) {
				user.setRole(RoleEnum.USER);
				user.setEnabled(false);
			} else {
				user.setRole(RoleEnum.LECTURER);
				user.setEnabled(true);
			}
		} catch (Exception e) {
			user = null;
		}
		return user;

	}

	private void sendConfirmationEmail(User user, HttpServletRequest request)
			throws AddressException, MessagingException, SQLException {
		String token = UUID.randomUUID().toString();

		VerificationToken myToken = new VerificationToken(token, user);
		if (!tokenService.addToken(myToken)) {
			throw new SQLException();
		}

		String recipientAddress = user.getEmail();
		String subject = "Registration Confirmation";
		String confirmationUrl = request.getRequestURL() + "?command=REGISTRATION_CONFIRM&token=" + token;

		String message = "Привет, " + user.getName();

		Mailer.sendMail(new String[] { recipientAddress }, message + "\r\n" + confirmationUrl, subject);

	}

}
