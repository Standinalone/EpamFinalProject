package com.epam.project.command.impl.post;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;
import com.epam.project.l10n.Localization;
import com.epam.project.mailer.Mailer;

public class LecturerPanelCommand implements ICommand {
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
			e.printStackTrace();
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		String page = request.getParameter("page");
		if (user == null || user.getRole() != RoleEnum.LECTURER) {
			return Constants.COMMAND__LOGIN;
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

		String[] checkedIds = request.getParameterValues("users");
		String action = request.getParameter("submit");

		Localization localization = (Localization) request.getSession().getAttribute("localization");
		switch (action) {
		case "decline":
			userService.declineRequestForIds(course.getId(), checkedIds);
			request.getSession().setAttribute("successMessage", localization.getResourcesParam("success.declined"));
			break;
		case "add":
			userService.registerInCourseByUsersIds(course.getId(), checkedIds, true);
			request.getSession().setAttribute("successMessage", localization.getResourcesParam("success.usersadded"));
			break;
		case "remove":
			userService.declineRequestForIds(course.getId(), checkedIds);
			request.getSession().setAttribute("successMessage", localization.getResourcesParam("success.usersremoved"));
			break;
		case "status":
			try {
				course.setStatus(CourseStatusEnum.valueOf(request.getParameter("status")));
			} catch (IllegalArgumentException e) {
				request.setAttribute("error", "error parsing status");
				return Constants.PAGE__ERROR;
			}
			courseService.updateCourse(course);
			break;
		case "givegrades":
			Map<String, String[]> grades = request.getParameterMap();
			Map<Integer, Integer> userGrade = new HashMap<>();
			for (Map.Entry<String, String[]> entry : grades.entrySet()) {
				if (entry.getKey().contains("grade")) {
					int userId = Integer.parseInt(entry.getKey().split("-")[1]);
					int grade = Integer.parseInt(entry.getValue()[0]);
					userGrade.put(userId, grade);
				}
			}

			userService.updateGrades(course.getId(), userGrade);
			
			String notify = request.getParameter("sendmail");
			if (notify != null && "on".equals(notify)) {
				for (Map.Entry<Integer, Integer> entry : userGrade.entrySet()) {
					user = userService.findUserById(entry.getKey());
					if (user != null) {
						try {
							Mailer.sendMail(new String[] { user.getEmail() },
									"Your grade was updated - " + entry.getValue(), "Grade info");
						} catch (MessagingException e) {
							e.printStackTrace();
						}
					}
				}
			}
			break;
		default:
			return Constants.PAGE__ERROR;
		}
		return "?" + (page == null ? "" : page);
	}

}
