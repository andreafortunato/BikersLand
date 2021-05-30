package com.bikersland.db;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;

import com.bikersland.Event;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class EventDAO {
	public static void setEvent(Event event) throws SQLException, IOException {
		
		
		String query = "INSERT INTO event(title, description, owner_username, departure_city, " +
				"destination_city, departure_date, return_date, image) VALUES(?,?,?,?,?,?,?,?);";
		
		PreparedStatement pstNewEvent = DB_Connection.getConnection().prepareStatement(query);
				
		pstNewEvent.setString(1,event.getTitle());
		pstNewEvent.setString(2,event.getDescription());
		pstNewEvent.setString(3,event.getOwner_username());
		pstNewEvent.setString(4,event.getDeparture_city());
		pstNewEvent.setString(5,event.getDestination_city());
		pstNewEvent.setDate(6,event.getDeparture_date());
		pstNewEvent.setDate(7,event.getReturn_date());
		
		
		BufferedImage img = SwingFXUtils.fromFXImage(event.getImage(),null);
		ByteArrayOutputStream bts = new ByteArrayOutputStream();
		ImageIO.write(img,"png" , bts);
		InputStream is = new ByteArrayInputStream(bts.toByteArray());
		
		pstNewEvent.setBlob(8,is);
		
		
		pstNewEvent.executeUpdate();  
		
		//DB_Connection.getConnection().commit();
		
		
		
	}
	
	public static Event getEventByID(Integer id) throws SQLException, IOException {
		Event event;
		String query = "SELECT * FROM event WHERE id='" + id + "';";
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
        ResultSet rs = stmt.executeQuery(query);
        
        if(rs.first()) {
        	
        	BufferedImage img = ImageIO.read(rs.getBinaryStream("image"));
        	Image image = SwingFXUtils.toFXImage(img, null);
        	event = new Event(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("owner_username"),
        			rs.getString("departure_city"), rs.getString("destination_city"), rs.getDate("departure_date"),
        			rs.getDate("return_date"),image);
        } else {
        	event = null;
        }
        
        rs.close();
        
        if (stmt != null)
        	stmt.close();
        
		return event;
	}
}
