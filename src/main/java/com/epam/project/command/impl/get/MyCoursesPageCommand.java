package com.epam.project.command.impl.get;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dto.CourseDto;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;
import com.epam.project.util.Page;

public class MyCoursesPageCommand implements ICommand {
	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	private static ServiceFactory serviceFactory;
	private static IUserService userService;
	public static ICourseService courseService;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
			courseService = serviceFactory.getCourseService();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		final User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			try {
				response.sendRedirect(Constants.COMMAND__LOGIN);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		Page<CourseDto> page = new Page<>(request,
				(limit, offset) -> courseService.findAllCoursesDtoByLecturerIdFromTo(user.getId(), limit, offset),
				() -> courseService.getCoursesWithLecturerCount(user.getId()));

		if (page.getList() == null) {
			request.setAttribute("error", "Cannot parse id");
			return Constants.PAGE__ERROR;
		}

		request.setAttribute("page", page);
		return Constants.PAGE__MY_COURSES;
	}

}