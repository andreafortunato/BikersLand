package com.bikersland.bean;

import java.io.File;
import java.util.Date;

import com.bikersland.App;
import com.bikersland.NonSoComeChiamarla;
import com.bikersland.exception.ImageFileException;
import com.bikersland.exception.user.EmailException;
import com.bikersland.exception.user.NameException;
import com.bikersland.exception.user.PasswordException;
import com.bikersland.exception.user.SurnameException;
import com.bikersland.exception.user.UsernameException;

import javafx.scene.image.Image;

public class UserBean {
	private Integer id;
	private String name;
	private String surname;
	private String username;
	private String email;
	private String password;
	private Image image;
	private Date create_time;
	
	public UserBean() {
		this(null, null, null, null, null, null, null, null);
	}
	
	public UserBean(String name, String surname, String username, String email, String password, Image image) {
		this(null, name, surname, username, email, password, image, null);
	}
	
	public UserBean(Integer id, String name, String surname, String username, String email, String password, Image image, Date create_time) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.email = email;
		this.password = password;
		this.image = image;
		this.create_time = create_time;
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

	public void setName(String name) throws NameException {
		String stripped_name = name.strip();
		validateName(stripped_name);
		this.name = stripped_name;
	}
	
	private void validateName(String stripped_name) throws NameException {		
		if(stripped_name.length() < 2)
			throw new NameException(App.bundle.getString("ex_user_name_short"));
		
		if(stripped_name.length() > 32)
			throw new NameException(App.bundle.getString("ex_user_name_long"));
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) throws SurnameException {
		String stripped_surname = surname.strip();
		validateSurname(stripped_surname);
		this.surname = stripped_surname;
	}
	
	private void validateSurname(String stripped_surname) throws SurnameException {
		if(stripped_surname.length() < 2)
			throw new SurnameException(App.bundle.getString("ex_user_surname_short"));
		
		if(stripped_surname.length() > 32)
			throw new SurnameException(App.bundle.getString("ex_user_surname_long"));
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) throws UsernameException {
		String stripped_username = username.strip();
		validateUsername(stripped_username);
		this.username = stripped_username;
	}
	
	private void validateUsername(String stripped_username) throws UsernameException {
		if(stripped_username.length() < 2)
			throw new UsernameException(App.bundle.getString("ex_user_username_short"));
		
		if(stripped_username.length() > 16)
			throw new UsernameException(App.bundle.getString("ex_user_username_long"));
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws EmailException {
		String stripped_email = email.strip();
		validateEmail(stripped_email);
		this.email = stripped_email;
	}
	
	private void validateEmail(String stripped_email) throws EmailException {		
		if(stripped_email.length() < 6 ||
				stripped_email.length() > 128 ||
				!stripped_email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"))
			throw new EmailException();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws PasswordException {
		validatePassword(password);
		this.password = password;
	}
	
	private void validatePassword(String password) throws PasswordException {
		if(password.length() < 6)
			throw new PasswordException(App.bundle.getString("ex_user_password_short"));
		if(password.length() > 64)
			throw new PasswordException(App.bundle.getString("ex_user_password_long"));
		if(!password.matches(".*\\d.*"))
			throw new PasswordException(App.bundle.getString("ex_user_password_no_number"));
		if(!password.matches(".*[A-Z].*"))
			throw new PasswordException(App.bundle.getString("ex_user_password_no_capital_letter"));
		if(!password.matches(".*[.!@#$%&*()_+=|<>?{}\\[\\]~-].*"))
			throw new PasswordException(App.bundle.getString("ex_user_password_no_special_char"));
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
	
	public void setImage(File imageFile) throws ImageFileException {
		validateImageFile(imageFile);
		setImage(new Image(imageFile.toURI().toString()));
	}
	
	private void validateImageFile(File imageFile) throws ImageFileException {
    	if(imageFile.length() > 4194304)
    		throw new ImageFileException(App.bundle.getString("ex_image_too_big"));
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
