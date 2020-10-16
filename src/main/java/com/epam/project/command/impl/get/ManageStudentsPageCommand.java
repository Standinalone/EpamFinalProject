package com.epam.project.command.impl.get;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DBException;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;
import com.epam.project.util.Page;

/**
 * ICommand implementation for getting a page to manage users
 *
 */
public class ManageStudentsPageCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(ManageStudentsPageCommand.class);

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
	public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {

		Page<User> page = new Page<>(request, (limit, offset) -> userService.findAllUsersFromTo(limit, offset),
				() -> userService.getUsersCount());

		request.setAttribute("page", page);
		return Constants.PAGE__MANAGE_STUDENTS;
	}
}
