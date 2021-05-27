package com.bikersland;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.bikersland.db.EmailException;
import com.bikersland.db.UserDAO;
import com.bikersland.db.UsernameException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class LoginController {
	
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
		
		try {
			NonSoComeChiamarla.addTextLimiter(txtUser, 32);
			NonSoComeChiamarla.addTextLimiter(txtPassword, 64);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void login() throws SQLException, IOException {
		User logged_user = null;
		
		String username_email = txtUser.getText().strip();
		String password = txtPassword.getText().strip();
		
		logged_user = UserDAO.askLogin(username_email, password);
		
		if(logged_user == null) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR, "Login Error", "Username/email or password not found", "No user found.\n\n Please try again...", null);
			txtUser.setText("");
			txtPassword.setText("");
			txtUser.requestFocus();
		} else {
			LoginSingleton Login = LoginSingleton.getLoginInstance();
			Login.setUser(logged_user);
			App.setRoot("Homepage");
		}
		
		//TODO impostare la view pagina personale per l'utente e lasciare la connessione aperta
	}
}
