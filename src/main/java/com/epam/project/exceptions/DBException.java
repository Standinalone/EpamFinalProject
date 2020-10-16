package com.epam.project.exceptions;

/**
 * Base exception used to indicate that SQLException was thrown while working
 * with database
 *
 */
public class DBException extends Exception {

	private static final long serialVersionUID = 2491889503864935486L;

	public DBException(String message) {
		super(message);
	}

	public DBException(String message, Throwable e) {
		super(message, e);
	}
}
