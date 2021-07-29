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
import java.util.Locale;

import javax.imageio.ImageIO;

import com.bikersland.Main;
import com.bikersland.db.queries.CRUDQueries;
import com.bikersland.db.queries.SimpleQueries;
import com.bikersland.exception.ImageConversionException;
import com.bikersland.exception.event.EventNotFoundException;
import com.bikersland.exception.user.DuplicateEmailException;
import com.bikersland.exception.user.DuplicateUsernameException;
import com.bikersland.model.Event;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class EventDAO {
	
	private static final String ID_COL = "id";
	private static final String TITLE_COL = "title";
	private static final String DESCRIPTION_COL = "description";
	private static final String OWNER_USERNAME_COL = "owner_username";
	private static final String DEPARTURE_CITY_COL = "departure_city";
	private static final String DESTINATION_CITY_COL = "destination_city";
	private static final String DEPARTURE_DATE_COL = "departure_date";
	private static final String RETURN_DATE_COL = "return_date";
	private static final String IMAGE_COL = "image";
	private static final String CREATE_TIME_COL = "create_time";
	
	private EventDAO() {}
	
	public static Event createNewEvent(Event event) throws SQLException, ImageConversionException, EventNotFoundException {
		CallableStatement stmtCreateEvent = null;
		ResultSet createNewEventRS = null;
		
		try {
			stmtCreateEvent = DB_Connection.getConnection().prepareCall("{CALL CreateEvent(?,?,?,?,?,?,?,?)}");
			createNewEventRS = CRUDQueries.createNewEventQuery(stmtCreateEvent, event);
			
			if(createNewEventRS.next()) {
				Image image;
				if(createNewEventRS.getBinaryStream(IMAGE_COL) != null) {
					try {
						BufferedImage img = ImageIO.read(createNewEventRS.getBinaryStream(IMAGE_COL));
						image = SwingFXUtils.toFXImage(img, null);
					} catch (IOException e) {
						/* Se si verifica un problema nella conversione dell'immagine, lancio 
						   un'eccezione e gestisco l'errore come se fosse dovuto dal Database */
						createNewEventRS.close();
						stmtCreateEvent.close();
						throw new ImageConversionException(e);
					}
				} else {
					image = null;
				}
				
				return new Event(createNewEventRS.getInt(ID_COL), createNewEventRS.getString(TITLE_COL), createNewEventRS.getString(DESCRIPTION_COL),
						createNewEventRS.getString(OWNER_USERNAME_COL), createNewEventRS.getString(DEPARTURE_CITY_COL),
						createNewEventRS.getString(DESTINATION_CITY_COL), createNewEventRS.getDate(DEPARTURE_DATE_COL),
						createNewEventRS.getDate(RETURN_DATE_COL), image, createNewEventRS.getDate(CREATE_TIME_COL), event.getTags());
			}
			
			throw new EventNotFoundException();
			
		} finally {
			if(createNewEventRS != null)
				createNewEventRS.close();
			
			if(stmtCreateEvent != null)
				stmtCreateEvent.close();
		}
	}
	
//	// TODO: Da rimuovere?
//	public static Event getEventByID(Integer id) throws SQLException, IOException {
//		Event event;
//		String query = "SELECT * FROM event WHERE id='" + id + "';";
//		
//		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//                
//        ResultSet rs = stmt.executeQuery(query);
//        
//        if(rs.first()) {
//        	Image image;
//			if(rs.getBinaryStream("image") != null) {
//	        	BufferedImage img = ImageIO.read(rs.getBinaryStream("image"));
//	        	image = SwingFXUtils.toFXImage(img, null);
//			} else {
//				image = null;
//			}
//        	event = new Event(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("owner_username"),
//        			rs.getString("departure_city"), rs.getString("destination_city"), rs.getDate("departure_date"),
//        			rs.getDate("return_date"), image, rs.getDate("create_time"), EventTagDAO.getEventTags(rs.getInt("id")));
//        	//TODO: Controllare cosa succede se la lista di tags è nulla (cioè "EventTagDAO.getEventTags(rs.getInt("id"))" come
//        	//		si comporta se l'evento non ha tag ad esso associati)
//        } else {
//        	event = null;
//        }
//        
//        rs.close();
//        
//        if (stmt != null)
//        	stmt.close();
//        
//		return event;
//	}
	
	public static List<Event> getEventByCities(String departureCity, String destinationCity) throws SQLException {
		List<Event> eventList = new ArrayList<Event>();
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	        rs = SimpleQueries.getEventByCities(stmt, departureCity, destinationCity);
	        
	        while(rs.next()) {
				Image image;
				if(rs.getBinaryStream(IMAGE_COL) != null) {
		        	BufferedImage img;
					try {
						img = ImageIO.read(rs.getBinaryStream(IMAGE_COL));
					} catch (IOException ioe) {
						rs.close();
						stmt.close();
					
						throw new SQLException(ioe);
					}
		        	image = SwingFXUtils.toFXImage(img, null);
				} else {
					image = null;
				}
				eventList.add(new Event(rs.getInt(ID_COL), rs.getString(TITLE_COL), rs.getString(DESCRIPTION_COL), rs.getString(OWNER_USERNAME_COL),
	        			rs.getString(DEPARTURE_CITY_COL), rs.getString(DESTINATION_CITY_COL), rs.getDate(DEPARTURE_DATE_COL),
	        			rs.getDate(RETURN_DATE_COL), image, rs.getDate(CREATE_TIME_COL), EventTagDAO.getEventTags(rs.getInt(ID_COL))));
			}
	        
	        return eventList;
		}finally {
			if (stmt != null)
				stmt.close();
			
			if(rs != null) 
				rs.close();
		}
		
		
	}
	
	/*
	
	// TODO: Da rimuovere?
	public static List<Event> getEventByTags(List<Integer> tagList) throws SQLException, IOException {
		List<Event> eventList = new ArrayList<Event>();
		
		String query = "SELECT * FROM event WHERE id IN (SELECT DISTINCT ET.event_id FROM event_tag ET WHERE ET.tag_id IN (";
		for(Integer tag: tagList)
        	query += "'" + tag + "', ";
        query = query.substring(0, query.length()-2) + ") ";
        query += "GROUP BY ET.event_id HAVING COUNT(DISTINCT ET.tag_id) = " + tagList.size() + ") ORDER BY id DESC;";
        
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
		if(App.locale == Locale.ITALIAN)
			return getEventByCitiesAndTags(departureCity, destinationCity, tagList, "it");
		else
			return getEventByCitiesAndTags(departureCity, destinationCity, tagList, "en");
	}*/
	
	
	public static List<Event> getEventByCitiesAndTags(String departureCity, String destinationCity, List<String> tagList, String language) throws SQLException{
		
		if(tagList.size() == 0) 
			return getEventByCities(departureCity, destinationCity);

		List<Event> eventList = new ArrayList<Event>();
		
		ResultSet rs = null;
		Statement stmt = null;
		
		
		try {
			stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	        rs = SimpleQueries.getEventByCitiesAndTags(stmt, departureCity, destinationCity, tagList, language);
	        
	        while(rs.next()) {
				Image image;
				if(rs.getBinaryStream(IMAGE_COL) != null) {
		        	BufferedImage img;
					try {
						img = ImageIO.read(rs.getBinaryStream(IMAGE_COL));
					} catch (IOException ioe) {
						rs.close();
						stmt.close();
					
						throw new SQLException(ioe);
					} 
		        	image = SwingFXUtils.toFXImage(img, null);
				} else {
					image = null;
				}
				eventList.add(new Event(rs.getInt(ID_COL), rs.getString(TITLE_COL), rs.getString(DESCRIPTION_COL), rs.getString(OWNER_USERNAME_COL),
	        			rs.getString(DEPARTURE_CITY_COL), rs.getString(DESTINATION_CITY_COL), rs.getDate(DEPARTURE_DATE_COL),
	        			rs.getDate(RETURN_DATE_COL), image, rs.getDate(CREATE_TIME_COL), EventTagDAO.getEventTags(rs.getInt(ID_COL))));
			}
	        
	        return eventList;
		}finally {
			if (stmt != null)
				stmt.close();
			
			if(rs != null) 
				rs.close();
		}
		
	}
}
