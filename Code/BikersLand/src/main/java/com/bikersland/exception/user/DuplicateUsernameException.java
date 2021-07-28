package com.bikersland.exception.user;

public class DuplicateUsernameException extends UsernameException {

	public DuplicateUsernameException() {
		super();
	}

	public DuplicateUsernameException(String message) {
		super(message);
	}

}
