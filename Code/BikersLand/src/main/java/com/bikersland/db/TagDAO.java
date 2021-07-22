package com.bikersland.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.bikersland.App;

public class TagDAO {
	public static List<String> getTags() throws SQLException {
		if(App.locale == Locale.ITALIAN)
			return getTags("it");
		else
			return getTags("en");
	}
	
	private static List<String> getTags(String language) throws SQLException {
		List<String> tagList = new ArrayList<String>();
		
		String query = "SELECT " + language + " FROM tag ORDER BY " + language + ";";
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
		ResultSet rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			tagList.add(rs.getString(language));
		}
       
        if (stmt != null)
        	stmt.close();
       
        return tagList;
	}
	
	public static List<Integer> tagNameToTagId(List<String> tagList) throws SQLException {
		List<Integer> tagIdList= new ArrayList<>();
		
		String query;
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs;
		
		for(String tagName:tagList) {
			query = "SELECT id FROM tag WHERE en='" + tagName + "' OR it='" + tagName + "';";
			rs = stmt.executeQuery(query);
			
			if(rs.next()) {
				tagIdList.add(rs.getInt("id"));
			}
		}
       
        if (stmt != null)
        	stmt.close();
        
		return tagIdList;
	}
}
