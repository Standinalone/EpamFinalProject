package com.epam.project.exceptions;

public class DBException extends Exception {

	private static final long serialVersionUID = 2491889503864935486L;

	public DBException(String message) {
		super(message);
	}
	
	public DBException(String message, Throwable e) {
		super(message, e);
	}
}
