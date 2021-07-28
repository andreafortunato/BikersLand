package com.bikersland.db.queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.bikersland.db.DB_Connection;

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
}
