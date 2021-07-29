package com.bikersland.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.bikersland.Main;
import com.bikersland.db.queries.CRUDQueries;

import javafx.collections.FXCollections;

public class EventTagDAO {
	public static void addEventTags(Integer eventId, List<Integer> tagList) throws SQLException {
        		
		Statement stmtAddEventTags = null;
		try {
			stmtAddEventTags = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
			CRUDQueries.addEventTagsQuery(stmtAddEventTags, eventId, tagList);
		} finally {
			if (stmtAddEventTags != null)
				stmtAddEventTags.close();
		}
	}
	
	public static List<String> getEventTags(Integer eventId) throws SQLException {
		if(Main.locale == Locale.ITALIAN)
			return getEventTags(eventId, "it");
		else
			return getEventTags(eventId, "en");
	}
	
	private static List<String> getEventTags(Integer eventId, String language) throws SQLException {
		List<String> tagList = new ArrayList<String>();
				
		String query = "SELECT T." + language + " FROM event_tag ET JOIN tag T ON ET.tag_id=T.id WHERE ET.event_id=" + eventId;
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
		ResultSet rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			tagList.add(rs.getString(language));
		}
       
        if (stmt != null)
        	stmt.close();
        
        return tagList;
	}
}
