package com.bikersland.exception.user;

public class DuplicateEmailException extends EmailException {

	public DuplicateEmailException() {
		super();
	}

	public DuplicateEmailException(String message) {
		super(message);
	}

}
