package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import com.bikersland.Main;
import com.bikersland.db.CityDAO;
import com.bikersland.db.TagDAO;
import com.bikersland.exception.InternalDBException;

public class MainControllerApp {

	public static List<String> getTags() throws InternalDBException {
			try {
				if(Main.getLocale() == Locale.ITALIAN)
					return TagDAO.getTags("it");
				else
					return TagDAO.getTags("en");
			} catch (SQLException sqle) {
				throw new InternalDBException(Main.getBundle().getString("ex_internal_db_error"), sqle, "getTags", "MainControllerApp.java");
			}
		}
	
	public static List<String> getCities() throws InternalDBException{
		try {
			return CityDAO.getCities();
		} catch (SQLException sqle){
			throw new InternalDBException(Main.getBundle().getString("ex_internal_db_error"), sqle, "getCities", "MainControllerApp.java");
		}
	}


}
