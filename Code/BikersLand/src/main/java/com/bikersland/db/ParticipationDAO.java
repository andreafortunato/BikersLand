package com.bikersland.db;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.bikersland.db.queries.CRUDQueries;
import com.bikersland.db.queries.SimpleQueries;
import com.bikersland.exception.NoEventParticipantsException;
import com.bikersland.model.Event;
import com.bikersland.model.User;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ParticipationDAO {
	public static Boolean isJoinedEvent(Integer userId, Integer eventId) throws SQLException {
		Boolean isJoined = false;
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = SimpleQueries.userJoinedEvent(stmt, userId, eventId);
			
			if(rs.next()) {
				isJoined = true;
			}
			
			return isJoined;
		} finally {
			if (rs != null) 
				rs.close();
	       
	        if (stmt != null)
	        	stmt.close();
		}
	}
	
	public static void addUserParticipation(Integer userId, Integer eventId) throws SQLException {
		Statement stmt = null;
		
		try {
			stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			CRUDQueries.addUserParticipationQuery(stmt, userId, eventId);
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}
	
	public static void removeUserParticipation(Integer userId, Integer eventId) throws SQLException {		
		Statement stmt = null;
		try {
			stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			CRUDQueries.removeUserParticipationQuery(stmt, userId, eventId);
		} finally {
			if (stmt != null)
	        	stmt.close();
		}
	}
	
	public static List<Event> getJoinedEventsByUser(User user) throws SQLException, IOException {
		List<Event> eventList = new ArrayList<Event>();
		
		String query = "SELECT * FROM event WHERE id IN (SELECT event_id FROM participation WHERE user_id=" + user.getId() + ");";
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
		ResultSet rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			Image image;
			if(rs.getBinaryStream("image") != null) {
	        	BufferedImage img = ImageIO.read(rs.getBinaryStream("image"));
	        	image = SwingFXUtils.toFXImage(img, null);
			} else {
				image = null;
			}
			eventList.add(new Event(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("owner_username"),
        			rs.getString("departure_city"), rs.getString("destination_city"), rs.getDate("departure_date"),
        			rs.getDate("return_date"), image, rs.getDate("create_time"), EventTagDAO.getEventTags(rs.getInt("id"))));
		}
       
        if (stmt != null)
        	stmt.close();
        
		return eventList;
	}
	
	public static List<String> getParticipantsByEventId(Integer eventId) throws SQLException, NoEventParticipantsException {
		List<String> userList = new ArrayList<String>();
				
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = SimpleQueries.getParticipantsByEventIdQuery(stmt, eventId);
			
			if(!rs.isFirst())
				throw new NoEventParticipantsException();
			
			while(rs.next()) {
				userList.add(rs.getString(UserDAO.USERNAME_COL));
			}
			
			return userList;
		} finally {
			if (rs != null)
				rs.close();
			
			if (stmt != null)
	        	stmt.close();
		}
	}
}
