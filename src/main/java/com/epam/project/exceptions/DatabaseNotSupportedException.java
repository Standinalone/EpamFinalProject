package com.epam.project.exceptions;

public class DatabaseNotSupportedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6696128958106637283L;

	public DatabaseNotSupportedException(String message) {
		super(message);
	}
}
