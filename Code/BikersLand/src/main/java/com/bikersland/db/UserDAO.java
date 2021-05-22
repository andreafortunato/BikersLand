package com.bikersland.db;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import com.bikersland.App;
import com.bikersland.NonSoComeChiamarla;
import com.bikersland.User;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

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
	
	public static User askLogin (String userem, String password) throws SQLException, UsernameException, EmailException{
		
		
		String query = "SELECT id, name, surname, username, email, create_time FROM user WHERE (username='" + userem + "' OR email='" + userem + "') AND password=MD5('" + password + "');";
		
		
		ResultSet rs;
		User user1;
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
          
		 rs = stmt.executeQuery(query);
		
		
		 if(rs.first()) {
	        	user1 = new User(rs.getInt("id"), rs.getString("name"), rs.getString("surname"), rs.getString("username"),
	        			rs.getString("email"),null, rs.getDate("create_time"));
	        } else {
	        	user1 = null;
	        	
	        	// TODO: Generare eccezione "Utente non trovato"
	        }
		 
		 //ora ho l'utente ed in base a quello apro la sua area personale
	        
	        rs.close();
		
       
        if (stmt != null)
        	stmt.close();
        
        return user1;
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
        	user = new User();
        	
        	// TODO: Generare eccezione tipo "Utente non trovato"
        }
        
        rs.close();
        
        if (stmt != null)
        	stmt.close();
        
		return user;
	}
}
