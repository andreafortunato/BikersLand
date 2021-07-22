package com.bikersland;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.scene.image.Image;

public class User {
	private Integer id = null;
	private String name = null;
	private String surname = null;
	private String username = null;
	private String email = null;
	private String password = null;
	private Image image = null;
	private Date create_time = null;
	
	public User() {}
	
	public User(Integer id, String name, String surname, String username, String email, String password, Image image, Date create_time) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.email = email;
		this.password = password;
		this.image = image;
		this.create_time = create_time;
	}
	
	public User(String name, String surname, String username, String email, String password, Image image) {
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.email = email;
		this.password = password;
		this.image = image;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String toString() {
		String userString = "ID: " + id + "\nName: " + this.name + 
        		"\nSurname: " + this.surname + "\nUsername: " + this.username +
        		"\nEmail: " + this.email + "\nPassword: " + this.password +
        		"\nCreate_time: ";
		
		if(this.create_time == null)
			userString += this.create_time;
		else
			userString += NonSoComeChiamarla.dateToString(this.create_time);
		
		return userString;
	}
}
