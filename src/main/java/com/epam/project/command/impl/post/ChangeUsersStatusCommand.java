package com.epam.project.command.impl.post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

/**
 * ICommand implementation for `change users' statuses` command
 *
 */
public class ChangeUsersStatusCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(ChangeUsersStatusCommand.class);
	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	private static ServiceFactory serviceFactory;
	private static IUserService userService;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String page = request.getParameter("page");
		String[] checkedIds = request.getParameterValues("users");
		String action = request.getParameter("submit");

		if ("block".equals(action)) {
			userService.blockUsersById(checkedIds);
		} else {
			userService.unblockUsersById(checkedIds);
		}
		log.info("Users' status changed by {}", ((User) request.getSession().getAttribute("user")).getLogin());
		return "?" + (page == null ? "" : page);
	}

}
