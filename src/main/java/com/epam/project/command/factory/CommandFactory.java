package com.epam.project.command.factory;

import javax.servlet.http.HttpServletRequest;

import com.epam.project.command.CommandEnum;
import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;

public class CommandFactory {

	public static ICommand getCommand(HttpServletRequest request) {
		String command = request.getParameter(Constants.PARAM_COMMAND);
		ICommand resultCommand = null;
		if (command != null) {

			try {
				resultCommand = CommandEnum.valueOf(command).getCommand();
			} catch (IllegalArgumentException ex) {
				resultCommand = CommandEnum.ERROR_PAGE.getCommand();
				ex.printStackTrace();
			}
		} else {
			resultCommand = CommandEnum.ERROR_PAGE.getCommand();
		}
		return resultCommand;
	}
}