package com.bikersland.controller.graphics;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;

import com.bikersland.Main;
import com.bikersland.InstantTooltip;
import com.bikersland.NonSoComeChiamarla;
import com.bikersland.controller.application.HeaderControllerApp;
import com.bikersland.db.EventDAO;
import com.bikersland.db.TagDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.singleton.LoginSingleton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class HeaderControllerView {
	
	@FXML
    private ImageView btnLanguage;
	
	@FXML
	private Button btnNewEvent;

    @FXML
    private HBox hbNotLoggedIn;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnLogin;

    @FXML
    private HBox hbLoggedIn;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnLogout;

    @FXML
    private ImageView imgLogo;
    
    public void initialize() {
    	if(LoginSingleton.getLoginInstance().getUser() != null) {
    		hbNotLoggedIn.setVisible(false);
    		hbLoggedIn.setVisible(true);
    		btnNewEvent.setVisible(true);
    	} else {
    		hbNotLoggedIn.setVisible(true);
    		hbLoggedIn.setVisible(false);
    		btnNewEvent.setVisible(false);
    	}
    	
    	
    	InstantTooltip flagTooltip = new InstantTooltip(Main.bundle.getString("change_lang"));
    	Tooltip.install(btnLanguage, flagTooltip);
    	
    	if(Main.locale == Locale.ITALIAN)
    		btnLanguage.setImage(new Image(Main.class.getResource("img/italy.png").toString()));
    	else
    		btnLanguage.setImage(new Image(Main.class.getResource("img/usa.png").toString()));
    	
    }
    
    @FXML
    private void login() throws IOException {
    	Main.setRoot("Login");
    }
    
    @FXML
    private void register() throws IOException, SQLException {
    	Main.setRoot("Register");
//    	Event event = EventDAO.getEventByID(1);
//    	App.setRoot("EventDetails", event);
    }
    
    @FXML
    private void btnClick(ActionEvent event) {
    	System.out.println("Metodo comune invocato da " + ((Button)event.getSource()).getText() + "!");
    }
    
    @FXML
    private void goToHomepage() throws IOException {
    	Main.setRoot("Homepage");
    }
    
    @FXML
    private void goToProfile() throws IOException {
    	Main.setRoot("Profile");
    }
    
    @FXML
    private void logout() throws IOException {
    	LoginSingleton.logout();
    	Main.setRoot("Homepage");
    }
    
    @FXML
    private void newEvent() throws IOException {
    	Main.setRoot("NewEvent");
    }
    
    @FXML
    private void changeLanguage() {
    	if(Main.locale == Locale.ITALIAN) {
    		Main.locale = Locale.ENGLISH;
    		btnLanguage.setImage(new Image(Main.class.getResource("img/usa.png").toString()));
    	} else {
    		Main.locale = Locale.ITALIAN;
    		btnLanguage.setImage(new Image(Main.class.getResource("img/italy.png").toString()));
    	}
    	//App.tags = TagDAO.getTags();
    	try {
			Main.tags = HeaderControllerApp.getTags();
		} catch (InternalDBException idbe) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("timedalert_internal_error"),
					Main.bundle.getString("timedalert_sql_ex_header"),
					idbe.getMessage(), Main.logFile);
			
			//App.setRoot("Homepage");
		}
    	Main.setRoot("Homepage");
    }
}
