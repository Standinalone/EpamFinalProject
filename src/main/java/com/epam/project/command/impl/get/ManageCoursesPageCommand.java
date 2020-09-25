package com.epam.project.command.impl.get;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DaoFactory;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.dao.ICourseHomePageDAO;
import com.epam.project.dao.IUserDAO;
import com.epam.project.dto.CourseHomePageDto;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.ICourseService;
import com.epam.project.service.ServiceFactory;

public class ManageCoursesPageCommand implements ICommand {
	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	private static DaoFactory daoFactory;
	private static ICourseHomePageDAO coursesDao;
	private static ServiceFactory serviceFactory;
	private static ICourseService courseService;

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(db);
			coursesDao = daoFactory.getCourseHomePageDAO();
			serviceFactory = ServiceFactory.getServiceFactory(db);
			courseService = serviceFactory.getCourseService();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
//		User user = (User) request.getSession().getAttribute("user");
//		if (user == null || user.getRole() != RoleEnum.ADMIN) {
//			try {
//				response.sendRedirect(Constants.PAGE_LOGIN);
//				return;
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		int count = courseService.getCoursesCount();
		request.setAttribute("totalPages", Math.ceil((double)count / Constants.PAGE_SIZE));
		
		int pageNum = 0;
		if (request.getParameter("pagenum") != null) {
			pageNum = Integer.parseInt(request.getParameter("pagenum")) - 1;
		}
		
		List<CourseHomePageDto> courses = coursesDao.findAllCoursesHomePageFromTo(Constants.PAGE_SIZE, pageNum * Constants.PAGE_SIZE);
		request.setAttribute("courses",  courses);
		try {
			request.getRequestDispatcher(Constants.PATH_MANAGE_COURSES_PAGE).forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

}
