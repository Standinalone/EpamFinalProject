package com.epam.project.command.impl.get;

import java.util.Locale;

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
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.i18n.Localization;
import com.epam.project.i18n.LocalizationFactory;
import com.epam.project.service.ICourseService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;
import com.epam.project.util.Page;

/**
 * ICommand implementation for getting a page to edit a lecturer (appoint / dismiss courses)
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
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		Localization localization = LocalizationFactory.getLocalization((Locale) request.getSession().getAttribute("locale"));
		int userId = 0;
		try {
			userId = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			log.debug("Cannot parse id");
			request.setAttribute("error", localization.getResourcesParam("error.badid"));
			return Constants.PAGE__ERROR;
		}
		user = userService.findUserById(userId);
		if (user == null) {
			log.debug("Cannot find user");
			request.setAttribute("error", localization.getResourcesParam("error.usernotfound"));
			return Constants.PAGE__ERROR;
		}
		if (user.getRole() != RoleEnum.LECTURER) {
			log.debug("Requested user not a lecturer");
			request.setAttribute("error", localization.getResourcesParam("error.notlecturer"));
			return Constants.PAGE__ERROR;
		}

		Page<CourseDto> page = new Page<>(request,
				(limit, offset) -> courseService.findAllCoursesDtoFromTo(limit, offset),
				() -> courseService.getCoursesCount());

		if (page.getList() == null) {
			log.error("Page cannot be formed");
			request.setAttribute("error", localization.getResourcesParam("error.badid"));
			return Constants.PAGE__ERROR;
		}

		request.setAttribute("page", page);
		return Constants.PAGE__EDIT_USER;
	}

}
