package com.bikersland.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

public class InternalDBException extends Exception {

	public InternalDBException() {
		super();
		Logger.getGlobal().log(Level.SEVERE, "Catched an unknown internal Exception");
	}

	public InternalDBException(String message) {
		super(message);
		Logger.getGlobal().log(Level.SEVERE, "Catched an unknown internal Exception");
	}
	
	public InternalDBException(String message, Throwable e, String method, String file) {
		super(message);
		Logger.getGlobal().log(Level.SEVERE, "Catched Exception in " + method + " method, inside " + file, e);
	}

}
