package com.epam.project.command.impl.post;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DBException;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.exceptions.ValidatingRequestException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

/**
 * ICommand implementation for an `edit a lecturer` command
 *
 */
public class EditLecturerCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(EditLecturerCommand.class);
	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	private static ServiceFactory serviceFactory;
	private static ICourseService courseService;
	public static IUserService userService;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			courseService = serviceFactory.getCourseService();
			userService = serviceFactory.getUserService();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException, ValidatingRequestException {
		int lecturerId = 0;
		try {
			lecturerId = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			throw new ValidatingRequestException("error.badid", e);
		}
		User user = userService.findUserById(lecturerId);

		if (user.getRole() != RoleEnum.LECTURER) {
			throw new ValidatingRequestException("error.notlecturer");
		}
		String[] checkedIds = request.getParameterValues("courses");

		courseService.setLecturerForCoursesByLecturerId(lecturerId, checkedIds);
		log.info("Courses were set to lecturer {}", user.getLogin());
		return Constants.COMMAND__EDIT_LECTURER + "&id=" + lecturerId;
	}

}
