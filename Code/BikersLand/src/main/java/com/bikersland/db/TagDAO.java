package com.bikersland.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bikersland.db.queries.SimpleQueries;
import com.bikersland.exception.TagNotFoundException;

public class TagDAO {
	/*
	public static List<String> getTags() throws SQLException {
		if(App.locale == Locale.ITALIAN)
			return getTags("it");
		else
			return getTags("en");
	}*/
	
	public static List<String> getTags(String language) throws SQLException {
		List<String> tagList = new ArrayList<String>();
		ResultSet rs = null;
		Statement stmt = null;
		
		try {
			stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = SimpleQueries.getTags(stmt, language);
			while(rs.next()) {
				tagList.add(rs.getString(language));
			}
			
			return tagList;
		}finally {
			if (stmt != null)
	        	stmt.close();
	       if(rs != null)
	    	   rs.close();
		} 
        
	}
	
	public static List<Integer> tagNameToTagId(List<String> tagList) throws SQLException, TagNotFoundException {
		List<Integer> tagIdList = new ArrayList<>();
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			for(String tagName:tagList) {
				rs = SimpleQueries.tagNameToTagIdQuery(stmt, tagName);
				
				if(rs.next()) {
					tagIdList.add(rs.getInt("id"));
				} else {
					throw new TagNotFoundException();
				}
			}
			
			return tagIdList;
		} finally {
			if (rs != null)
				rs.close();
			
			if (stmt != null)
				stmt.close();
		}
	}
}
