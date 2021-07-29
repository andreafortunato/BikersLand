package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bikersland.Main;
import com.bikersland.bean.UserBean;
import com.bikersland.db.UserDAO;
import com.bikersland.exception.AutomaticLoginException;
import com.bikersland.exception.ImageConversionException;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.user.DuplicateEmailException;
import com.bikersland.exception.user.DuplicateUsernameException;
import com.bikersland.exception.user.UserNotFoundException;
import com.bikersland.model.User;

public class RegisterControllerApp {
	
	private RegisterControllerApp() {}
	
	public static void register(UserBean userBean) throws DuplicateUsernameException, DuplicateEmailException, AutomaticLoginException, InternalDBException {
		User newUser = new User();
		newUser.setName(userBean.getName());
		newUser.setSurname(userBean.getSurname());
		newUser.setUsername(userBean.getUsername());
		newUser.setEmail(userBean.getEmail());
		newUser.setPassword(userBean.getPassword());
		newUser.setImage(userBean.getImage());
		
		try {
			UserDAO.createNewUser(newUser);
			
			try {
				User loggedUser = UserDAO.getUserByUsername(newUser.getUsername());
				LoginControllerApp.loginJustRegisteredUser(loggedUser);
			} catch (SQLException | ImageConversionException | UserNotFoundException sqle) {
				Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in register() function, inside RegisterControllerApp.java", sqle);
				
				throw new AutomaticLoginException(Main.bundle.getString("ex_failed_autologin"));
			}
			
		} catch (SQLException | ImageConversionException e) {
			Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in register() function, inside RegisterControllerApp.java", e);
			
			throw new InternalDBException(Main.bundle.getString("ex_internal_db_error"));
		}
	}
}
