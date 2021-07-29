package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bikersland.Main;
import com.bikersland.db.FavoriteEventDAO;
import com.bikersland.db.ParticipationDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.NoEventParticipantsException;

import javafx.scene.image.Image;

public class EventCardControllerApp {

	public static List<String> getEventParticipants(Integer eventId) throws InternalDBException, NoEventParticipantsException {
		try {
			return ParticipationDAO.getParticipantsByEventId(eventId);
		} catch (SQLException sqle) {
			Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in getEventParticipants() function, inside EventDetailsControllerApp.java", sqle);
			
			throw new InternalDBException(Main.bundle.getString("ex_internal_db_error"));
		}
	}
	
	public static Image getDefaultEventImage() {
		return new Image(Main.class.getResourceAsStream("img/background.jpg"));
	}
	
	public static Boolean isFavoriteEvent(Integer userId, Integer eventId) throws InternalDBException {
		
		try {
			return FavoriteEventDAO.isFavoriteEvent(userId, eventId);
		} catch (SQLException sqle) {
			Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in userJoinedEvent() function, inside EventCardControllerApp.java", sqle);
			
			throw new InternalDBException(Main.bundle.getString("ex_internal_db_error"), sqle, "IsFavoriteEvent", "EventCardControllerApp.java");

		}
	}

	public static Boolean isJoinedEvent(Integer loggedUserId, Integer eventId) throws InternalDBException {
		try {
			return ParticipationDAO.isJoinedEvent(loggedUserId, eventId);
		} catch (SQLException sqle) {
			Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in userJoinedEvent() function, inside EventCardControllerApp.java", sqle);
			
			throw new InternalDBException(Main.bundle.getString("ex_internal_db_error"), sqle, "IsJoinedEvent", "EventCardControllerApp.java");

		}
	}

	public static void removeFavoriteEvent(Integer loggedUserId, Integer eventId) throws InternalDBException {
		
		try {
			FavoriteEventDAO.removeFavoriteEvent(loggedUserId, eventId);
		} catch (SQLException sqle) {
			Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in userJoinedEvent() function, inside EventCardControllerApp.java", sqle);
			
			throw new InternalDBException(Main.bundle.getString("ex_internal_db_error"), sqle, "removeFavoriteEvent", "EventCardControllerApp.java");

		}
		
	}

	public static void addFavoriteEvent(Integer loggedUserId, Integer eventId) throws InternalDBException {
		try {
			FavoriteEventDAO.addFavoriteEvent(loggedUserId, eventId);
		} catch (SQLException sqle) {
			Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in userJoinedEvent() function, inside EventCardControllerApp.java", sqle);
			
			throw new InternalDBException(Main.bundle.getString("ex_internal_db_error"), sqle, "addFavoriteEvent", "EventCardControllerApp.java");

		}
		
	}

	public static void addUserParticipation(Integer userId, Integer eventId) throws InternalDBException {
		try {
			ParticipationDAO.addUserParticipation(userId, eventId);
		} catch (SQLException sqle) {
//			Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in userJoinedEvent() function, inside EventDetailsControllerApp.java", sqle);
			
			throw new InternalDBException(Main.bundle.getString("ex_internal_db_error"), sqle, "addUserParticipation", "EventCardControllerApp.java");
		}
	}
	
	public static void removeUserParticipation(Integer userId, Integer eventId) throws InternalDBException {
		try {
			ParticipationDAO.removeUserParticipation(userId, eventId);
		} catch (SQLException sqle) {
//			Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in userJoinedEvent() function, inside EventDetailsControllerApp.java", sqle);
			
			throw new InternalDBException(Main.bundle.getString("ex_internal_db_error"), sqle, "removeUserParticipation", "EventCardControllerApp.java");
		}
	}
}
