package com.bikersland;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import com.bikersland.db.EventDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class HeaderController {
	
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
    }
    
    @FXML
    private void login() throws IOException {
    	
    	App.setRoot("Login");
    	
    }
    
    @FXML
    private void register() throws IOException, SQLException {
//    	App.setRoot("Register");
    	Event event = EventDAO.getEventByID(1);
    	App.setRoot("EventDetails", event);
    }
    
    @FXML
    private void btnClick(ActionEvent event) {
    	System.out.println("Metodo comune invocato da " + ((Button)event.getSource()).getText() + "!");
    }
    
    @FXML
    private void goToHomepage() throws IOException {
    	App.setRoot("Homepage");
    }
    
    @FXML
    private void goToProfile() throws IOException {
    	App.setRoot("Profile");
    }
    
    @FXML
    private void logout() throws IOException {
    	LoginSingleton.logout();
    	App.setRoot("Homepage");
    }
    
    @FXML
    private void newEvent() throws IOException {
    	App.setRoot("NewEvent");
    	
    }
}
