package com.bikersland.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bikersland.utility.TimedAlert;
import com.bikersland.Main;

import javafx.scene.control.Alert.AlertType;

public class DBConnection {
	
	private DBConnection() {}
	
	private static final String USER = "bikersland";
    private static final String PASS = "bikersland";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bikersland";
    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    
	private static Connection connection;
	
	public static Connection getConnection() throws SQLException {
		if(connection == null) {
			try {
				Class.forName(DRIVER_CLASS_NAME);
				connection = DriverManager.getConnection(DB_URL, USER, PASS);
			} catch (SQLException | ClassNotFoundException e) {
				Logger.getGlobal().log(Level.SEVERE, "Catched SQLException/ClassNotFoundException in getConnection() method, inside DB_Connection.java", e);
				
				throw new SQLException(e);
			}
		}
		
		if(connection.isClosed()) {
			try {
				connection = DriverManager.getConnection(DB_URL, USER, PASS);
			} catch (SQLException sqle) {
				Logger.getGlobal().log(Level.SEVERE, "Catched SQLException/ClassNotFoundException in getConnection() method, inside DB_Connection.java", sqle);
				
				throw new SQLException(sqle);
			}
		}
		
		return connection;		
	}
}
