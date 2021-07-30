package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.List;

import com.bikersland.Main;
import com.bikersland.db.ParticipationDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.NoEventParticipantsException;

import javafx.scene.image.Image;

public class EventDetailsControllerApp {
	
	public static List<String> getEventParticipants(Integer eventId) throws InternalDBException, NoEventParticipantsException {
		try {
			return ParticipationDAO.getParticipantsByEventId(eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString("ex_internal_db_error"), sqle, "getEventParticipants", "EventDetailsControllerApp.java");
		}
	}

	public static Image getDefaultEventImage() {
		return new Image(Main.class.getResourceAsStream("img/background.jpg"));
	}

	public static Boolean userJoinedEvent(Integer userId, Integer eventId) throws InternalDBException {
		try {
			return ParticipationDAO.isJoinedEvent(userId, eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString("ex_internal_db_error"), sqle, "userJoinedEvent", "EventDetailsControllerApp.java");
		} 
	}
	
	public static void addUserParticipation(Integer userId, Integer eventId) throws InternalDBException {
		try {
			ParticipationDAO.addUserParticipation(userId, eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString("ex_internal_db_error"), sqle, "addUserParticipation", "EventDetailsControllerApp.java");
		}
	}
	
	public static void removeUserParticipation(Integer userId, Integer eventId) throws InternalDBException {
		try {
			ParticipationDAO.removeUserParticipation(userId, eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString("ex_internal_db_error"), sqle, "removeUserParticipation", "EventDetailsControllerApp.java");
		}
	}
}
