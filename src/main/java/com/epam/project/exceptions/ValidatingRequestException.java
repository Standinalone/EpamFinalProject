package com.epam.project.exceptions;

public class ValidatingRequestException extends Exception {

	private static final long serialVersionUID = -4812938858625782748L;

	public ValidatingRequestException(String message) {
		super(message);
	}
	
	public ValidatingRequestException(String message, Throwable e) {
		super(message, e);
	}
}
