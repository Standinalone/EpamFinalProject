package com.epam.project.command.impl.post;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.i18n.Localization;
import com.epam.project.i18n.LocalizationFactory;
import com.epam.project.service.ICourseService;
import com.epam.project.service.ITopicService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

/**
 * ICommand implementation for `add or edit a course` command
 *
 */
public class AddEditCourseCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(AddEditCourseCommand.class);
	public static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	public static IUserService userService;
	public static ITopicService topicService;
	public static ICourseService courseService;
	public static ServiceFactory serviceFactory;

	private Course course;
	private Localization localization;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
			topicService = serviceFactory.getTopicService();
			courseService = serviceFactory.getCourseService();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		localization = LocalizationFactory.getLocalization((Locale) request.getSession().getAttribute("locale"));
		String page = request.getParameter("page");

		List<String> mappingErrors = mapToCourse(request);
		if (!mappingErrors.isEmpty()) {
			log.error("Error mapping to course");
			request.getSession().setAttribute("errors", mappingErrors);
			return Constants.COMMAND__ADD_EDIT_COURSE;
		}
		List<String> errors = validate(course);

		String courseId = request.getParameter("id");
		if (!errors.isEmpty()) {
			log.error("Error validating course");
			request.getSession().setAttribute("errors", errors);
			return "?" + (page == null ? "" : page);
		}

		if (courseId != null && !courseId.isEmpty()) {
			if (!courseService.updateCourse(course)) {
				log.error("Error updating course");
				request.getSession().setAttribute("error", localization.getResourcesParam("error.updating"));
				return Constants.COMMAND__ERROR;
			}
		} else {
			if (!courseService.addCourse(course)) {
				log.error("Error adding course");
				request.getSession().setAttribute("error", localization.getResourcesParam("error.adding"));
				return Constants.COMMAND__ERROR;
			}
		}
		request.getSession().setAttribute("successMessage", localization.getResourcesParam("success.updated"));

		log.info("Course {} edited by {}", course.getName(),
				((User) request.getSession().getAttribute("user")).getLogin());
		return Constants.COMMAND__ADD_EDIT_COURSE + "&id=" + course.getId();
	}

	private List<String> mapToCourse(HttpServletRequest request) {
		List<String> errors = new ArrayList<>();
		String courseId = request.getParameter("id");
		course = new Course();
		if (courseId != null && !courseId.isEmpty()) {
			try {
				int id = Integer.parseInt(request.getParameter("id"));
				course.setId(id);
			} catch (NumberFormatException e) {
				errors.add(localization.getResourcesParam("error.badid"));
			}
		}

		try {
			LocalDate startDate = LocalDate.parse(request.getParameter("startdate"));
			LocalDate endDate = LocalDate.parse(request.getParameter("enddate"));
			course.setStartDate(startDate);
			course.setEndDate(endDate);
		} catch (DateTimeParseException e) {
			errors.add(localization.getResourcesParam("error.badDate"));
		}

		try {
			course.setLecturerId(Integer.parseInt(request.getParameter("lecturer")));
		} catch (NumberFormatException e) {
			errors.add(localization.getResourcesParam("error.lecturer"));
		}
		try {
			course.setStatus(CourseStatusEnum.valueOf(request.getParameter("status")));
		} catch (IllegalArgumentException e) {
			errors.add(localization.getResourcesParam("error.badStatus"));
		}
		String name = request.getParameter("name");
		if (name == null) {
			errors.add(localization.getResourcesParam("error.emptyName"));
		}
		course.setName(name);
		try {
			course.setTopicId(Integer.parseInt(request.getParameter("topic")));
		} catch (NumberFormatException e) {
			errors.add(localization.getResourcesParam("error.badTopic"));
		}
		return errors;
	}

	private List<String> validate(Course course) {
		List<String> errors = new ArrayList<>();
		if (topicService.findTopicById(course.getTopicId()) == null) {
			errors.add(localization.getResourcesParam("error.topicnotfound"));
		}
		User user = null;
		try {
			user = userService.findUserById(course.getLecturerId());
		} catch (SQLException e) {
			log.error("Finding user error", e);
			errors.add(localization.getResourcesParam("dberror.finduser"));
		}
		if (user == null) {
			errors.add(localization.getResourcesParam("error.lecturernotfound"));
		}
		if (user != null && user.getRole() != RoleEnum.LECTURER) {
			errors.add(localization.getResourcesParam("error.notlecturer"));
		}
		if (course.getEndDate().isBefore(course.getStartDate())) {
			errors.add(localization.getResourcesParam("error.badDate"));
		}
		return errors;
	}

}
