package com.epam.project.command.impl.post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.Course;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.ITopicService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

public class DeleteCourseCommand implements ICommand {
	public static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	public static IUserService userService;
	public static ITopicService topicService;
	public static ICourseService courseService;
	public static ServiceFactory serviceFactory;

	private Course course;
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
		int id = 0;
		try {
			id = Integer.parseInt(request.getParameter("id"));
		}
		catch(NumberFormatException e) {
			request.getSession().setAttribute("error", "Cannot parse id :(");
			return Constants.PAGE_ERROR;
		}
		if (!courseService.deleteCourseById(id)) {
			request.getSession().setAttribute("error", "Cannot delete :(");
			return Constants.PAGE_ERROR;
		}
		return Constants.PAGE_MANAGE_COURSES;
	}

}
