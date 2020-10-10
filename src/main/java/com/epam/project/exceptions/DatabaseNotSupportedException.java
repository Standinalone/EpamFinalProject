package com.epam.project.exceptions;

/**
 * DatabaseNotSupportedException class used to indicate that the database set in
 * the project configuration isn't supported
 *
 */
public class DatabaseNotSupportedException extends Exception {

	private static final long serialVersionUID = 6696128958106637283L;

	public DatabaseNotSupportedException(String message) {
		super(message);
	}
}
