package com.bikersland.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bikersland.Main;
import com.bikersland.utility.TimedAlert;

import javafx.scene.control.Alert.AlertType;

public class DB_Connection {
	private static String USER = "bikersland";
    private static String PASS = "bikersland";
    private static String DB_URL = "jdbc:mysql://localhost:3306/bikersland";
//    private static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    
	public static Connection connection = null;
	
	public static Connection getConnection() throws SQLException {
		if(connection == null) {
			try {
				Class.forName(DRIVER_CLASS_NAME);
				connection = DriverManager.getConnection(DB_URL, USER, PASS);
			} catch (SQLException | ClassNotFoundException e) {
				TimedAlert.show(AlertType.ERROR,
						Main.getBundle().getString("timedalert_db_conn_error_title"),
						Main.getBundle().getString("timedalert_db_conn_error_header"),
						Main.getBundle().getString("timedalert_db_conn_error_content"), null);
				
				Logger.getGlobal().log(Level.SEVERE, "Catched SQLException/ClassNotFoundException in getConnection() method, inside DB_Connection.java", e);

				System.exit(-1);
			}
		}
		
		try {
			if(connection.isClosed()) {
				connection = DriverManager.getConnection(DB_URL, USER, PASS);
			}
		} catch (SQLException e) {
			throw e;
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
