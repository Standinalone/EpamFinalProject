package com.epam.project.command.impl.post;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.ITokenDAO;
import com.epam.project.dao.IUserDAO;
import com.epam.project.entity.User;
import com.epam.project.entity.VerificationToken;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.l10n.Localization;

public class RegistrationConfirmCommand implements ICommand {

	private static final Logger log = LoggerFactory.getLogger(RegistrationConfirmCommand.class);
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

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getParameter("token");
		List<String> errors = new ArrayList<>();
		Localization localization = new Localization((Locale) request.getSession().getAttribute("locale"));

		if (token != null) {
			VerificationToken verificationToken = tokenDao.getVerificationToken(token);
			if (verificationToken == null) {
				errors.add(localization.getMessagesParam("register.badtoken"));
				request.getSession().setAttribute("error", errors);
				try {
					request.getRequestDispatcher(Constants.PATH_ERROR_PAGE).forward(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
				return;
			}
			User user = userDao.findUserById(verificationToken.getUserId());
			if (verificationToken.getExpiryDate().isBefore(LocalDate.now())) {
				errors.add(localization.getMessagesParam("register.tokenexpired"));
			}
			else {
				user.setEnabled(true);
				userDao.updateUser(user);
				String successMessage = localization.getMessagesParam("success.registration");
				request.getSession().setAttribute("successMessage", successMessage);
				try {
					response.sendRedirect(Constants.PAGE_SUCCESS);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} else {
			log.error("Token wasn't provided");
			errors.add(localization.getMessagesParam("register.token"));
			request.getSession().setAttribute("errors", errors);
			try {
				request.getRequestDispatcher(Constants.PATH_ERROR_PAGE).forward(request, response);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		}
	}

}
