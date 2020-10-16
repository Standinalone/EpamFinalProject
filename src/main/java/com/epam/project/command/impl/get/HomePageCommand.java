package com.epam.project.command.impl.get;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dto.CourseDto;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DBException;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.ITopicService;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;
import com.epam.project.util.Page;
import com.epam.project.util.QueryFactory;

/**
 * ICommand implementation for getting a home page
 *
 */
public class HomePageCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(HomePageCommand.class);

	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	private ServiceFactory serviceFactory;

	private ICourseService courseService;
	private IUserService userService;
	private ITopicService topicService;
	// Field for storing column names to prevent sql injection when using order by
	// `columnName`
	private static final Map<String, String> COLUMN_NAMES = new HashMap<>();

	/**
	 * Constructor used in testing by Mockito
	 * 
	 * @param serviceFactory mocked ServiceFactory
	 * @param courseService  mocked ICourseService
	 * @param userService    mocked IUserService
	 * @param topicService   mocked ITopicService
	 */
	private HomePageCommand(ServiceFactory serviceFactory, ICourseService courseService, IUserService userService,
			ITopicService topicService) {
		super();
		this.serviceFactory = serviceFactory;
		this.courseService = courseService;
		this.userService = userService;
		this.topicService = topicService;
	}

	public HomePageCommand() {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			courseService = serviceFactory.getCourseService();
			userService = serviceFactory.getUserService();
			topicService = serviceFactory.getTopicService();
		} catch (DatabaseNotSupportedException e) {
			log.error("DatabaseNotSupportedException", e.getMessage());
		}
		log.debug(String.valueOf(this.hashCode()));
	}

	static {
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
	public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {

		int topicId = 0, lecturerId = 0, statusId = 0;
		try {
			lecturerId = Integer.parseInt(request.getParameter("lecturer"));
		} catch (NumberFormatException e) {
			log.trace("Wrong format for lecturer");
		}
		try {
			topicId = Integer.parseInt(request.getParameter("topic"));
		} catch (NumberFormatException e) {
			log.trace("Wrong format for topic");
		}
		try {
			statusId = CourseStatusEnum.valueOf(request.getParameter("status")).ordinal() + 1;
		} catch (IllegalArgumentException | NullPointerException e) {
			log.trace("Wrong format for status");
		}
		Map<String, Integer> map = new HashMap<>();
		map.put("lecturer_id", lecturerId);
		map.put("topic_id", topicId);
		map.put("status_id", statusId);

		String conditions = QueryFactory.formExtraConditionQuery(map);
		String orderBy = QueryFactory.formOrderByQuery(COLUMN_NAMES, request);
		User user = (User) request.getSession().getAttribute("user");
		Page<CourseDto> page = new Page<>(request,
				(limit, offset) -> courseService.findAllCoursesDtoWithParametersFromTo(limit, offset, conditions,
						orderBy, user == null ? 0 : user.getId()),
				() -> courseService.findAllCoursesWithParametersCount(conditions));

		List<User> lecturers;
		lecturers = userService.findAllUsersByRole(3);

		request.setAttribute("topics", topicService.findAllTopics());

		request.setAttribute("page", page);
		request.setAttribute("lecturers", lecturers);
		List<String> statuses = Stream.of(CourseStatusEnum.values())
                .map(Enum::name)
                .collect(Collectors.toList());
		request.setAttribute("statuses", statuses);
		return Constants.PAGE__HOME;
	}

}
