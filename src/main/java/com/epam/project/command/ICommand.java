package com.epam.project.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.project.exceptions.DBException;
import com.epam.project.exceptions.ValidatingRequestException;


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
	 * @throws ValidatingRequestException 
	 * @throws DBException 
	 */
	String execute(HttpServletRequest request, HttpServletResponse response) throws DBException, ValidatingRequestException;

}
