package com.epam.project.exceptions;

/**
 * Exception used to indicate that SQLException was thrown while working with
 * topics
 *
 */
public class DBTopicException extends DBException {
	
	private static final long serialVersionUID = -7828324979862688484L;

	public DBTopicException(String message, Throwable e) {
		super(message, e);
	}

}
