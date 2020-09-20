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
import com.epam.project.dao.IUserDAO;
import com.epam.project.dto.CourseProfilePageDto;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

public class ManageStudentsPageCommand implements ICommand {
	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	private static ServiceFactory serviceFactory;
	private static DaoFactory daoFactory;
	private static IUserService userService;
	private static IUserDAO userDao;

	static {
		try {
			daoFactory = DaoFactory.getDaoFactory(db);
			userDao = daoFactory.getUserDAO();
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || user.getRole() != RoleEnum.ADMIN) {
			try {
				response.sendRedirect(Constants.PAGE_LOGIN);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		List<UserDto> users = userService.getUsersWithRoles();
		List<User> users = userDao.findAll();
		request.setAttribute("users",  users);
		try {
			request.getRequestDispatcher(Constants.PATH_MANAGE_STUDENTS_PAGE).forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

}
