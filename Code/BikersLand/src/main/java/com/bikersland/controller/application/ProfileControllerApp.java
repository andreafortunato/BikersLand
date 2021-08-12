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
import com.bikersland.utility.ConstantStrings;

public class ProfileControllerApp {
	
	private ProfileControllerApp() {}

	public static List<EventBean> getJoinedEventsByUser(Integer userId) throws InternalDBException {
		List<Event> eventList = new ArrayList<>();
		try {
			eventList = ParticipationDAO.getJoinedEventsByUser(userId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "getJoinedEventsByUser", "ProfileControllerApp.java");
		}
		
		List<EventBean> eventBeanList = new ArrayList<>();
		
		for(Event event:eventList) {
			
			eventBeanList.add(new EventBean(event.getId(), event.getTitle(), event.getDescription(), event.getOwnerUsername(),
        			event.getDepartureCity(), event.getDestinationCity(), event.getDepartureDate(),
        			event.getReturnDate(), event.getImage(), event.getCreateTime(), event.getTags()));
		}
		
		return eventBeanList;
		
	}

	public static List<EventBean> getFavoriteEventsByUser(Integer userId) throws InternalDBException {
		List<Event> eventList = new ArrayList<>();
		try {
			eventList = FavoriteEventDAO.getFavoriteEventsByUser(userId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "getFavoriteEventsByUser", "ProfileControllerApp.java");
		}
		
		List<EventBean> eventBeanList = new ArrayList<>();
		
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
