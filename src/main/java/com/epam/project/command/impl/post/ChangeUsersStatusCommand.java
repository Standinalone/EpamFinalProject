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

public class ChangeUsersStatusCommand implements ICommand {
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
		if (user == null || user.getRole() != RoleEnum.ADMIN) {
			try {
				response.sendRedirect(Constants.PAGE_LOGIN);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String[] checkedIds = request.getParameterValues("users");
		String action = request.getParameter("submit");

		if ("block".equals(action)) {
			for (String id : checkedIds) {
				userService.blockUser(Integer.parseInt(id));
			}
		} else {
			for (String id : checkedIds) {
				userService.unblockUser(Integer.parseInt(id));
			}
		}
		try {
			response.sendRedirect(Constants.PAGE_MANAGE_STUDENTS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("ChangeUsersStatusCommand");
	}

}
