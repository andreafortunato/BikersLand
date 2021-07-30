package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.bikersland.Main;
import com.bikersland.bean.EventBean;
import com.bikersland.db.EventDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.model.Event;

public class HomepageControllerApp {
	

	public static List<EventBean> getEventByCities(String departureCity, String destinationCity) throws InternalDBException {
		List<Event> eventList = new ArrayList<Event>();
		try {
			eventList = EventDAO.getEventByCities(departureCity, destinationCity);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString("ex_internal_db_error"), sqle, "getEventByCities", "HomepageControllerApp.java");
		}
		
		List<EventBean> eventBeanList = new ArrayList<EventBean>();
		
		for(Event event:eventList) {
			
			eventBeanList.add(new EventBean(event.getId(), event.getTitle(), event.getDescription(), event.getOwnerUsername(),
        			event.getDepartureCity(), event.getDestinationCity(), event.getDepartureDate(),
        			event.getReturnDate(), event.getImage(), event.getCreateTime(), event.getTags()));
		}
		
		return eventBeanList;
	}

	public static List<EventBean> getEventByCitiesAndTags(String departureCity, String destinationCity,List<String> tagList) throws InternalDBException {
		List<Event> eventList = new ArrayList<Event>();
		
		try {
			if(Main.getLocale() == Locale.ITALIAN) {
				eventList = EventDAO.getEventByCitiesAndTags(departureCity, destinationCity, tagList,"it");
			}
			else {
				eventList = EventDAO.getEventByCitiesAndTags(departureCity, destinationCity, tagList,"en");
			}
		}catch(SQLException sqle){
			throw new InternalDBException(Main.getBundle().getString("ex_internal_db_error"), sqle, "getEventByCitiesAndTags", "HomepageControllerApp.java");
		}
		
		List<EventBean> eventBeanList = new ArrayList<EventBean>();
		
		for(Event event:eventList) {
			
			eventBeanList.add(new EventBean(event.getId(), event.getTitle(), event.getDescription(), event.getOwnerUsername(),
        			event.getDepartureCity(), event.getDestinationCity(), event.getDepartureDate(),
        			event.getReturnDate(), event.getImage(), event.getCreateTime(), event.getTags()));
		}
		
		return eventBeanList;
	}

}
