package com.epam.project.command.impl.get;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dto.CourseDto;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;
import com.epam.project.util.Page;

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
		final User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			try {
				response.sendRedirect(Constants.COMMAND__LOGIN);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

//		int pageNum = 0;
//		int pageNumNotEnrolled = 0;
//		if (request.getParameter("pagenum") != null && !request.getParameter("pagenum").isEmpty()) {
//			try {
//				pageNum = Integer.parseInt(request.getParameter("pagenum")) - 1;
//			} catch (NumberFormatException e) {
//				request.setAttribute("error", "Cannot parse id :(");
//				return Constants.PAGE__ERROR;
//			}
//		}
//		if (request.getParameter("pagenumnotenrolled") != null
//				&& !request.getParameter("pagenumnotenrolled").isEmpty()) {
//			try {
//				pageNumNotEnrolled = Integer.parseInt(request.getParameter("pagenumnotenrolled")) - 1;
//			} catch (NumberFormatException e) {
//				request.setAttribute("error", "Cannot parse id :(");
//				return Constants.PAGE__ERROR;
//			}
//		}

//		int countEnrolled = userService.getCoursesCountForUser(user.getId(), true);
//		request.setAttribute("totalPagesEnrolled", Math.ceil((double) countEnrolled / Constants.PAGE_SIZE_COMMON));
//
//		int countNotEnrolled = userService.getCoursesCountForUser(user.getId(), false);
//		request.setAttribute("totalPagesNotEnrolled", Math.ceil((double) countNotEnrolled / Constants.PAGE_SIZE_COMMON));
//
//		List<CourseProfilePageDto> coursesEnrolled = courseService.findAllCoursesProfilePageFromTo(Constants.PAGE_SIZE_COMMON,
//				pageNum * Constants.PAGE_SIZE_COMMON, user, true);
//		List<CourseProfilePageDto> coursesNotEnrolled = courseService.findAllCoursesProfilePageFromTo(
//				Constants.PAGE_SIZE_COMMON, pageNumNotEnrolled * Constants.PAGE_SIZE_COMMON, user, false);
		
		Page<CourseProfilePageDto> page1 = new Page<>("pagenumenrolled", "startIndexEnrolled", "totalPagesEnrolled", request,
				(limit, offset) -> courseService.findAllCoursesProfilePageFromTo(limit, offset, user, true),
				() -> userService.getCoursesCountForUser(user.getId(), true));
		
		Page<CourseProfilePageDto> page2 = new Page<>("pagenumnotenrolled", "startIndexNotEnrolled", "totalPagesNotEnrolled", request,
				(limit, offset) -> courseService.findAllCoursesProfilePageFromTo(limit, offset, user, false),
				() -> userService.getCoursesCountForUser(user.getId(), false));

		if (page1.getList() == null || page2.getList() == null) {
			request.setAttribute("error", "Cannot parse id");
			return Constants.PAGE__ERROR;
		}

		request.setAttribute("page1", page1);
		request.setAttribute("page2", page2);
//		request.setAttribute("coursesEnrolled", coursesEnrolled);
//		request.setAttribute("coursesNotEnrolled", coursesNotEnrolled);

//		request.setAttribute("startIndex", pageNum * Constants.PAGE_SIZE_COMMON + 1);

		return Constants.PAGE__PROFILE;
	}

}
