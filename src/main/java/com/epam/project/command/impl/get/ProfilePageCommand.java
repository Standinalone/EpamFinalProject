package com.epam.project.command.impl.get;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DBException;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;
import com.epam.project.util.Page;

/**
 * ICommand implementation for getting a profile page
 *
 */
public class ProfilePageCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(ProfilePageCommand.class);


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
	public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
		final User user = (User) request.getSession().getAttribute("user");
		Page<CourseProfilePageDto> page1 = new Page<>("pagenumenrolled", "startIndexEnrolled", "totalPagesEnrolled",
				request, (limit, offset) -> courseService.findAllCoursesProfilePageFromTo(limit, offset, user, true),
				() -> userService.getCoursesCountForUser(user.getId(), true));

		Page<CourseProfilePageDto> page2 = new Page<>("pagenumnotenrolled", "startIndexNotEnrolled",
				"totalPagesNotEnrolled", request,
				(limit, offset) -> courseService.findAllCoursesProfilePageFromTo(limit, offset, user, false),
				() -> userService.getCoursesCountForUser(user.getId(), false));

		request.setAttribute("page1", page1);
		request.setAttribute("page2", page2);

		return Constants.PAGE__PROFILE;
	}

}
