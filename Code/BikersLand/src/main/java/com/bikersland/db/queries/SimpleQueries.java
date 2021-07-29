package com.bikersland.db.queries;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.bikersland.Main;
import com.bikersland.db.DB_Connection;
import com.bikersland.model.Event;

public class SimpleQueries {
	
	private SimpleQueries() {}

	public static ResultSet getUserByUsernameQuery(Statement stmt, String username) throws SQLException {
		String query = "SELECT id, name, surname, username, email, image, create_time FROM user WHERE username='" + username + "';";
		
		return stmt.executeQuery(query);
	}
	
	public static ResultSet askLoginQuery(Statement stmt, String usernameOrEmail, String password) throws SQLException {
		String query = "SELECT id, name, surname, username, email, image, create_time FROM user WHERE (username='" + usernameOrEmail + "' OR email='" + usernameOrEmail + "') AND password=MD5('" + password + "');";
		return stmt.executeQuery(query);
	}
	
	public static ResultSet tagNameToTagIdQuery(Statement stmt, String tagName) throws SQLException {
		String query = "SELECT id FROM tag WHERE en='" + tagName + "' OR it='" + tagName + "';";
		return stmt.executeQuery(query);		
	}
	
	public static ResultSet getParticipantsByEventIdQuery(Statement stmt, Integer eventId) throws SQLException {
		String query = "SELECT username FROM user WHERE id IN (SELECT user_id FROM participation WHERE event_id=" + eventId + ") ORDER BY username;";
		                
		return stmt.executeQuery(query);
	}
	
	public static ResultSet userJoinedEvent(Statement stmt, Integer userId, Integer eventId) throws SQLException {
		String query = "SELECT * FROM participation WHERE user_id=" + userId + " AND event_id=" + eventId + ";";
		
        return stmt.executeQuery(query);
	}
	
	public static ResultSet isFavoriteEvent(Statement stmt, Integer userId, Integer eventId) throws SQLException {
		String query = "SELECT * FROM favorite_event WHERE user_id=" + userId + " AND event_id=" + eventId + ";";
		
		return stmt.executeQuery(query);
	}
	
	public static ResultSet getEventByCities(Statement stmt, String departureCity, String destinationCity) throws SQLException {
		String query = "SELECT * FROM event";
		if(departureCity.equals(Main.bundle.getString("all_female")) && destinationCity.equals(Main.bundle.getString("all_female"))) {
			query += " ORDER BY id DESC;";
		} else {
			if(departureCity.equals(Main.bundle.getString("all_female"))) {
				query += " WHERE destination_city='" + destinationCity + "'";
			} else if(destinationCity.equals(Main.bundle.getString("all_female"))){
				query += " WHERE departure_city='" + departureCity + "'";
			} else {
				query += " WHERE departure_city='" + departureCity + "' AND destination_city='" + destinationCity + "'";
			}
			query += " ORDER BY id DESC;";
		}
		
		return stmt.executeQuery(query);
	}
	
	public static ResultSet getEventByCitiesAndTags(Statement stmt, String departureCity, String destinationCity, List<String> tagList, String language) throws SQLException {
	
		String queryCities = "SELECT * FROM event WHERE 1=1";
		if(departureCity.equals(Main.bundle.getString("all_female")) && destinationCity.equals(Main.bundle.getString("all_female"))) {
			;
		} else if(departureCity.equals(Main.bundle.getString("all_female"))) {
			queryCities += " AND destination_city='" + destinationCity + "'";
		} else if(destinationCity.equals(Main.bundle.getString("all_female"))){
			queryCities += " AND departure_city='" + departureCity + "'";
		} else {
			queryCities += " AND departure_city='" + departureCity + "' AND destination_city='" + destinationCity + "'";
		}
		
		
		String queryTags = " AND id IN (SELECT DISTINCT ET.event_id FROM event_tag ET JOIN tag T ON ET.tag_id=T.id WHERE T." + language + " IN (";
		for(String tag: tagList)
        	queryTags += "'" + tag + "', ";
        queryTags = queryTags.substring(0, queryTags.length()-2) + ") ";
        queryTags += "GROUP BY ET.event_id HAVING COUNT(DISTINCT T." + language + ") = " + tagList.size() + ") ORDER BY id DESC;";
		
		String query = queryCities + queryTags;
	
	
		return stmt.executeQuery(query);
	}
	
	public static ResultSet getJoinedEventsByUser(Statement stmt, Integer userId) throws SQLException {
		String query = "SELECT * FROM event WHERE id IN (SELECT event_id FROM participation WHERE user_id=" + userId + ");";
		
		return stmt.executeQuery(query);
	}
	
	public static ResultSet getFavoriteEventsByUser(Statement stmt, Integer userId) throws SQLException {
		String query = "SELECT * FROM event WHERE id IN (SELECT event_id FROM favorite_event WHERE user_id=" + userId + ");";
		
		return stmt.executeQuery(query);
	}
	
	public static ResultSet getTags(Statement stmt, String language) throws SQLException {
		
		String query = "SELECT " + language + " FROM tag ORDER BY " + language + ";";
		
		return stmt.executeQuery(query);
	}
	
	public static ResultSet getCities(Statement stmt) throws SQLException {
		String query = "SELECT * FROM city ORDER BY name;";
		
		return stmt.executeQuery(query);
	}
}
