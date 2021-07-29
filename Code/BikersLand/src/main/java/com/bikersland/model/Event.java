package com.bikersland.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.bikersland.NonSoComeChiamarla;

import javafx.scene.image.Image;

public class Event {
	private Integer id;
	private String title;
	private String description;
	private String owner_username;
	private String departure_city;
	private String destination_city;
	private Date departure_date;
	private Date return_date;
	private Image image;
	private Date create_time;
	private List<String> tags = new ArrayList<String>();
	
	public Event() {
		this(null, null, null, null, null, null, null, null, null, null, null);
	}
	
	public Event(String title, String description, String owner_username, String departure_city, String destination_city, Date departure_date, Date return_date, Image image, List<String> tags) {
		this(null, title, description, owner_username, departure_city, destination_city, departure_date, return_date, image, null, tags);
	}
	
	public Event(Integer id, String title, String description, String owner_username, String departure_city, String destination_city, Date departure_date, Date return_date, Image image, Date create_time, List<String> tags) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.owner_username = owner_username;
		this.departure_city = departure_city;
		this.destination_city = destination_city;
		this.departure_date = departure_date;
		this.return_date = return_date;
		this.image = image;
		this.create_time = create_time;
		this.tags = tags;
	}
	
	public String toString() {
		return String.format("ID: %s\n"
				+ "Title: %s\n"
				+ "Description: %s\n"
				+ "Owner Username: %s\n"
				+ "Departure City: %s\n"
				+ "Destination City: %s\n"
				+ "Departure Date: %s\n"
				+ "Return Date: %s\n"
				+ "Tag List: %s\n"
				+ "Create time: %s",
				id, title, description, owner_username, departure_city,
				destination_city, NonSoComeChiamarla.dateToString(departure_date),
				NonSoComeChiamarla.dateToString(return_date), tags.toString(),
				NonSoComeChiamarla.dateToString(create_time));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOwner_username() {
		return owner_username;
	}

	public void setOwner_username(String owner_username) {
		this.owner_username = owner_username;
	}

	public String getDeparture_city() {
		return departure_city;
	}

	public void setDeparture_city(String departure_city) {
		this.departure_city = departure_city;
	}

	public String getDestination_city() {
		return destination_city;
	}

	public void setDestination_city(String destination_city) {
		this.destination_city = destination_city;
	}

	public Date getDeparture_date() {
		return departure_date;
	}

	public void setDeparture_date(Date departure_date) {
		this.departure_date = departure_date;
	}

	public Date getReturn_date() {
		return return_date;
	}

	public void setReturn_date(Date return_date) {
		this.return_date = return_date;
	}
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}	
}