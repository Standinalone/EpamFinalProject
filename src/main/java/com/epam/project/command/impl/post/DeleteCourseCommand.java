package com.epam.project.command.impl.post;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.i18n.Localization;
import com.epam.project.i18n.LocalizationFactory;
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
	private Localization localization;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			courseService = serviceFactory.getCourseService();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		localization = LocalizationFactory.getLocalization((Locale) request.getSession().getAttribute("locale"));
		int id = 0;
		try {
			id = Integer.parseInt(request.getParameter("id"));
		}
		catch(NumberFormatException e) {
			log.debug("Cannot parse id");
			request.getSession().setAttribute("error", localization.getResourcesParam("error.badid"));
			return Constants.COMMAND__ERROR;
		}
		if(!courseService.deleteCourseById(id)){
			log.error("Cannot delete course");
			request.getSession().setAttribute("error", localization.getResourcesParam("error.deleting"));
			return Constants.COMMAND__ERROR;
		}
		log.info("Course [{}] deleted by {}", id, ((User) request.getSession().getAttribute("user")).getLogin());
		return Constants.COMMAND__MANAGE_COURSES;
	}

}
