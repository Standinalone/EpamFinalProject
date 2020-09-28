package com.epam.project.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.command.ICommand;
import com.epam.project.command.factory.CommandFactory;

public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		String forward = handleRequest(request, response);
		if (forward != null) {
			try {
				request.getRequestDispatcher(forward).forward(request, response);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String redirect = handleRequest(request, response);
		if (redirect != null) {
			try {
				response.sendRedirect(request.getRequestURL().append(redirect).toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String handleRequest(HttpServletRequest request, HttpServletResponse response) {
		ICommand command = CommandFactory.getCommand(request);
		return command.execute(request, response);
	}
}
