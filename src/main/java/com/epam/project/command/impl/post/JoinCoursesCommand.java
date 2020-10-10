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
 * ICommand implementation for a `join courses` command
 *
 */
public class JoinCoursesCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(JoinCoursesCommand.class);
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
		User user = (User) request.getSession().getAttribute("user");
		String[] checkedIds = request.getParameterValues("courses");
		userService.enrollToCourse(checkedIds, user.getId());
		log.info("User {} was enrolled to courses", user.getLogin());
		return Constants.COMMAND__HOME;
	}

}
