package com.epam.project.command.impl.get;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DBException;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.exceptions.ValidatingRequestException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;
import com.epam.project.util.Page;

/**
 * ICommand implementation for getting a page for lecturer to manage a course
 *
 */
public class LecturerPanelPageCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(LecturerPanelPageCommand.class);

	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);

	private static ServiceFactory serviceFactory;
	private static IUserService userService;
	private static ICourseService courseService;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
			courseService = serviceFactory.getCourseService();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws DBException, ValidatingRequestException {
		User user = (User) request.getSession().getAttribute("user");
		int courseId = 0;
		try {
			courseId = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			throw new ValidatingRequestException("error.badid", e);
		}
		Course course = courseService.findCourseById(courseId);

		if (course.getLecturerId() != user.getId())
			throw new ValidatingRequestException("error.notyourcourse");

		final int id = course.getId();
		Page<User> page1 = new Page<>("pagenumEnrolled", "startIndexEnrolled", "totalPagesEnrolled", request,
				(limit, offset) -> userService.findAllUsersWithCourseFromTo(id, limit, offset, true),
				() -> userService.getUsersWithCourseCount(id, true));
		Page<User> page2 = new Page<>("pagenumNotEnrolled", "startIndexNotEnrolled", "totalPagesNotEnrolled", request,
				(limit, offset) -> userService.findAllUsersWithCourseFromTo(id, limit, offset, false),
				() -> userService.getUsersWithCourseCount(id, false));

		request.setAttribute("page1", page1);
		request.setAttribute("page2", page2);
		request.setAttribute("statuses", CourseStatusEnum.values());
		request.setAttribute("course", course);

		return Constants.PAGE__MANAGE_JOURNAL;
	}

}
