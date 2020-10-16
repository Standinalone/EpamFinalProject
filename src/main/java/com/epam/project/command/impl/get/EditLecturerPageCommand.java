package com.epam.project.command.impl.get;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dto.CourseDto;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DBException;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.exceptions.ValidatingRequestException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;
import com.epam.project.util.Page;

/**
 * ICommand implementation for getting a page to edit a lecturer (appoint /
 * dismiss courses)
 *
 */
public class EditLecturerPageCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(EditLecturerPageCommand.class);
	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);

	private static ServiceFactory serviceFactory;
	private static IUserService userService;
	private static ICourseService courseService;

	public boolean isLecturer = false;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
			courseService = serviceFactory.getCourseService();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, ValidatingRequestException {

		int userId = 0;
		try {
			userId = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			throw new ValidatingRequestException("error.badid", e);
		}
		User user = userService.findUserById(userId);
		if (user.getRole() != RoleEnum.LECTURER)
			throw new ValidatingRequestException("error.notlecturer");

		Page<CourseDto> page = new Page<>(request,
				(limit, offset) -> courseService.findAllCoursesDtoFromTo(limit, offset),
				() -> courseService.getCoursesCount());

		request.setAttribute("page", page);
		return Constants.PAGE__EDIT_USER;
	}

}
