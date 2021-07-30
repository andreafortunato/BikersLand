package com.bikersland.controller.graphics;

import com.bikersland.Main;
import com.bikersland.controller.application.LoginControllerApp;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.InvalidLoginException;
import com.bikersland.utility.TimedAlert;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
		
		/*
		try {
			ConvertMethods.addTextLimiter(txtUser, 32);
			ConvertMethods.addTextLimiter(txtPassword, 64);
		} catch (IOException e) {
			Logger.getGlobal().log(Level.SEVERE, "Catched IOException in initialize() method, inside LoginControllerView.java", e);
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString("timedalert_internal_error"),
					Main.getBundle().getString("timedalert_system_error_header"),
					Main.getBundle().getString("timedalert_system_error_content"), Main.getLogFile());
		}
		*/
		
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
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString("timedalert_login_error_title"),
					Main.getBundle().getString("timedalert_login_error_header"),
					Main.getBundle().getString("timedalert_login_error_content"), null);
			txtUser.setText("");
			txtPassword.setText("");
			txtUser.requestFocus();
		} catch (InternalDBException idbe) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString("timedalert_internal_error"),
					Main.getBundle().getString("timedalert_sql_ex_header"),
					idbe.getMessage(), Main.getLogFile());
		} 
	}
}
