package com.epam.project.command.impl.get;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dto.CourseDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.ITokenService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;
import com.epam.project.util.Page;

public class EditJournalPageCommand implements ICommand {
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
		if (user == null || user.getRole() != RoleEnum.LECTURER) {
			try {
				response.sendRedirect(Constants.COMMAND__LOGIN);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		int courseId = 0;
		try {
			courseId = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			request.setAttribute("error", "Cannot parse id :(");
			return Constants.PAGE__ERROR;
		}
		Course course = courseService.findCourseById(courseId);
		if (course == null) {
			request.setAttribute("error", "Cannot find course :(");
			return Constants.PAGE__ERROR;
		}
		if (course.getLecturerId() != user.getId()) {
			request.setAttribute("error", "Not your course :\\");
			return Constants.PAGE__ERROR;
		}

		Page<User> page1 = new Page<>("pagenumEnrolled", "startIndexEnrolled", "totalPagesEnrolled", request, (limit, offset) -> userService.findAllUsersWithCourseFromTo(course.getId(), limit, offset, true),
				() -> userService.getUsersWithCourseCount(course.getId(), true));
		Page<User> page2 = new Page<>("pagenumNotEnrolled", "startIndexNotEnrolled", "totalPagesNotEnrolled", request, (limit, offset) -> userService.findAllUsersWithCourseFromTo(course.getId(), limit, offset, false),
				() -> userService.getUsersWithCourseCount(course.getId(), false));
		
		if (page1.getList() == null || page2.getList() == null) {
			request.setAttribute("error", "Cannot parse id");
			return Constants.PAGE__ERROR;
		}

		request.setAttribute("page1", page1);
		request.setAttribute("page2", page2);
		request.setAttribute("statuses", CourseStatusEnum.values());
		request.setAttribute("course", course);

//		List<CourseDto> courses = courseService.findAllCoursesDto();
//		request.setAttribute("courses", courses);
		return Constants.PAGE__MANAGE_JOURNAL;
	}

}
