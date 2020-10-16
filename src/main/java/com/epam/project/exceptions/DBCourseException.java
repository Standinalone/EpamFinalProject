package com.epam.project.exceptions;

/**
 * Exception used to indicate that SQLException was thrown while working with
 * courses
 *
 */
public class DBCourseException extends DBException {

	private static final long serialVersionUID = -8042827965325863022L;

	public DBCourseException(String message, Throwable e) {
		super(message, e);
	}

	public DBCourseException(String message) {
		super(message);
	}
}
