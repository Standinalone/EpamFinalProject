package com.epam.project.command.impl.get;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

public class ProfilePageCommand implements ICommand {

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
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			try {
				response.sendRedirect(Constants.PAGE_LOGIN);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		int pageNum = 0;
		int pageNumNotEnrolled = 0;
		if (request.getParameter("pagenum") != null && !request.getParameter("pagenum").isEmpty()) {
			try {
				pageNum = Integer.parseInt(request.getParameter("pagenum")) - 1;
			} catch (NumberFormatException e) {
				request.setAttribute("error", "Cannot parse id :(");
				return Constants.PATH_ERROR_PAGE;
			}
		}
		if (request.getParameter("pagenumnotenrolled") != null
				&& !request.getParameter("pagenumnotenrolled").isEmpty()) {
			try {
				pageNumNotEnrolled = Integer.parseInt(request.getParameter("pagenumnotenrolled")) - 1;
			} catch (NumberFormatException e) {
				request.setAttribute("error", "Cannot parse id :(");
				return Constants.PATH_ERROR_PAGE;
			}
		}

		int countEnrolled = userService.getCoursesCountForUser(user.getId(), true);
		request.setAttribute("totalPagesEnrolled", Math.ceil((double) countEnrolled / Constants.PAGE_SIZE));

		int countNotEnrolled = userService.getCoursesCountForUser(user.getId(), false);
		request.setAttribute("totalPagesNotEnrolled", Math.ceil((double) countNotEnrolled / Constants.PAGE_SIZE));

		List<CourseProfilePageDto> coursesEnrolled = courseService.findAllCoursesProfilePageFromTo(Constants.PAGE_SIZE,
				pageNum * Constants.PAGE_SIZE, user, true);
		List<CourseProfilePageDto> coursesNotEnrolled = courseService.findAllCoursesProfilePageFromTo(
				Constants.PAGE_SIZE, pageNumNotEnrolled * Constants.PAGE_SIZE, user, false);

		request.setAttribute("coursesEnrolled", coursesEnrolled);
		request.setAttribute("coursesNotEnrolled", coursesNotEnrolled);

		request.setAttribute("startIndex", pageNum * Constants.PAGE_SIZE + 1);

		return Constants.PATH_PROFILE_PAGE;
	}

}
