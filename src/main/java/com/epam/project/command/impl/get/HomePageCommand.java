package com.epam.project.command.impl.get;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dto.CourseHomePageDto;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

public class HomePageCommand implements ICommand {
	
	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	private static ServiceFactory serviceFactory;
	private static ICourseService courseService;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			courseService = serviceFactory.getCourseService();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		int count = courseService.getCoursesCount();
		request.setAttribute("totalPages", Math.ceil((double)count / Constants.PAGE_SIZE));

		int pageNum = 0;
		if (request.getParameter("pagenum") != null) {
			pageNum = Integer.parseInt(request.getParameter("pagenum")) - 1;
		}
		
		List<CourseHomePageDto> courses = courseService.getCoursesInfoDTOFromTo(Constants.PAGE_SIZE, pageNum * Constants.PAGE_SIZE);
		request.setAttribute("courses", courses);
		request.setAttribute("startIndex", pageNum * Constants.PAGE_SIZE + 1);
		try {
			request.getRequestDispatcher(Constants.PATH_HOMEPAGE).forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

}
