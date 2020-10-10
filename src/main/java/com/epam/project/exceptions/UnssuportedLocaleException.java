package com.epam.project.exceptions;

/**
 * Class used to indicate that a locale isn't supported
 *
 */
public class UnssuportedLocaleException extends Exception{

	private static final long serialVersionUID = -5912754996114666340L;

	public UnssuportedLocaleException(String message) {
		super(message);
	}
}
