package com.epam.project.command.impl.get;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.dao.DatabaseEnum;
import com.epam.project.entity.RoleEnum;
import com.epam.project.entity.User;
import com.epam.project.exceptions.DatabaseNotSupportedException;
import com.epam.project.service.IUserService;
import com.epam.project.service.ServiceFactory;

public class ManageStudentsPageCommand implements ICommand {
	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	public static ServiceFactory serviceFactory;
	public static IUserService userService;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || user.getRole() != RoleEnum.ADMIN) {
			try {
				response.sendRedirect(Constants.PAGE_LOGIN);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		List<User> users = userService.findAllUsers();

		request.setAttribute("users", users);
		return Constants.PATH_MANAGE_STUDENTS_PAGE;
	}
}
