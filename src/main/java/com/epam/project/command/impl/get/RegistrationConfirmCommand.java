package com.epam.project.command.impl.get;

import java.time.LocalDate;

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
			log.error("Databae not supported - {}", db.name());
			e.printStackTrace();
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		Localization localization = (Localization) request.getSession().getAttribute("localization");
		
		if (token == null) {
			log.error("Token wasn't provided");
			request.getSession().setAttribute("error", localization.getResourcesParam("register.token"));
			return Constants.PAGE__ERROR;
		}
		
		VerificationToken verificationToken = tokenService.findTokenByToken(token);
		if (verificationToken == null) {
			request.getSession().setAttribute("error", localization.getResourcesParam("register.badtoken"));
			return Constants.PAGE__ERROR;
		}
		
		User user = userService.findUserById(verificationToken.getUserId());
		if (verificationToken.getExpiryDate().isBefore(LocalDate.now())) {
			request.getSession().setAttribute("error", localization.getResourcesParam("register.tokenexpired"));
			return Constants.PAGE__ERROR;
		} 
		
		user.setEnabled(true);
		if (!userService.updateUser(user)) {
			request.getSession().setAttribute("error", localization.getResourcesParam("error.updating"));
			return Constants.PAGE__ERROR;
		}
		
		request.getSession().setAttribute("successMessage", localization.getResourcesParam("success.registration"));
		return Constants.PAGE__SUCCESS;
	}
}
