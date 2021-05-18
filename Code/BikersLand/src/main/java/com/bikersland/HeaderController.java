package com.bikersland;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class HeaderController {
	
	@FXML
    private ImageView btnLanguage;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnLogin;

    @FXML
    public ImageView imgLogo;
    
    public void initialize() {
    	System.out.println("Header inizializzato");
    	System.out.println(btnRegister.getText());
    }
    
    @FXML
    private void login() {
    	System.out.println("Hai cliccato ACCEDI");    
    	System.out.println(getClass());
    	System.out.println();
    }
    
    @FXML
    private void register() throws IOException {
    	System.out.println("Hai cliccato REGISTRATI");
    	App.setRoot("Register");
    }
    
    @FXML
    private void btnClick(ActionEvent event) {
    	System.out.println("Metodo comune invocato da " + ((Button)event.getSource()).getText() + "!");
    }
    
    @FXML
    private void goToHomepage() throws IOException {
    	App.setRoot("Homepage");
    }
    
    public void setRegTxt() {
    	btnLogin.setText("ciao");
    }

}
