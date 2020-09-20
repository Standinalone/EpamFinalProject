package com.epam.project.command.impl.post;

import java.io.IOException;

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

public class JoinCoursesCommand implements ICommand {
	private static DatabaseEnum db = DatabaseEnum.valueOf(Constants.DATABASE);
	private static ServiceFactory serviceFactory;
	private static IUserService userService;

	static {
		try {
			serviceFactory = ServiceFactory.getServiceFactory(db);
			userService = serviceFactory.getUserService();
		} catch (DatabaseNotSupportedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			try {
				response.sendRedirect(Constants.PAGE_LOGIN);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String[] checkedIds = request.getParameterValues("courses");
		for (String id : checkedIds) {
			userService.enrollToCourse(Integer.parseInt(id), user.getId());
		}
		try {
			response.sendRedirect(Constants.PAGE_HOME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
