package com.epam.project.command.impl.get;

import java.time.LocalDate;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.User;
import com.epam.project.entity.VerificationToken;
import com.epam.project.exceptions.DBException;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.exceptions.ValidatingRequestException;
import com.epam.project.i18n.Localization;
import com.epam.project.i18n.LocalizationFactory;
import com.epam.project.service.ITokenService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

/**
 * ICommand implementation for getting a page with token validation
 *
 */
public class RegistrationConfirmCommand implements ICommand {

	private static final Logger log = LoggerFactory.getLogger(RegistrationConfirmCommand.class);
	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);

	private static ServiceFactory serviceFactory;
	private static IUserService userService;
	private static ITokenService tokenService;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
			tokenService = serviceFactory.getTokenService();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, ValidatingRequestException {
		String token = request.getParameter("token");
		Localization localization = LocalizationFactory
				.getLocalization((Locale) request.getSession().getAttribute("locale"));

		if (token == null)
			throw new ValidatingRequestException("register.token");

		VerificationToken verificationToken = tokenService.findTokenByToken(token);

		User user = userService.findUserById(verificationToken.getUserId());

		if (verificationToken.getExpiryDate().isBefore(LocalDate.now()))
			throw new ValidatingRequestException("register.tokenexpired");

		user.setEnabled(true);
		userService.updateUser(user);

		request.getSession().setAttribute("successMessage", localization.getResourcesParam("success.registration"));
		return Constants.PAGE__SUCCESS;
	}
}
