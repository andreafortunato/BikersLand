package com.bikersland.db;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.bikersland.Event;
import com.bikersland.User;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class FavoriteEventDAO {
	public static Boolean isFavoriteEvent(User user, Event event) throws SQLException, IOException {
		Boolean isFavorite = false;
		
		String query = "SELECT * FROM favorite_event WHERE user_id=" + user.getId() + " AND event_id=" + event.getId() + ";";
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
		ResultSet rs = stmt.executeQuery(query);
		
		if(rs.next()) {
			isFavorite = true;
		}
       
        if (stmt != null)
        	stmt.close();
        
		return isFavorite;
	}
	
	public static void addFavoriteEvent(User user, Event event) throws SQLException, IOException {
		String query = "INSERT INTO favorite_event VALUES(" + user.getId() + ", " + event.getId() + ");";
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
		stmt.executeUpdate(query);
       
        if (stmt != null)
        	stmt.close();
	}
	
	public static void removeFavoriteEvent(User user, Event event) throws SQLException, IOException {
		String query = "DELETE FROM favorite_event WHERE user_id=" + user.getId() + " AND event_id=" + event.getId() + ";";
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
		stmt.executeUpdate(query);
       
        if (stmt != null)
        	stmt.close();
	}
	
	public static List<Event> getFavoriteEventsByUser(User user) throws SQLException, IOException {
		List<Event> eventList = new ArrayList<Event>();
		
		String query = "SELECT * FROM event WHERE id IN (SELECT event_id FROM favorite_event WHERE user_id=" + user.getId() + ");";
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
		ResultSet rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			BufferedImage img = ImageIO.read(rs.getBinaryStream("image"));
        	Image image = SwingFXUtils.toFXImage(img, null);
			eventList.add(new Event(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("owner_username"),
        			rs.getString("departure_city"), rs.getString("destination_city"), rs.getDate("departure_date"),
        			rs.getDate("return_date"), image, rs.getDate("create_time"), EventTagDAO.getEventTags(rs.getInt("id"))));
		}
       
        if (stmt != null)
        	stmt.close();
        
		return eventList;
	}
}
