package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.List;

import com.bikersland.Main;
import com.bikersland.db.ParticipationDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.NoEventParticipantsException;
import com.bikersland.utility.ConstantStrings;

import javafx.scene.image.Image;

public class EventDetailsControllerApp {
	
	private EventDetailsControllerApp() {}
	
	public static List<String> getEventParticipants(Integer eventId) throws InternalDBException, NoEventParticipantsException {
		try {
			return ParticipationDAO.getParticipantsByEventId(eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "getEventParticipants", ConstantStrings.EVENTDETAILSCONTROLLERAPP_JAVA);
		}
	}

	public static Image getDefaultEventImage() {
		return new Image(Main.class.getResourceAsStream("img/background.jpg"));
	}

	public static Boolean userJoinedEvent(Integer userId, Integer eventId) throws InternalDBException {
		try {
			return ParticipationDAO.isJoinedEvent(userId, eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "userJoinedEvent", ConstantStrings.EVENTDETAILSCONTROLLERAPP_JAVA);
		} 
	}
	
	public static void addUserParticipation(Integer userId, Integer eventId) throws InternalDBException {
		try {
			ParticipationDAO.addUserParticipation(userId, eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "addUserParticipation", ConstantStrings.EVENTDETAILSCONTROLLERAPP_JAVA);
		}
	}
	
	public static void removeUserParticipation(Integer userId, Integer eventId) throws InternalDBException {
		try {
			ParticipationDAO.removeUserParticipation(userId, eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "removeUserParticipation", ConstantStrings.EVENTDETAILSCONTROLLERAPP_JAVA);
		}
	}
}
