package com.epam.project.command.impl.get;

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
import com.epam.project.l10n.Localization;
import com.epam.project.service.ICourseService;
import com.epam.project.service.ITopicService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

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
			e.printStackTrace();
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		Localization localization = (Localization) request.getSession().getAttribute("localization");
		int courseId;
		request.setAttribute("lecturers", userService.findAllUsersByRole(3));
		request.setAttribute("topics", topicService.findAllTopics());
		request.setAttribute("statuses", CourseStatusEnum.values());
		if (id == null || id.isEmpty()) {
			return Constants.PAGE__ADD_COURSE;
		}
		try {
			courseId = Integer.parseInt(id);
			CourseDto courseDto = courseService.getCourseDtoByCourseId(courseId);
			if (courseDto != null) {
				request.setAttribute("course", courseDto);
				return Constants.PAGE__ADD_COURSE;
			} else {
				log.debug("Course not found");
				request.setAttribute("error", localization.getResourcesParam("error.coursenotfound"));
				return Constants.PAGE__ERROR;
			}
		} catch (NumberFormatException e) {
			log.debug("Cannot parse id");
			request.setAttribute("error",  localization.getResourcesParam("error.badid"));
			return Constants.PAGE__ERROR;
		}
	}

}
