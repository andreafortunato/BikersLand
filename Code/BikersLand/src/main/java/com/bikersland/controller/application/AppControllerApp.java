package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import com.bikersland.Main;
import com.bikersland.db.CityDAO;
import com.bikersland.db.TagDAO;
import com.bikersland.exception.InternalDBException;

public class AppControllerApp {

	public static List<String> getTags() throws InternalDBException {
			
			try {
				if(Main.locale == Locale.ITALIAN)
					return TagDAO.getTags("it");
				else
					return TagDAO.getTags("en");
			}catch(SQLException sqle) {
				//Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in userJoinedEvent() function, inside EventDetailsControllerApp.java", sqle);
				
				throw new InternalDBException(Main.bundle.getString("ex_internal_db_error"), sqle, "getTags", "HeaderControllerApp.java");
	
			}
			
			
		}
	
	public static List<String> getCities() throws InternalDBException{
		try {
			return CityDAO.getCities();
		}catch(SQLException sqle){
			//Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in userJoinedEvent() function, inside EventCardControllerApp.java", sqle);
			
			throw new InternalDBException(Main.bundle.getString("ex_internal_db_error"), sqle, "getCities", "AppControllerApp.java");

			
		}
	}


}
