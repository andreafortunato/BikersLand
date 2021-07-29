package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import com.bikersland.Main;
import com.bikersland.bean.EventBean;
import com.bikersland.model.Event;


import com.bikersland.db.EventDAO;

import com.bikersland.exception.InternalDBException;

public class HomepageControllerApp {
	

	public static List<EventBean> getEventByCities(String departureCity, String destinationCity) throws InternalDBException {
		
		List<Event> eventList = new ArrayList<Event>();
		try {
			eventList = EventDAO.getEventByCities(departureCity, destinationCity);
		} catch (SQLException sqle) {
			//Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in userJoinedEvent() function, inside EventCardControllerApp.java", sqle);
			
			throw new InternalDBException(Main.bundle.getString("ex_internal_db_error"), sqle, "getEventByCities", "HomepageControllerApp.java");

		}
		
		List<EventBean> eventBeanList = new ArrayList<EventBean>();
		
		for(Event event:eventList) {
			
			eventBeanList.add(new EventBean(event.getId(), event.getTitle(), event.getDescription(), event.getOwner_username(),
        			event.getDeparture_city(), event.getDestination_city(), event.getDeparture_date(),
        			event.getReturn_date(), event.getImage(), event.getCreate_time(), event.getTags()));
		}
		
		return eventBeanList;
	}

	public static List<EventBean> getEventByCitiesAndTags(String departureCity, String destinationCity,List<String> tagList) throws InternalDBException {
		
		List<Event> eventList = new ArrayList<Event>();
		
		try {
			if(Main.locale == Locale.ITALIAN) {
				eventList = EventDAO.getEventByCitiesAndTags(departureCity, destinationCity, tagList,"it");
			}
				
			else {
				eventList = EventDAO.getEventByCitiesAndTags(departureCity, destinationCity, tagList,"en");
			}
		}catch(SQLException sqle){
			//Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in userJoinedEvent() function, inside EventCardControllerApp.java", sqle);
			
			throw new InternalDBException(Main.bundle.getString("ex_internal_db_error"), sqle, "getEventByCitiesAndTags", "HomepageControllerApp.java");

		}
		
		
		List<EventBean> eventBeanList = new ArrayList<EventBean>();
		
		for(Event event:eventList) {
			
			eventBeanList.add(new EventBean(event.getId(), event.getTitle(), event.getDescription(), event.getOwner_username(),
        			event.getDeparture_city(), event.getDestination_city(), event.getDeparture_date(),
        			event.getReturn_date(), event.getImage(), event.getCreate_time(), event.getTags()));
		}
		
		return eventBeanList;
		
	}

}
