package com.bikersland;

import java.io.IOException;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class HeaderController {
	
	@FXML
    private ImageView btnLanguage;

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
    	} else {
    		hbNotLoggedIn.setVisible(true);
    		hbLoggedIn.setVisible(false);
    	}
    }
    
    @FXML
    private void login() throws IOException {
    	
    	App.setRoot("Login");
    	
    }
    
    @FXML
    private void register() throws IOException {
//    	App.setRoot("Register");
    	Event event = new Event(10, "Titolo eventino", "", "", "", "", new Date(), new Date());
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
}
