package com.bikersland.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bikersland.Event;

public class EventDAO {
	public static void setEvent(Event event) throws SQLException {
		String query = "INSERT INTO event(title, description, owner_username, departure_city, " +
				"destination_city, departure_date, return_date) VALUES('" +
				event.getTitle() + "', '" + event.getDescription() + "', '" + 
				event.getOwner_username() + "', '" + event.getDeparture_city() + "', '" +
				event.getDestination_city() + "', '" + event.getDeparture_date() + "', '" +
				event.getReturn_date() + "');";
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
		stmt.executeUpdate(query);
       
        if (stmt != null)
        	stmt.close();
	}
	
	public static Event getEventByID(Integer id) throws SQLException {
		Event event;
		String query = "SELECT * FROM event WHERE id='" + id + "';";
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
        ResultSet rs = stmt.executeQuery(query);
        
        if(rs.first()) {
        	event = new Event(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("owner_username"),
        			rs.getString("departure_city"), rs.getString("destination_city"), rs.getDate("departure_date"),
        			rs.getDate("return_date"));
        } else {
        	event = null;
        }
        
        rs.close();
        
        if (stmt != null)
        	stmt.close();
        
		return event;
	}
}
