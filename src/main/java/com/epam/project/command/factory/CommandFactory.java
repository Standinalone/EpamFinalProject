package com.epam.project.command.factory;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.CommandEnum;
import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;

public class CommandFactory {
	private static final Logger log = LoggerFactory.getLogger(CommandFactory.class);

	public static ICommand getCommand(HttpServletRequest request) {
		String command = request.getParameter(Constants.PARAM_COMMAND);
		ICommand resultCommand = null;
		if (command != null) {

			try {
				resultCommand = CommandEnum.valueOf(command).getCommand();
			} catch (IllegalArgumentException ex) {
				log.error("Command {} not found", resultCommand);
				resultCommand = CommandEnum.ERROR_PAGE.getCommand();
				ex.printStackTrace();
			}
		} else {
			log.error("Cannot parse command");
			resultCommand = CommandEnum.ERROR_PAGE.getCommand();
		}
		return resultCommand;
	}
}