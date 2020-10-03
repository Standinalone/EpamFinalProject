package com.epam.project.command.impl.post;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
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
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.i18n.Localization;
import com.epam.project.service.ICourseService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;
import com.epam.project.mailer.Mailer;

public class LecturerPanelCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(LecturerPanelCommand.class);
	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	private static ServiceFactory serviceFactory;
	private static IUserService userService;
	private static ICourseService courseService;
	private Localization localization;
	private User user;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
			courseService = serviceFactory.getCourseService();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public List<String> validate(Course course) {
		List<String> errors = new ArrayList<>();
		if (course == null) {
			errors.add(localization.getResourcesParam("error.cannotfind"));
		}
		if (course.getLecturerId() != user.getId()) {
			errors.add(localization.getResourcesParam("error.notyourcourse"));
		}
		return errors;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		localization = (Localization) request.getSession().getAttribute("localization");
		user = (User) request.getSession().getAttribute("user");
		String page = request.getParameter("page");
		int courseId = 0;
		try {
			courseId = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			log.error("Cannot parse id");
			request.getSession().setAttribute("error", localization.getResourcesParam("error.badid"));
			return Constants.COMMAND__ERROR;
		}
		Course course = courseService.findCourseById(courseId);
		List<String> validateErrors = validate(course);
		if (!validateErrors.isEmpty()) {
			log.error("Error validating course");
			request.getSession().setAttribute("errors", validateErrors);
			return "?" + (page == null ? "" : page);
		}

		String[] checkedIds = request.getParameterValues("users");
		String action = request.getParameter("submit");

		switch (action) {
		case "decline":
			userService.declineRequestForIds(course.getId(), checkedIds);
			request.getSession().setAttribute("successMessage", localization.getResourcesParam("success.declined"));
			log.info("Requests to course {} were declined by {}", course.getName(), user.getLogin());
			break;
		case "add":
			userService.registerInCourseByUsersIds(course.getId(), checkedIds, true);
			request.getSession().setAttribute("successMessage", localization.getResourcesParam("success.usersadded"));
			log.info("Registered in course {} by {}", course.getName(), user.getLogin());
			break;
		case "remove":
			userService.declineRequestForIds(course.getId(), checkedIds);
			request.getSession().setAttribute("successMessage", localization.getResourcesParam("success.usersremoved"));
			log.info("Removed from course {} by {}", course.getName(), user.getLogin());
			break;
		case "status":
			try {
				course.setStatus(CourseStatusEnum.valueOf(request.getParameter("status")));
			} catch (IllegalArgumentException e) {
				log.error("Cannot parse status");
				request.getSession().setAttribute("error", localization.getResourcesParam("success.badStatus"));
				return Constants.PAGE__ERROR;
			}
			try {
				courseService.updateCourse(course);
				request.getSession().setAttribute("successMessage", localization.getResourcesParam("success.updated"));
				log.info("Status of course {} updated by {}", course.getName(), user.getLogin());
			} catch (SQLException e1) {
				log.error("Error updating course");
			}

			break;
		case "givegrades":
			Map<String, String[]> grades = request.getParameterMap();
			Map<Integer, Integer> userGrade = new HashMap<>();
			for (Map.Entry<String, String[]> entry : grades.entrySet()) {
				if (entry.getKey().contains("grade")) {
					int userId = Integer.parseInt(entry.getKey().split("-")[1]);
					int grade = Integer.parseInt(entry.getValue()[0]);
					if (grade < 0 || grade > 100) {
						request.getSession().setAttribute("error", localization.getResourcesParam("error.badGrade"));
						return Constants.COMMAND__ERROR;
					}
					userGrade.put(userId, grade);
				}
			}

			userService.updateGrades(course.getId(), userGrade);

			log.info("Grades in course {} were given by {}", course.getName(), user.getLogin());

			String notify = request.getParameter("sendmail");
			if (notify != null && "on".equals(notify)) {
				for (Map.Entry<Integer, Integer> entry : userGrade.entrySet()) {
					user = userService.findUserById(entry.getKey());
					if (user != null) {
						try {
							Mailer.sendMail(new String[] { user.getEmail() },
									"Your grade was updated - " + entry.getValue(), "Grade info");
						} catch (MessagingException e) {
							log.error("Error sending message to {}", user.getLogin());
							e.printStackTrace();
						}
					}
				}
			}
			request.getSession().setAttribute("successMessage",
					localization.getResourcesParam("success.gradesupdated"));
			break;
		default:
			log.error("Uknown command");
			return Constants.PAGE__ERROR;
		}
		return "?" + (page == null ? "" : page);
	}

}
