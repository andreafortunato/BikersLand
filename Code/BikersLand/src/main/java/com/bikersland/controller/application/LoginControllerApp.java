package com.bikersland.controller.application;

import java.sql.SQLException;

import com.bikersland.Main;
import com.bikersland.db.UserDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.InvalidLoginException;
import com.bikersland.model.User;
import com.bikersland.singleton.LoginSingleton;

public class LoginControllerApp {
	
	private LoginControllerApp() {};

	public static void askLogin(String username_email, String password) throws InvalidLoginException, InternalDBException {
		User logged_user;
		
		try {
			logged_user = UserDAO.askLogin(username_email, password);
			LoginSingleton.getLoginInstance().setUser(logged_user);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString("ex_internal_db_error"), sqle, "askLogin", "LoginControllerApp.java");
		}
	}
	
	public static void loginJustRegisteredUser(User user) {
		LoginSingleton.getLoginInstance().setUser(user);
	}
	
}
