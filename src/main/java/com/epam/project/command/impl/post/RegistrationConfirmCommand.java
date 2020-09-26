package com.epam.project.command.impl.post;

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
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.l10n.Localization;
import com.epam.project.service.ITokenService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

public class RegistrationConfirmCommand implements ICommand {

	private static final Logger log = LoggerFactory.getLogger(RegistrationConfirmCommand.class);
	public static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);

	public static ServiceFactory serviceFactory;
	public static IUserService userService;
	public static ITokenService tokenService;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
			tokenService = serviceFactory.getTokenService();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		Localization localization = new Localization((Locale) request.getSession().getAttribute("locale"));

		if (token == null) {
			log.error("Token wasn't provided");
			request.getSession().setAttribute("error", localization.getMessagesParam("register.token"));
			return Constants.PAGE_ERROR;
		}
		
		VerificationToken verificationToken = tokenService.findTokenByToken(token);
		if (verificationToken == null) {
			request.getSession().setAttribute("error", localization.getMessagesParam("register.badtoken"));
			return Constants.PATH_ERROR_PAGE;
		}
		
		User user = userService.findUserById(verificationToken.getUserId());
		if (verificationToken.getExpiryDate().isBefore(LocalDate.now())) {
			request.getSession().setAttribute("error", localization.getMessagesParam("register.tokenexpired"));
			return Constants.PATH_ERROR_PAGE;
		} 
		
		user.setEnabled(true);
		if (!userService.updateUser(user)) {
			request.getSession().setAttribute("error", "Couldn't update user");
			return Constants.PATH_ERROR_PAGE;
		}
		
		request.getSession().setAttribute("successMessage", localization.getMessagesParam("success.registration"));
		return Constants.PAGE_SUCCESS;
	}
}
