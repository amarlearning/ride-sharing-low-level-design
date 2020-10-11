package com.company.exception;

public class DuplicateRideIDException extends RuntimeException {

	public DuplicateRideIDException(final String message) {
		super(message);
	}
}
