package com.epam.project.command.impl.post;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.entity.VerificationToken;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.i18n.Localization;
import com.epam.project.i18n.LocalizationFactory;
import com.epam.project.mailer.Mailer;
import com.epam.project.service.ITokenService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

import java.util.Locale;
import java.util.UUID;

/**
 * ICommand implementation for a `register` command
 *
 */
public class RegisterCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(RegisterCommand.class);

	public static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);

	private ServiceFactory serviceFactory;

	private IUserService userService;
	private ITokenService tokenService;
	private User user;
	private Localization localization;

	public boolean isLecturer = false;

	/**
	 * Constructor used in testing by Mockito
	 * 
	 * @param serviceFactory mocked ServiceFactory
	 * @param userService    mocked IUserService
	 * @param tokenService   mocked ITokenService
	 */
	private RegisterCommand(ServiceFactory serviceFactory, IUserService userService, ITokenService tokenService) {
		super();
		this.serviceFactory = serviceFactory;
		this.userService = userService;
		this.tokenService = tokenService;
	}

	public RegisterCommand() {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
			tokenService = serviceFactory.getTokenService();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}
//	static {
//		try {
//			serviceFactory = ServiceFactory.getServiceFactory(db);
//			userService = serviceFactory.getUserService();
//			tokenService = serviceFactory.getTokenService();
//		} catch (DatabaseNotSupportedException e) {
//			log.error("DatabaseNotSupportedException", e.getMessage());
//		}
//	}

	public List<String> validate(User user) {
		List<String> errors = new ArrayList<>();
		if (!Pattern.matches(Constants.REGEX__USERNAME, user.getLogin())) {
			errors.add(localization.getResourcesParam("register.invalidUsername"));
		}
		User existingUser = null;
		try {
			existingUser = userService.findUserByLogin(user.getLogin());
		} catch (SQLException e) {
			log.error("Finding user error", e);
			errors.add(localization.getResourcesParam("dberror.finduser"));
		}
		if (existingUser != null) {
			errors.add(localization.getResourcesParam("register.userExists"));
		}
//		if (userDao.findUserByEmail(user.getEmail()) != null) {
//			errors.add(localization.getMessagesParam("register.emailExists"));
//		}
		if (!Pattern.matches(Constants.REGEX__PASSWORD, user.getPassword())) {
			errors.add(localization.getResourcesParam("register.invalidPassword"));
		}
//		if (passwordConfirmation == null || !passwordConfirmation.equals(password)) {
//			errors.add(localization.getResourcesParam("register.passwordConfirmation"));
//		}
		if (!Pattern.matches(Constants.REGEX__NAME, user.getName())) {
			errors.add(localization.getResourcesParam("register.nameerror"));
		}
		if (!Pattern.matches(Constants.REGEX__NAME, user.getSurname())) {
			errors.add(localization.getResourcesParam("register.nameerror"));
		}
		if (!Pattern.matches(Constants.REGEX__NAME, user.getPatronym())) {
			errors.add(localization.getResourcesParam("register.nameerror"));
		}
		if (!Pattern.matches(Constants.REGEX__EMAIL, user.getEmail())) {
			errors.add(localization.getResourcesParam("register.email"));
		}
		return errors;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
//		localization = new Localization((Locale) request.getSession().getAttribute("locale"));
		localization = LocalizationFactory.getLocalization((Locale) request.getSession().getAttribute("locale"));
		user = (User) request.getSession().getAttribute("user");
		if (request.getParameter("lecturer") != null && !request.getParameter("lecturer").isEmpty() && user != null
				&& user.getRole() == RoleEnum.ADMIN) {
			isLecturer = true;
		}

		List<String> mappingErrors = mapToUser(request);
		if (!mappingErrors.isEmpty()) {
			log.error("Error mapping to User");
			request.getSession().setAttribute("errors", mappingErrors);
			if (isLecturer)
				return Constants.COMMAND__ADD_LECTURER;
			return Constants.COMMAND__REGISTER;
		}

		List<String> errors = validate(user);
		if (!errors.isEmpty()) {
			log.error("Error validating user");
			request.getSession().setAttribute("errors", errors);
			if (isLecturer)
				return Constants.COMMAND__ADD_LECTURER;
			return Constants.COMMAND__REGISTER;
		}

		if (!userService.addUser(user)) {
			log.error("Error adding user");
			request.getSession().setAttribute("error", localization.getResourcesParam("error.adding"));
			return Constants.COMMAND__ERROR;
		}

		if (!isLecturer) {
			try {
				sendConfirmationEmail(user, request);
			} catch (MessagingException e) {
				log.error("Error sending mail to user");
				request.getSession().setAttribute("error", localization.getResourcesParam("error.email"));
				userService.deleteUserById(user.getId());
				return Constants.COMMAND__ERROR;
			} catch (SQLException e) {
				log.error("Error finding token");
				request.getSession().setAttribute("error", localization.getResourcesParam("error.token"));
				userService.deleteUserById(user.getId());
				return Constants.COMMAND__ERROR;
			}
			log.info("User {} was registered", user.getLogin());
			String successMessage = localization.getResourcesParam("success.email");
			request.getSession().setAttribute("successMessage", successMessage);
			return Constants.COMMAND__SUCCESS;
		}
		log.info("Lecturer {} was registered", user.getLogin());
		return Constants.COMMAND__MANAGE_STUDENTS;
	}

	private List<String> mapToUser(HttpServletRequest request) {
		user = new User();
		List<String> errors = new ArrayList<>();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String surname = request.getParameter("surname");
		String patronym = request.getParameter("patronym");
		String email = request.getParameter("email");
		String passwordConfirmation = request.getParameter("passwordConfirmation");
		if (username == null || username.isEmpty())
			errors.add(localization.getResourcesParam("error.username"));
		if (password == null || password.isEmpty())
			errors.add(localization.getResourcesParam("error.password"));
		if (name == null || name.isEmpty())
			errors.add(localization.getResourcesParam("error.name"));
		if (surname == null || surname.isEmpty())
			errors.add(localization.getResourcesParam("error.surname"));
		if (patronym == null || patronym.isEmpty())
			errors.add(localization.getResourcesParam("error.patronym"));
		if (email == null || email.isEmpty())
			errors.add(localization.getResourcesParam("error.email"));
		if (passwordConfirmation == null || passwordConfirmation.isEmpty())
			errors.add(localization.getResourcesParam("error.passwordConfirmation"));
		if (!passwordConfirmation.equals(password))
			errors.add(localization.getResourcesParam("register.passwordConfirmation"));
		user.setLogin(username);
		user.setPassword(password);
		user.setName(name);
		user.setSurname(surname);
		user.setPatronym(patronym);
		user.setEmail(email);
		if (!isLecturer) {
			user.setRole(RoleEnum.USER);
			user.setEnabled(false);
		} else {
			user.setRole(RoleEnum.LECTURER);
			user.setEnabled(true);
		}
		return errors;

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
