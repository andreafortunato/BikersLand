package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bikersland.Main;
import com.bikersland.db.UserDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.model.User;
import com.bikersland.singleton.LoginSingleton;

import UserDAO.InvalidLoginException;

public class LoginControllerApp {
	
	private LoginControllerApp() {};

	public static void askLogin(String username_email, String password) throws InvalidLoginException, InternalDBException {
		User logged_user;
		
		try {
			logged_user = UserDAO.askLogin(username_email, password);
			LoginSingleton.getLoginInstance().setUser(logged_user);
		} catch (SQLException sqle) {
			Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in askLogin() function, inside LoginControllerApp.java", sqle);
			
			throw new InternalDBException(Main.bundle.getString("ex_internal_db_error"));
		}
	}
	
	public static void loginJustRegisteredUser(User user) {
		LoginSingleton.getLoginInstance().setUser(user);
	}
	
}
