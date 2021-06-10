package com.bikersland.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.bikersland.NonSoComeChiamarla;

import javafx.scene.control.Alert.AlertType;

public class DB_Connection {
	private static String USER = "bikersland";
    private static String PASS = "bikersland";
    private static String DB_URL = "jdbc:mysql://localhost:3306/bikersland";
//    private static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    
	public static Connection connection = null;
	
	public static Connection getConnection() {
		if(connection == null) {
//            Class.forName(DRIVER_CLASS_NAME);
			try {
				connection = DriverManager.getConnection(DB_URL, USER, PASS);
			} catch (SQLException e) {
				NonSoComeChiamarla.showTimedAlert(AlertType.ERROR, "DataBase Error", "Connection refused", "A connection with the database could not be established.\n\nPlease try again...", null);
				System.exit(-1);
			}
		}
		
		return connection;		
	}
	
	public static void printSQLException(SQLException ex) {
		
		/* Error Codes table: https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-error-sqlstates.html */

	    for (Throwable e : ex) {
	        if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("\n\nSQLState: " +
                    ((SQLException)e).getSQLState());

                System.err.println("Error Code: " +
                    ((SQLException)e).getErrorCode());

                System.err.println("Message: " + e.getMessage());
                
                System.err.println(e.getLocalizedMessage());
                System.err.println(e.getCause());
                System.err.println(((SQLException) e).getNextException());

                Throwable t = ex.getCause();
                while(t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
	        }
	    }
	}
	
}
