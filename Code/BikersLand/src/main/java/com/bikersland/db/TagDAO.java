package com.bikersland.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TagDAO {
	public static List<String> getTags() throws SQLException {
		List<String> tagList = new ArrayList<String>();
		
		String query = "SELECT * FROM tag ORDER BY name;";
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
		ResultSet rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			tagList.add(rs.getString("name"));
		}
       
        if (stmt != null)
        	stmt.close();
       
        return tagList;
	}
}
