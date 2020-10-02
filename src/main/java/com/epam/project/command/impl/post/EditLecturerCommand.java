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
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.l10n.Localization;
import com.epam.project.service.ICourseService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

public class EditLecturerCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(EditLecturerCommand.class);
	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	private static ServiceFactory serviceFactory;
	private static ICourseService courseService;
	public static IUserService userService;
	private Localization localization;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			courseService = serviceFactory.getCourseService();
			userService = serviceFactory.getUserService();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		localization = (Localization) request.getSession().getAttribute("localization");
		int lecturerId = 0;
		try {
			lecturerId = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			log.error("Cannot parse id");
			request.getSession().setAttribute("error", localization.getResourcesParam("error.badid"));
			return Constants.COMMAND__ERROR;
		}
		User user = userService.findUserById(lecturerId);
		if (user == null) {
			log.error("Cannot find user");
			request.getSession().setAttribute("error", localization.getResourcesParam("error.cannotfind"));
			return Constants.COMMAND__ERROR;
		}
		if (user.getRole() != RoleEnum.LECTURER) {
			log.error("User is not a lecturer");
			request.getSession().setAttribute("error", localization.getResourcesParam("error.notlecturer"));
			return Constants.COMMAND__ERROR;
		}
		String[] checkedIds = request.getParameterValues("courses");

		courseService.setLecturerForCoursesByLecturerId(lecturerId, checkedIds);
		log.info("Courses were set to lecturer {}", user.getLogin());
		return Constants.COMMAND__EDIT_LECTURER + "&id=" + lecturerId;
	}

}
