package com.epam.project.command.impl.post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.ServiceFactory;

public class DeleteCourseCommand implements ICommand {
	public static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	public static ICourseService courseService;
	public static ServiceFactory serviceFactory;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
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
			return Constants.COMMAND__ERROR;
		}
		if (!courseService.deleteCourseById(id)) {
			request.getSession().setAttribute("error", "Cannot delete :(");
			return Constants.COMMAND__ERROR;
		}
		return Constants.COMMAND__MANAGE_COURSES;
	}

}
