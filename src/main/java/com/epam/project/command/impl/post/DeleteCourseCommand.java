package com.epam.project.command.impl.post;


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
import com.epam.project.exceptions.ValidatingRequestException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.ServiceFactory;

/**
 * ICommand implementation for `delete a course` command
 *
 */
public class DeleteCourseCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(ChangeUsersStatusCommand.class);
	public static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	public static ICourseService courseService;
	public static ServiceFactory serviceFactory;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			courseService = serviceFactory.getCourseService();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, ValidatingRequestException {
		int id = 0;
		try {
			id = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			throw new ValidatingRequestException("error.badid", e);
		}

		courseService.deleteCourseById(id);
		log.info("Course [{}] deleted by {}", id, ((User) request.getSession().getAttribute("user")).getLogin());
		return Constants.COMMAND__MANAGE_COURSES;
	}

}
