package com.epam.project.command.impl.get;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dto.CourseDto;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.entity.Topic;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.i18n.Localization;
import com.epam.project.i18n.LocalizationFactory;
import com.epam.project.service.ICourseService;
import com.epam.project.service.ITopicService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;
import com.epam.project.util.Page;
import com.epam.project.util.QueryFactory;

/**
 * ICommand implementation for getting a page to manage courses
 *
 */
public class ManageCoursesPageCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(ManageCoursesPageCommand.class);

	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	private static ServiceFactory serviceFactory;
	private static ICourseService courseService;
	private static IUserService userService;
	private static ITopicService topicService;
	private static final Map<String, String> COLUMN_NAMES = new HashMap<>();

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			courseService = serviceFactory.getCourseService();
			userService = serviceFactory.getUserService();
			topicService = serviceFactory.getTopicService();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
		COLUMN_NAMES.put("students", "students");
		COLUMN_NAMES.put("name", "courses.name");
		COLUMN_NAMES.put("name", "courses.name");
		COLUMN_NAMES.put("topic", "topics.name");
		COLUMN_NAMES.put("lecturer", "users.name");
		COLUMN_NAMES.put("startdate", "courses.start_date");
		COLUMN_NAMES.put("enddate", "courses.end_date");
		COLUMN_NAMES.put("duration", "duration");
		COLUMN_NAMES.put("status", "statuses.name");
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {

		Localization localization = LocalizationFactory.getLocalization((Locale) request.getSession().getAttribute("locale"));
		int topicId = 0, lecturerId = 0, statusId = 0;
		try {
			lecturerId = Integer.parseInt(request.getParameter("lecturer"));
		} catch (NumberFormatException e) {
			log.trace("Cannot parse lecturer");
		}
		try {
			topicId = Integer.parseInt(request.getParameter("topic"));
		} catch (NumberFormatException e) {
			log.trace("Cannot parse topic");
		}
		try {
			statusId = CourseStatusEnum.valueOf(request.getParameter("status")).ordinal() + 1;
		} catch (IllegalArgumentException | NullPointerException e) {
			log.trace("Cannot parse status");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("lecturer_id", lecturerId);
		map.put("topic_id", topicId);
		map.put("status_id", statusId);

		String conditions = QueryFactory.formExtraConditionQuery(map);
		String orderBy = QueryFactory.formOrderByQuery(COLUMN_NAMES, request);

		Page<CourseDto> page = new Page<>(request, (limit, offset) -> courseService
				.findAllCoursesDtoFromToWithParameters(limit, offset, conditions, orderBy),
				() -> courseService.getCoursesWithParametersCount(conditions));

		if (page.getList() == null) {

			log.error("Page cannot be formed");
			request.setAttribute("error", localization.getResourcesParam("error.badid"));
			return Constants.PAGE__ERROR;
		}

		List<User> lecturers = userService.findAllUsersByRole(3);
		List<Topic> topics = topicService.findAllTopics();
		request.setAttribute("lecturers", lecturers);
		request.setAttribute("topics", topics);
		request.setAttribute("statuses", CourseStatusEnum.values());
		request.setAttribute("page", page);

		return Constants.PAGE__MANAGE_COURSES;
	}
}
