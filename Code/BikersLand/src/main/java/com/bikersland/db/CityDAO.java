package com.bikersland.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bikersland.db.queries.SimpleQueries;

public class CityDAO {
	public static List<String> getCities() throws SQLException {
		List<String> tagList = new ArrayList<String>();
		ResultSet rs = null;
		Statement stmt = null;
		
		try {
			stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = SimpleQueries.getCities(stmt);
			
			while(rs.next()) {
				tagList.add(rs.getString("name"));
			}
			
			 return tagList;
		}finally {
			if(rs != null)
				rs.close();
		       
	        if (stmt != null)
	        	stmt.close();
		}
		
	
	}
}
