package com.epam.project.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * The main interface for all commands
 *
 */
public interface ICommand {

	/**
	 * Executes specific command
	 * 
	 * @param request incoming request 
	 * @param response
	 * @return either forward or redirect string
	 */
	String execute(HttpServletRequest request, HttpServletResponse response);

}
