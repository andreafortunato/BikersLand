package com.bikersland.db;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.bikersland.Event;
import com.bikersland.User;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class EventDAO {
	public static Event setEvent(Event event) throws SQLException, IOException {
		CallableStatement stmCreateEvent = DB_Connection.getConnection().prepareCall("{CALL CreateEvent(?,?,?,?,?,?,?,?)}");
				
		stmCreateEvent.setString(1, event.getTitle());
		stmCreateEvent.setString(2, event.getDescription());
		stmCreateEvent.setString(3, event.getOwner_username());
		stmCreateEvent.setString(4, event.getDeparture_city());
		stmCreateEvent.setString(5, event.getDestination_city());
		stmCreateEvent.setDate(6, event.getDeparture_date());
		stmCreateEvent.setDate(7, event.getReturn_date());
		
		if(event.getImage() != null) {
			BufferedImage buffImg = SwingFXUtils.fromFXImage(event.getImage(),null);
			ByteArrayOutputStream bts = new ByteArrayOutputStream();
			ImageIO.write(buffImg,"png" , bts);
			InputStream blobImage = new ByteArrayInputStream(bts.toByteArray());
			
			stmCreateEvent.setBlob(8, blobImage);
		} else {
			stmCreateEvent.setNull(8, Types.BLOB);
		}
		
		ResultSet newEvent = null;
		try {
			newEvent = stmCreateEvent.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		
		if(newEvent.next()) {
			Image image;
			if(newEvent.getBinaryStream("image") != null) {
			BufferedImage img = ImageIO.read(newEvent.getBinaryStream("image"));
        	image = SwingFXUtils.toFXImage(img, null);
			} else {
				image = null;
			}
			
			return new Event(newEvent.getInt("id"), newEvent.getString("title"), newEvent.getString("description"),
					newEvent.getString("owner_username"), newEvent.getString("departure_city"),
					newEvent.getString("destination_city"), newEvent.getDate("departure_date"),
					newEvent.getDate("return_date"), image, newEvent.getDate("create_time"), event.getTags());
		}
		
		return null;
	}
	
	public static Event getEventByID(Integer id) throws SQLException, IOException {
		Event event;
		String query = "SELECT * FROM event WHERE id='" + id + "';";
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
        ResultSet rs = stmt.executeQuery(query);
        
        if(rs.first()) {
        	Image image;
			if(rs.getBinaryStream("image") != null) {
	        	BufferedImage img = ImageIO.read(rs.getBinaryStream("image"));
	        	image = SwingFXUtils.toFXImage(img, null);
			} else {
				image = null;
			}
        	event = new Event(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("owner_username"),
        			rs.getString("departure_city"), rs.getString("destination_city"), rs.getDate("departure_date"),
        			rs.getDate("return_date"), image, rs.getDate("create_time"), EventTagDAO.getEventTags(rs.getInt("id")));
        	//TODO: Controllare cosa succede se la lista di tags è nulla (cioè "EventTagDAO.getEventTags(rs.getInt("id"))" come
        	//		si comporta se l'evento non ha tag ad esso associati)
        } else {
        	event = null;
        }
        
        rs.close();
        
        if (stmt != null)
        	stmt.close();
        
		return event;
	}
	
	public static List<Event> getEventByCities(String departureCity, String destinationCity) throws IOException, SQLException {
		List<Event> eventList = new ArrayList<Event>();
		String query = "SELECT * FROM event";
		if(departureCity.equals("All") && destinationCity.equals("All")) {
			query += " ORDER BY id DESC;";
		} else {
			if(departureCity.equals("All")) {
				query += " WHERE destination_city='" + destinationCity + "'";
			} else if(destinationCity.equals("All")){
				query += " WHERE departure_city='" + departureCity + "'";
			} else {
				query += " WHERE departure_city='" + departureCity + "' AND destination_city='" + destinationCity + "'";
			}
			query += " ORDER BY id DESC;";
		}
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        
		ResultSet rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			Image image;
			if(rs.getBinaryStream("image") != null) {
	        	BufferedImage img = ImageIO.read(rs.getBinaryStream("image"));
	        	image = SwingFXUtils.toFXImage(img, null);
			} else {
				image = null;
			}
			eventList.add(new Event(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("owner_username"),
        			rs.getString("departure_city"), rs.getString("destination_city"), rs.getDate("departure_date"),
        			rs.getDate("return_date"), image, rs.getDate("create_time"), EventTagDAO.getEventTags(rs.getInt("id"))));
		}
       
        if (stmt != null)
        	stmt.close();
       		
		return eventList;
	}
	
//	public static List<Event> getEventByCities(String departureCity, String destinationCity, User user) throws SQLException, IOException {
//		if(user == null)
//			return getEventByCities(departureCity, destinationCity);
//		
//		List<Event> eventList = new ArrayList<Event>();
//		String query = "SELECT * FROM event";
//		if(departureCity.equals("All") && destinationCity.equals("All")) {
//			query += " ORDER BY id DESC;";
//		} else {
//			if(departureCity.equals("All")) {
//				query += " WHERE destination_city='" + destinationCity + "'";
//			} else if(destinationCity.equals("All")){
//				query += " WHERE departure_city='" + departureCity + "'";
//			} else {
//				query += " WHERE departure_city='" + departureCity + "' AND destination_city='" + destinationCity + "'";
//			}
//			query += " ORDER BY id DESC;";
//		}
//		
//		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//        
//		ResultSet rs = stmt.executeQuery(query);
//		
//		while(rs.next()) {
//			Image image;
//			if(rs.getBinaryStream("image") != null) {
//	        	BufferedImage img = ImageIO.read(rs.getBinaryStream("image"));
//	        	image = SwingFXUtils.toFXImage(img, null);
//			} else {
//				image = null;
//			}
//        	eventList.add(new Event(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("owner_username"),
//        			rs.getString("departure_city"), rs.getString("destination_city"), rs.getDate("departure_date"),
//        			rs.getDate("return_date"), image, rs.getDate("create_time"), EventTagDAO.getEventTags(rs.getInt("id"))));
//        	}
//       
//        if (stmt != null)
//        	stmt.close();
//       		
//		return eventList;
//	}
	
	public static List<Event> getEventByTags(List<String> tagList) throws SQLException, IOException {
		List<Event> eventList = new ArrayList<Event>();
		
		String query = "SELECT * FROM event WHERE id IN (SELECT DISTINCT ET.event_id FROM event_tag ET WHERE ET.tag_name IN (";
		for(String tag: tagList)
        	query += "'" + tag + "', ";
        query = query.substring(0, query.length()-2) + ") ";
        query += "GROUP BY ET.event_id HAVING COUNT(DISTINCT ET.tag_name) = " + tagList.size() + ") ORDER BY id DESC;";
        
        System.out.println(query);
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        
		ResultSet rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			Image image;
			if(rs.getBinaryStream("image") != null) {
	        	BufferedImage img = ImageIO.read(rs.getBinaryStream("image"));
	        	image = SwingFXUtils.toFXImage(img, null);
			} else {
				image = null;
			}
			eventList.add(new Event(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("owner_username"),
        			rs.getString("departure_city"), rs.getString("destination_city"), rs.getDate("departure_date"),
        			rs.getDate("return_date"), image, rs.getDate("create_time"), EventTagDAO.getEventTags(rs.getInt("id"))));
		}
       
        if (stmt != null)
        	stmt.close();
       		
		return eventList;
	}
	
	public static List<Event> getEventByCitiesAndTags(String departureCity, String destinationCity, List<String> tagList) throws SQLException, IOException {
		
		if(tagList.size() == 0) 
			return getEventByCities(departureCity, destinationCity);
		
		
		List<Event> eventList = new ArrayList<Event>();
		
		String queryCities = "SELECT * FROM event WHERE 1=1";
		if(departureCity.equals("All") && destinationCity.equals("All")) {
			;
		} else if(departureCity.equals("All")) {
			queryCities += " AND destination_city='" + destinationCity + "'";
		} else if(destinationCity.equals("All")){
			queryCities += " AND departure_city='" + departureCity + "'";
		} else {
			queryCities += " AND departure_city='" + departureCity + "' AND destination_city='" + destinationCity + "'";
		}
		
		
		String queryTags = " AND id IN (SELECT DISTINCT ET.event_id FROM event_tag ET WHERE ET.tag_name IN (";
		for(String tag: tagList)
        	queryTags += "'" + tag + "', ";
        queryTags = queryTags.substring(0, queryTags.length()-2) + ") ";
        queryTags += "GROUP BY ET.event_id HAVING COUNT(DISTINCT ET.tag_name) = " + tagList.size() + ") ORDER BY id DESC;";
		
		
		String query = queryCities + queryTags;
		
        System.out.println(query);
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        
		ResultSet rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			Image image;
			if(rs.getBinaryStream("image") != null) {
	        	BufferedImage img = ImageIO.read(rs.getBinaryStream("image"));
	        	image = SwingFXUtils.toFXImage(img, null);
			} else {
				image = null;
			}
			eventList.add(new Event(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("owner_username"),
        			rs.getString("departure_city"), rs.getString("destination_city"), rs.getDate("departure_date"),
        			rs.getDate("return_date"), image, rs.getDate("create_time"), EventTagDAO.getEventTags(rs.getInt("id"))));
		}
       
        if (stmt != null)
        	stmt.close();
       		
		return eventList;
	}
}
