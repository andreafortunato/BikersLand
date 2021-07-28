package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bikersland.App;
import com.bikersland.bean.EventBean;
import com.bikersland.db.EventDAO;
import com.bikersland.db.EventTagDAO;
import com.bikersland.db.TagDAO;
import com.bikersland.exception.ImageConversionException;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.TagNotFoundException;
import com.bikersland.exception.event.EventNotFoundException;
import com.bikersland.model.Event;

public class NewEventControllerApp {
	
	private NewEventControllerApp() {};
	
	public static EventBean createNewEvent(EventBean eventBean) throws InternalDBException {
		Event event = new Event();
		event.setTitle(eventBean.getTitle());
		event.setDescription(eventBean.getDescription());
		event.setOwner_username(eventBean.getOwner_username());
		event.setDeparture_city(eventBean.getDeparture_city());
		event.setDestination_city(eventBean.getDestination_city());
		event.setDeparture_date(eventBean.getDeparture_date());
		event.setReturn_date(eventBean.getReturn_date());
		event.setImage(eventBean.getImage());
		event.setTags(eventBean.getTags());
		
		try {
			Event createdEvent = EventDAO.createNewEvent(event);
			
			if(!createdEvent.getTags().isEmpty())
				setEventTags(createdEvent);
			
			EventBean createdEventBean = eventBean;
			createdEventBean.setId(createdEvent.getId());
			createdEventBean.setCreate_time(createdEvent.getCreate_time());
			
			return createdEventBean;
						
		} catch (SQLException | ImageConversionException | EventNotFoundException e) {
			Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in createNewEvent() function, inside NewEventControllerApp.java", e);
			
			throw new InternalDBException(App.bundle.getString("ex_internal_db_error"));
		} 
	}
	
	private static void setEventTags(Event createdEvent) throws InternalDBException {
		try {
			List<Integer> eventTagIdList = new ArrayList<>();
			eventTagIdList = TagDAO.tagNameToTagId(createdEvent.getTags());
			
			EventTagDAO.addEventTags(createdEvent.getId(), eventTagIdList);
		} catch (SQLException | TagNotFoundException sqle) {
			Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in setEventTags() function, inside NewEventControllerApp.java", sqle);
			
			throw new InternalDBException(App.bundle.getString("ex_internal_db_error"));
		}
	}
	
}
