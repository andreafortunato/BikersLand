package com.bikersland.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventTagDAO {
	public static void addEventTags(Integer eventId, List<String> tagList) throws SQLException {
        String query = "INSERT INTO event_tag VALUES ";
        for(String tag: tagList)
        	query += "(" + eventId + ", '" + tag + "'), ";
        query = query.substring(0, query.length()-2) + ";";
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
		stmt.executeUpdate(query);
       
        if (stmt != null)
        	stmt.close();
	}
	
	public static List<String> getEventTags(Integer eventId) throws SQLException {
		List<String> tagList = new ArrayList<String>();
		
		String query = "SELECT tag_name FROM event_tag WHERE event_id=" + eventId;		
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
		ResultSet rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			tagList.add(rs.getString("tag_name"));
		}
       
        if (stmt != null)
        	stmt.close();
       
        return tagList;
	}
}
