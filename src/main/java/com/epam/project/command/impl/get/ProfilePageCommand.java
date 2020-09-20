package com.epam.project.command.impl.get;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

public class ProfilePageCommand implements ICommand {

	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	private static ServiceFactory serviceFactory;
	private static IUserService userService;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			try {
				response.sendRedirect(Constants.PAGE_LOGIN);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		int countEnrolled = userService.getCoursesDTOCount(user, true);
		request.setAttribute("totalPages", Math.ceil((double)countEnrolled / Constants.PAGE_SIZE));

		int countNotEnrolled = userService.getCoursesDTOCount(user, false);
		request.setAttribute("totalPagesNotEnrolled", Math.ceil((double)countNotEnrolled / Constants.PAGE_SIZE));
		
		int pageNum = 0;
		if (request.getParameter("pagenum") != null && !request.getParameter("pagenum").isEmpty()) {
			pageNum = Integer.parseInt(request.getParameter("pagenum")) - 1;
		}
		
		int pageNumNotEnrolled = 0;
		if (request.getParameter("pagenumnotenrolled") != null && !request.getParameter("pagenumnotenrolled").isEmpty()) {
			pageNumNotEnrolled = Integer.parseInt(request.getParameter("pagenumnotenrolled")) - 1;
		}
		
		List<CourseProfilePageDto> coursesEnrolled = userService.getEnrolledCoursesDTOFromTo(user, Constants.PAGE_SIZE, pageNum * Constants.PAGE_SIZE);
		
		request.setAttribute("coursesEnrolled", coursesEnrolled);
		request.setAttribute("startIndex", pageNum * Constants.PAGE_SIZE + 1);
		
		List<CourseProfilePageDto> coursesNotEnrolled = userService.getNotEnrolledCoursesDTOFromTo(user, Constants.PAGE_SIZE, pageNumNotEnrolled * Constants.PAGE_SIZE);
		request.setAttribute("coursesNotEnrolled", coursesNotEnrolled);
		
		try {
			request.getRequestDispatcher(Constants.PATH_PROFILE_PAGE).forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

}
