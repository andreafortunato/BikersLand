package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bikersland.Main;
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
		event.setOwnerUsername(eventBean.getOwnerUsername());
		event.setDepartureCity(eventBean.getDepartureCity());
		event.setDestinationCity(eventBean.getDestinationCity());
		event.setDepartureDate(eventBean.getDepartureDate());
		event.setReturnDate(eventBean.getReturnDate());
		event.setImage(eventBean.getImage());
		event.setTags(eventBean.getTags());
		
		try {
			Event createdEvent = EventDAO.createNewEvent(event);
			
			if(!createdEvent.getTags().isEmpty())
				setEventTags(createdEvent);
			
			EventBean createdEventBean = eventBean;
			createdEventBean.setId(createdEvent.getId());
			createdEventBean.setCreateTime(createdEvent.getCreateTime());
			
			return createdEventBean;
						
		} catch (SQLException | ImageConversionException | EventNotFoundException e) {
			throw new InternalDBException(Main.getBundle().getString("ex_internal_db_error"), e, "createNewEvent", "NewEventControllerApp.java");
		} 
	}
	
	private static void setEventTags(Event createdEvent) throws InternalDBException {
		try {
			List<Integer> eventTagIdList = new ArrayList<>();
			eventTagIdList = TagDAO.tagNameToTagId(createdEvent.getTags());
			
			EventTagDAO.addEventTags(createdEvent.getId(), eventTagIdList);
		} catch (SQLException | TagNotFoundException e) {
			throw new InternalDBException(Main.getBundle().getString("ex_internal_db_error"), e, "setEventTags", "NewEventControllerApp.java");
		}
	}
	
}
