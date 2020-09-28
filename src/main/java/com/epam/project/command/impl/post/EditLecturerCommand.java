package com.epam.project.command.impl.post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

public class EditLecturerCommand implements ICommand {
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
			e.printStackTrace();
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || user.getRole() != RoleEnum.ADMIN) {
			return Constants.COMMAND__LOGIN;
		}
		int lecturerId;
		try {
			lecturerId = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			request.getSession().setAttribute("error", "Cannot parse id :(");
			return Constants.COMMAND__ERROR;
		}
		user = userService.findUserById(lecturerId);
		if (user == null) {
			request.getSession().setAttribute("error", "Cannot find user :(");
			return Constants.COMMAND__ERROR;
		}
		if (user.getRole() != RoleEnum.LECTURER) {
			request.getSession().setAttribute("error", "Not a lecturer :(");
			return Constants.COMMAND__ERROR;
		}
		String[] checkedIds = request.getParameterValues("courses");
		String action = request.getParameter("submit");

		courseService.setLecturerForCoursesByLecturerId(lecturerId, checkedIds);

		return Constants.COMMAND__EDIT_LECTURER + "&id=" + lecturerId;
	}

}
