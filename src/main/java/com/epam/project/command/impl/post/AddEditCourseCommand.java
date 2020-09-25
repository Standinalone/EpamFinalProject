package com.epam.project.command.impl.post;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.ICourseDAO;
import com.epam.project.dao.ICourseHomePageDAO;
import com.epam.project.dao.ITopicDAO;
import com.epam.project.dao.IUserDAO;
import com.epam.project.dto.CourseHomePageDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.Topic;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.l10n.Localization;

public class AddEditCourseCommand implements ICommand {
	public static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	public static DaoFactory daoFactory;
	public static ICourseHomePageDAO courseHomePageDao;
	public static ITopicDAO topicDao;
	public static IUserDAO userDao;
	public static ICourseDAO courseDao;

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(db);
			courseHomePageDao = daoFactory.getCourseHomePageDAO();
			topicDao = daoFactory.getTopicDAO();
			userDao = daoFactory.getUserDAO();
			courseDao = daoFactory.getCourseDAO();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		Course course = mapToCourseDTO(request);
		if (course == null) {
			try {
				request.setAttribute("error", "Error updating");
				response.sendRedirect(Constants.PAGE_ERROR);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		List<String> errors = validate(course);
		if (errors.isEmpty()) {
			request.getSession().setAttribute("errors", null);
			if (courseDao.update(course)) {
				try {
					Localization localization = new Localization((Locale) request.getSession().getAttribute("locale"));
					
					request.getSession().setAttribute("successMessage", localization.getMessagesParam("update.success"));
					response.sendRedirect(
							Constants.PAGE_ADD_EDIT_COURSE + "&id=" + request.getParameter("id").toString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					request.setAttribute("error", "Error updating");
					response.sendRedirect(Constants.PAGE_ERROR);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			request.getSession().setAttribute("successMessage", null);
			request.getSession().setAttribute("errors", errors);
			try {
				response.sendRedirect(Constants.PAGE_ADD_EDIT_COURSE + "&id=" + request.getParameter("id").toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Course mapToCourseDTO(HttpServletRequest request) {
		Course course = new Course();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			String name = request.getParameter("name");
			course.setTopic_id(Integer.parseInt(request.getParameter("topic")));
			LocalDate startDate = LocalDate.parse(request.getParameter("startdate"));
			LocalDate endDate = LocalDate.parse(request.getParameter("enddate"));
			course.setLecturer_id(Integer.parseInt(request.getParameter("lecturer")));
			course.setEndDate(endDate);
			course.setId(id);
			course.setName(name);
			course.setStartDate(startDate);
			course.setStatus(CourseStatusEnum.valueOf(request.getParameter("status")));
		} catch (Exception e) {
			e.printStackTrace();
			course = null;
		}
		return course;
	}

	private List<String> validate(Course course) {
		List<String> errors = new ArrayList<>();
		if (topicDao.findById(course.getTopic_id()) == null) {
			errors.add("topic not found");
		}
		User user = userDao.findUserById(course.getLecturer_id());
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
