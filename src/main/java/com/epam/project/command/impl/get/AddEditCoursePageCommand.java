package com.epam.project.command.impl.get;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.ICourseDAO;
import com.epam.project.dao.ICourseHomePageDAO;
import com.epam.project.dao.ITokenDAO;
import com.epam.project.dao.ITopicDAO;
import com.epam.project.dao.IUserDAO;
import com.epam.project.dto.CourseHomePageDto;
import com.epam.project.entity.Course;
import com.epam.project.entity.CourseStatusEnum;
import com.epam.project.exceptions.DatabaseNotSupportedException;

public class AddEditCoursePageCommand implements ICommand {
	public static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	public static DaoFactory daoFactory;
	public static ICourseHomePageDAO courseHomePageDao;
	public static IUserDAO userDao;
	public static ITopicDAO topicDao;

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(db);
			courseHomePageDao = daoFactory.getCourseHomePageDAO();
			userDao = daoFactory.getUserDAO();
			topicDao = daoFactory.getTopicDAO();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		int courseId;
		if (id != null && !id.isEmpty()) {
			try {
				courseId = Integer.parseInt(id);
				CourseHomePageDto courseDto = courseHomePageDao.findById(courseId);
				if (courseDto != null) {
					request.getSession().setAttribute("course", courseDto);
				}
				else {
					try {
						request.getSession().setAttribute("error", "Course not found :(");
						response.sendRedirect(Constants.PAGE_ERROR);
						return;
					} catch (IOException e1) {
						e1.printStackTrace();
						return;
					}
				}
			}
			catch(NumberFormatException e) {
				e.printStackTrace();
				try {
					request.getSession().setAttribute("error", "Cannot parse id :(");
					response.sendRedirect(Constants.PAGE_ERROR);
					return;
				} catch (IOException e1) {
					e1.printStackTrace();
					return;
				}
			}
		}
		request.getSession().setAttribute("lecturers", userDao.findAllByRole(3));
		request.getSession().setAttribute("topics", topicDao.findAll());
		request.getSession().setAttribute("statuses", CourseStatusEnum.values());
		try {
			request.getRequestDispatcher(Constants.PATH_ADD_COURSE_PAGE).forward(request, response);
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}
	}

}
