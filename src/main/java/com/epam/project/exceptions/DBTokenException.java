package com.epam.project.exceptions;

/**
 * Exception used to indicate that SQLException was thrown while working with
 * tokens
 *
 */
public class DBTokenException extends DBException {

	private static final long serialVersionUID = 1078652444459965377L;

	public DBTokenException(String message, Throwable e) {
		super(message, e);
	}

}
