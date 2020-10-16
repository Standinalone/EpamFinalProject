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
import com.epam.project.exceptions.DBException;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.exceptions.ValidatingRequestException;
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
	public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException, ValidatingRequestException {

		request.setAttribute("lecturers", userService.findAllUsersByRole(3));
		request.setAttribute("topics", topicService.findAllTopics());
		request.setAttribute("statuses", CourseStatusEnum.values());
		
		String id = request.getParameter("id");
		if (id == null || id.isEmpty())
			return Constants.PAGE__ADD_COURSE;
		
		int courseId;
		try {
			courseId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			throw new ValidatingRequestException("error.badid", e);
		}
		
		CourseDto courseDto = courseService.getCourseDtoByCourseId(courseId);
		request.setAttribute("course", courseDto);
		return Constants.PAGE__ADD_COURSE;

	}
}
