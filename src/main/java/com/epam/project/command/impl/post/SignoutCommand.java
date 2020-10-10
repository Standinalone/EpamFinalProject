package com.epam.project.command.impl.post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.project.command.ICommand;
import com.epam.project.constants.Constants;
import com.epam.project.entity.User;

/**
 * ICommand implementation for a `signout` command
 *
 */
public class SignoutCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(SignoutCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession() != null) {
			log.info("User {} was signed out", ((User) request.getSession().getAttribute("user")).getLogin());
			request.getSession().invalidate();
		}
		return Constants.COMMAND__HOME;
	}

}
