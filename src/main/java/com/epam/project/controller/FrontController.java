package com.epam.project.controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.command.factory.CommandFactory;
import com.epam.project.constants.Constants;
import com.epam.project.exceptions.DBException;
import com.epam.project.exceptions.ValidatingRequestException;
import com.epam.project.i18n.Localization;
import com.epam.project.i18n.LocalizationFactory;

/**
 * Main controller
 *
 */
public class FrontController extends HttpServlet {
	private static final Logger log = LoggerFactory.getLogger(FrontController.class);
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String forward = handleRequest(request, response);
		if (forward != null) {
			try {
				request.getRequestDispatcher(forward).forward(request, response);
				request.getSession().setAttribute("successMessage", null);
				request.getSession().setAttribute("errors", null);
				log.trace("Forwarding to {}", forward);
			} catch (ServletException | IOException e) {
				log.error("Exception in doGet {}", e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String redirect = handleRequest(request, response);
		if (redirect != null) {
			try {
				response.sendRedirect(request.getRequestURL().append(redirect).toString());
				log.trace("Redirecting to {}", redirect);
			} catch (IOException e) {
				log.error("Exception in doPost {}", e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private String handleRequest(HttpServletRequest request, HttpServletResponse response) {
		Locale locale = (Locale) request.getSession().getAttribute("locale");
		Localization localization = LocalizationFactory.getLocalization(locale);
		ICommand command = CommandFactory.getCommand(request);
		try {
			return command.execute(request, response);
		} catch (DBException e) {
			e.printStackTrace();
			log.error("DBException ({}): {}", e.getClass().getSimpleName(), e);
			request.getSession().setAttribute("error", "DBException: " + localization.getResourcesParam(e.getMessage()));
			
		} catch (ValidatingRequestException e) {
			e.printStackTrace();
			log.error("ValidatingRequestException: {}", localization.getResourcesParam(e.getMessage()));
			request.getSession().setAttribute("error", "ValidatingRequestException: " + localization.getResourcesParam(e.getMessage()));
		}
		try {
			response.sendRedirect(request.getRequestURL().append(Constants.COMMAND__ERROR).toString());
		} catch (IOException e) {
			log.error("Exception in doPost {}", e.getMessage());
			e.printStackTrace();
		}
		log.trace("Redirecting to {}", Constants.COMMAND__ERROR);
		return null;
	}
}
