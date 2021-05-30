package com.bikersland;

import java.sql.Date;

import javafx.scene.image.Image;

public class Event {
	private Integer id = null;
	private String title = null;
	private String description = null;
	private String owner_username = null;
	private String departure_city = null;
	private String destination_city = null;
	private Date departure_date = null;
	private Date return_date = null;
	private Image image = null;
	
	public Event() {}
	
	public Event(Integer id, String title, String description, String owner_username, String departure_city, String destination_city, Date departure_date, Date return_date,Image image) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.owner_username = owner_username;
		this.departure_city = departure_city;
		this.destination_city = destination_city;
		this.departure_date = departure_date;
		this.return_date = return_date;
		this.image = image;
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
	
	
}
