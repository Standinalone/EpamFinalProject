package com.epam.project.command.impl.get;

import java.sql.SQLException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dto.CourseDto;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.i18n.Localization;
import com.epam.project.i18n.LocalizationFactory;
import com.epam.project.service.ICourseService;
import com.epam.project.service.ITopicService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

/**
 * ICommand implementation for getting a page to add and edit a course
 *
 */
public class AddEditCoursePageCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(AddEditCoursePageCommand.class);
	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	private static IUserService userService;
	private static ITopicService topicService;
	private static ICourseService courseService;
	private static ServiceFactory serviceFactory;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
			topicService = serviceFactory.getTopicService();
			courseService = serviceFactory.getCourseService();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		Localization localization = LocalizationFactory
				.getLocalization((Locale) request.getSession().getAttribute("locale"));
		int courseId;
		try {
			request.setAttribute("lecturers", userService.findAllUsersByRole(3));
		} catch (SQLException e) {
			log.error("Finding users error", e);
			request.setAttribute("error", localization.getResourcesParam("dberror.findusers"));
			return Constants.PAGE__ERROR;
		}
		try {
			request.setAttribute("topics", topicService.findAllTopics());
		} catch (SQLException e) {
			log.error("Finding topics error", e);
			request.setAttribute("error", localization.getResourcesParam("dberror.findtopics"));
			return Constants.PAGE__ERROR;
		}
		request.setAttribute("statuses", CourseStatusEnum.values());
		if (id == null || id.isEmpty()) {
			return Constants.PAGE__ADD_COURSE;
		}
		try {
			courseId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			log.debug("Cannot parse id");
			request.setAttribute("error", localization.getResourcesParam("error.badid"));
			return Constants.PAGE__ERROR;
		}
		CourseDto courseDto = null;
		try {
			courseDto = courseService.getCourseDtoByCourseId(courseId);
		} catch (SQLException e) {
			log.error("Finding course error", e);
			request.setAttribute("error", localization.getResourcesParam("dberror.getcourse"));
			return Constants.PAGE__ERROR;
		}
		if (courseDto != null) {
			request.setAttribute("course", courseDto);
			return Constants.PAGE__ADD_COURSE;
		} else {
			log.debug("Course not found");
			request.setAttribute("error", localization.getResourcesParam("error.coursenotfound"));
			return Constants.PAGE__ERROR;
		}

	}

}
