package com.bikersland.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bikersland.User;

public class UserDAO {
	
	public static void setUser(User user) throws SQLException, UsernameException, EmailException {
		String query = "INSERT INTO user(name, surname, username, email, password) VALUES('" +
				user.getName() + "', '" + user.getSurname() + "', '" + user.getUsername() + "', '" +
				user.getEmail() + "', MD5('" + user.getPassword() + "'));";
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
		try {
			stmt.executeUpdate(query);
		} catch (SQLException ex) {
			int errorCode = ex.getErrorCode();
			if(errorCode == 1062) {
				/* Username o Email già esistenti */
				if(ex.getMessage().contains("username_UNIQUE")) {
					/* Username già presente */
					throw new UsernameException();
				} else {
					/* Email già presente */
					throw new EmailException();
				}
			}
		}
		
       
        if (stmt != null)
        	stmt.close();
	}
	
	public static User askLogin (String username_email, String password) throws SQLException {
		String query = "SELECT id, name, surname, username, email, create_time FROM user WHERE (username='" + username_email + "' OR email='" + username_email + "') AND password=MD5('" + password + "');";
		
		ResultSet rs;
		User user;
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		rs = stmt.executeQuery(query);
		
		if(rs.first()) {
			user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("surname"), rs.getString("username"),
			rs.getString("email"), null, rs.getDate("create_time"));
		} else {
			user = null;
		}
		
		rs.close();
		
		if (stmt != null)
			stmt.close();
		
		return user;
	}
	
	public static User getUserByUsername(String username) throws SQLException {
		User user;
		String query = "SELECT * FROM user WHERE username='" + username + "';";
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
        ResultSet rs = stmt.executeQuery(query);
        
        if(rs.first()) {
        	user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("surname"), rs.getString("username"),
        			rs.getString("email"), rs.getString("password"), rs.getDate("create_time"));
        } else {
        	user = null;
        }
        
        rs.close();
        
        if (stmt != null)
        	stmt.close();
        
		return user;
	}
}
