package com.bikersland.controller.graphics;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.bikersland.Main;
import com.bikersland.NonSoComeChiamarla;
import com.bikersland.controller.application.LoginControllerApp;
import com.bikersland.exception.InternalDBException;

import UserDAO.InvalidLoginException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class LoginControllerView {
	
	@FXML
    private TextField txtUser;
	
	@FXML
    private PasswordField txtPassword;
	
	@FXML
	private Button btnLogin;
	
	private ChangeListener<String> checkEnableBtnLogin = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			if(txtUser.getText().strip().length() == 0 || txtPassword.getText().strip().length() == 0) {
				btnLogin.setDisable(true);
				return;
			}
			
			btnLogin.setDisable(false);
		}
	};
	 
	public void initialize() {
		txtUser.textProperty().addListener(checkEnableBtnLogin);
		txtPassword.textProperty().addListener(checkEnableBtnLogin);
		
//		try {
//			NonSoComeChiamarla.addTextLimiter(txtUser, 32);
//			NonSoComeChiamarla.addTextLimiter(txtPassword, 64);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		/****************************************/
		// TODO: Da rimuovere
		txtUser.setText("galaxy");
		txtPassword.setText("password");
		/****************************************/
		
	}
	
	@FXML
	private void login() {
//		User logged_user;
		
		try {
//			logged_user = LoginControllerApp.askLogin(txtUser.getText().strip(), txtPassword.getText().strip());
//			
//			LoginSingleton.getLoginInstance().setUser(logged_user);
			
			LoginControllerApp.askLogin(txtUser.getText().strip(), txtPassword.getText().strip());
			
			Main.setRoot("Homepage");
		} catch (InvalidLoginException e) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("timedalert_login_error_title"),
					Main.bundle.getString("timedalert_login_error_header"),
					Main.bundle.getString("timedalert_login_error_content"), null);
			txtUser.setText("");
			txtPassword.setText("");
			txtUser.requestFocus();
		} catch (InternalDBException idbe) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("timedalert_internal_error"),
					Main.bundle.getString("timedalert_sql_ex_header"),
					idbe.getMessage(), Main.logFile);
			
			Logger.getGlobal().log(Level.SEVERE, "Catched SQLException in login() method, inside LoginControllerView.java", idbe);
		} 
	}
}
