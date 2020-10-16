package com.epam.project.command.impl.get;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dto.CourseDto;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DBException;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.ServiceFactory;
import com.epam.project.util.Page;

/**
 * ICommand implementation for getting a page with lecturer's courses
 *
 */
public class MyCoursesPageCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(MyCoursesPageCommand.class);

	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	private static ServiceFactory serviceFactory;
	private static ICourseService courseService;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			courseService = serviceFactory.getCourseService();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
		final User user = (User) request.getSession().getAttribute("user");
		Page<CourseDto> page = new Page<>(request,
				(limit, offset) -> courseService.findAllCoursesDtoByLecturerIdFromTo(user.getId(), limit, offset),
				() -> courseService.getCoursesWithLecturerCount(user.getId()));

		request.setAttribute("page", page);
		return Constants.PAGE__MY_COURSES;
	}

}
