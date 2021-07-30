package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bikersland.Main;
import com.bikersland.bean.EventBean;
import com.bikersland.bean.UserBean;
import com.bikersland.db.FavoriteEventDAO;
import com.bikersland.db.ParticipationDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.model.Event;
import com.bikersland.model.User;
import com.bikersland.singleton.LoginSingleton;

public class ProfileControllerApp {

	public static List<EventBean> getJoinedEventsByUser(Integer userId) throws InternalDBException {
		List<Event> eventList = new ArrayList<Event>();
		try {
			eventList = ParticipationDAO.getJoinedEventsByUser(userId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString("ex_internal_db_error"), sqle, "getJoinedEventsByUser", "ProfileControllerApp.java");
		}
		
		List<EventBean> eventBeanList = new ArrayList<EventBean>();
		
		for(Event event:eventList) {
			
			eventBeanList.add(new EventBean(event.getId(), event.getTitle(), event.getDescription(), event.getOwnerUsername(),
        			event.getDepartureCity(), event.getDestinationCity(), event.getDepartureDate(),
        			event.getReturnDate(), event.getImage(), event.getCreateTime(), event.getTags()));
		}
		
		return eventBeanList;
		
	}

	public static List<EventBean> getFavoriteEventsByUser(Integer userId) throws InternalDBException {
		List<Event> eventList = new ArrayList<Event>();
		try {
			eventList = FavoriteEventDAO.getFavoriteEventsByUser(userId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString("ex_internal_db_error"), sqle, "getFavoriteEventsByUser", "ProfileControllerApp.java");
		}
		
		List<EventBean> eventBeanList = new ArrayList<EventBean>();
		
		for(Event event:eventList) {
			
			eventBeanList.add(new EventBean(event.getId(), event.getTitle(), event.getDescription(), event.getOwnerUsername(),
        			event.getDepartureCity(), event.getDestinationCity(), event.getDepartureDate(),
        			event.getReturnDate(), event.getImage(), event.getCreateTime(), event.getTags()));
		}
		
		return eventBeanList;
	}

	public static UserBean getLoggedUser() {
		User user = LoginSingleton.getLoginInstance().getUser();
		
		return new UserBean(user.getId(), user.getName(), user.getSurname(), user.getUsername(),
				user.getEmail(), null, user.getImage(), user.getCreateTime());
	}

}
