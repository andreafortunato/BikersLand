package com.bikersland.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DB_Connection {
	private static String USER = "bikersland";
    private static String PASS = "bikersland";
    private static String DB_URL = "jdbc:mysql://localhost:3306/bikersland";
//    private static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    
	public static Connection connection = null;
	
	public static Connection getConnection() throws SQLException {
		if(connection == null) {
//            Class.forName(DRIVER_CLASS_NAME);
			connection = DriverManager.getConnection(DB_URL, USER, PASS);			
		}
		
//		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
//                ResultSet.CONCUR_READ_ONLY);
//        
//		String sql = "SELECT * FROM user;";
//        
//        ResultSet rs = stmt.executeQuery(sql);
//        
//        while(rs.next()) {
//        	Integer id = rs.getInt("id");
//        	String nome = rs.getString("name");
//            String cognome = rs.getString("surname");
//            String username = rs.getString("username");
//            String email = rs.getString("email");
//            String password = rs.getString("password");
//            Date create_time = rs.getDate("create_time");
//            
//            SimpleDateFormat sdf = new SimpleDateFormat(
//            	    "dd-MM-yyyy");
//                        
//            System.out.println("ID: " + id + "\nNome: " + nome + 
//            		"\nCognome: " + cognome + "\nUsername: " + username +
//            		"\nEmail: " + email + "\nPassword: " + password +
//            		"\nCreate_time: " + sdf.format(create_time));
//            System.out.println();
//        }
//        
//        rs.close();
//        
//        if (stmt != null)
//        	stmt.close();
//        
//        if (connection != null)
//        	connection.close();
		
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
