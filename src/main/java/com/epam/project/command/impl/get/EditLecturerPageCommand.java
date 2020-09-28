package com.epam.project.command.impl.get;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dto.CourseDto;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.ITokenService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;
import com.epam.project.util.Page;

public class EditLecturerPageCommand implements ICommand {
	public static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);

	public static ServiceFactory serviceFactory;
	public static IUserService userService;
	public static ITokenService tokenService;
	public static ICourseService courseService;

	public boolean isLecturer = false;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
			tokenService = serviceFactory.getTokenService();
			courseService = serviceFactory.getCourseService();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || user.getRole() != RoleEnum.ADMIN) {
			try {
				response.sendRedirect(Constants.COMMAND__LOGIN);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		int userId = 0;
		try {
			userId = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			request.setAttribute("error", "Cannot parse id :(");
			return Constants.PAGE__ERROR;
		}
		user = userService.findUserById(userId);
		if (user == null) {
			request.setAttribute("error", "Cannot find user :(");
			return Constants.PAGE__ERROR;
		}
		if (user.getRole() != RoleEnum.LECTURER) {
			request.setAttribute("error", "Not a lecturer :(");
			return Constants.PAGE__ERROR;
		}

		Page<CourseDto> page = new Page<>(request, (limit, offset) -> courseService.findAllCoursesDtoFromTo(limit, offset),
				() -> courseService.getCoursesCount());

		if (page.getList() == null) {
			request.setAttribute("error", "Cannot parse id");
			return Constants.PAGE__ERROR;
		}

		request.setAttribute("page", page);

//		List<CourseDto> courses = courseService.findAllCoursesDto();
//		request.setAttribute("courses", courses);
		return Constants.PAGE__EDIT_USER;
	}

}
