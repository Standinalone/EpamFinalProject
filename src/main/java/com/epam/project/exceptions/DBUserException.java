package com.epam.project.exceptions;

/**
 * Exception used to indicate that SQLException was thrown while working with
 * users
 *
 */
public class DBUserException extends DBException{
	
	private static final long serialVersionUID = -5225735802435073533L;

	public DBUserException(String message, Throwable e) {
		super(message, e);
	}

}
