package com.epam.project.command.impl.post;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.l10n.Localization;
import com.epam.project.service.ICourseService;
import com.epam.project.service.ITopicService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

public class AddEditCourseCommand implements ICommand {
	public static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	public static IUserService userService;
	public static ITopicService topicService;
	public static ICourseService courseService;
	public static ServiceFactory serviceFactory;

	private Course course;
	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
			topicService = serviceFactory.getTopicService();
			courseService = serviceFactory.getCourseService();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String courseId = request.getParameter("id");
		request.getSession().setAttribute("errors", null);
		List<String> mappingErrors = mapToCourse(request, courseId);
		if (!mappingErrors.isEmpty()) {
			request.getSession().setAttribute("errors", mappingErrors);
			return Constants.COMMAND__ADD_EDIT_COURSE;
		}
		List<String> errors = validate(course);

		if (!errors.isEmpty()) {
			request.getSession().setAttribute("successMessage", null);
			request.getSession().setAttribute("errors", errors);
			if (courseId != null && !courseId.isEmpty())
				return Constants.COMMAND__ADD_EDIT_COURSE + "&id=" + request.getParameter("id").toString();
			return Constants.COMMAND__ADD_EDIT_COURSE;
		}

		if (courseId != null && !courseId.isEmpty()) {
			if (!courseService.updateCourse(course)) {
				request.getSession().setAttribute("error", "Error updating course");
				return Constants.COMMAND__ERROR;
			}
		} else {
			if (!courseService.addCourse(course)) {
				request.getSession().setAttribute("error", "Error adding course");
				return Constants.COMMAND__ERROR;
			}
		}
		final HttpSession s = request.getSession();
		List<User> list = (List<User> ) s.getAttribute("asd" );
		list = new ArrayList<>();
		list.add(new User());
		request.getSession().setAttribute("list" , list);
		
//		Localization localization = new Localization((Locale) request.getSession().getAttribute("locale"));

		Localization localization = (Localization) request.getSession().getAttribute("localization");
		
		request.getSession().setAttribute("successMessage", localization.getResourcesParam("success.updated"));

		return Constants.COMMAND__ADD_EDIT_COURSE + "&id=" + course.getId();
	}

	private List<String> mapToCourse(HttpServletRequest request, String courseId) {
		List<String> errors = new ArrayList<>();
		course = new Course();
		if (courseId != null && !courseId.isEmpty()) {
			try {
				int id = Integer.parseInt(request.getParameter("id"));
				course.setId(id);
			} catch (NumberFormatException e) {
				errors.add("Error parsing id");
			}
		}

		try {
			LocalDate startDate = LocalDate.parse(request.getParameter("startdate"));
			LocalDate endDate = LocalDate.parse(request.getParameter("enddate"));
			course.setStartDate(startDate);
			course.setEndDate(endDate);
		} catch (DateTimeParseException e) {
			errors.add("Error parsing date");
		}
		try {
			course.setLecturerId(Integer.parseInt(request.getParameter("lecturer")));
		} catch (NumberFormatException e) {
			errors.add("Error parsing lecturer");
		}
		try {
			course.setStatus(CourseStatusEnum.valueOf(request.getParameter("status")));
		} catch (IllegalArgumentException e) {
			errors.add("Error parsing status");
		}
		String name = request.getParameter("name");
		course.setTopicId(Integer.parseInt(request.getParameter("topic")));
		course.setName(name);
		return errors;
	}

	private List<String> validate(Course course) {
		List<String> errors = new ArrayList<>();
		if (topicService.findTopicById(course.getTopicId()) == null) {
			errors.add("topic not found");
		}
		User user = userService.findUserById(course.getLecturerId());
		if (user == null) {
			errors.add("lecturer not found");
		}
		if (user != null && user.getRole() != RoleEnum.LECTURER) {
			errors.add("Not a lecturer");
		}
		if (course.getEndDate().isBefore(course.getStartDate())) {
			errors.add("Date error");
		}
		return errors;
	}

}
