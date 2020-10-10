package com.company.exception;

public class RideNotFoundException extends RuntimeException {

	public RideNotFoundException(final String message) {
		super(message);
	}

}
