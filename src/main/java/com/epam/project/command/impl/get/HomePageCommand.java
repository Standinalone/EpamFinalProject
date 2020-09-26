package com.epam.project.command.impl.get;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dto.CourseDto;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
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
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		int count = courseService.getCoursesCount();
		request.setAttribute("totalPages", Math.ceil((double) count / Constants.PAGE_SIZE));

		int pageNum = 0;
		if (request.getParameter("pagenum") != null) {
			try {
				pageNum = Integer.parseInt(request.getParameter("pagenum")) - 1;
			} catch (NumberFormatException e) {
				request.setAttribute("error", "Cannot parse id");
				return Constants.PATH_ERROR_PAGE;
			}

		}
		List<CourseDto> courses = courseService.findAllCoursesDtoFromTo(Constants.PAGE_SIZE,
				pageNum * Constants.PAGE_SIZE);
		request.setAttribute("courses", courses);
		request.setAttribute("startIndex", pageNum * Constants.PAGE_SIZE + 1);

		return Constants.PATH_HOMEPAGE;
	}

}
