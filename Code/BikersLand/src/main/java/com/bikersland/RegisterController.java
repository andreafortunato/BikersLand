package com.bikersland;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bikersland.db.EmailException;
import com.bikersland.db.UserDAO;
import com.bikersland.db.UsernameException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class RegisterController {
	@FXML
    private TextField txtName;

    @FXML
    private TextField txtSurname;
    
    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtEmail1;

    @FXML
    private TextField txtEmail2;

    @FXML
    private PasswordField txtPassword1;

    @FXML
    private PasswordField txtPassword2;

    @FXML
    private Button btnRegister;
    
    private boolean validEmail = false;
    
    private ChangeListener<String> checkEnableBtnRegister = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			List<TextField> textFields = Arrays.asList(txtName, txtSurname, txtUsername, txtEmail1, txtEmail2, txtPassword1, txtPassword2);
			
			for(TextField tf : textFields) {
				if(tf == null) {
					System.out.println("QUALCOSA E' NULL!");
					System.exit(-1);
				}
				if(tf.getText().strip().length() == 0) {
					btnRegister.setDisable(true);
					return;
				}
			}
			
			if(!validEmail) {
				btnRegister.setDisable(true);
				return;
			}
			
			btnRegister.setDisable(false);
		}
	};
	
	public void initialize() {
		List<TextField> textFields = Arrays.asList(txtName, txtSurname, txtUsername, txtEmail1, txtEmail2, txtPassword1, txtPassword2);
		
		ChangeListener<String> emailListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!txtEmail1.getText().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
//					txtEmail1.getStyleClass().removeIf(style -> style.equals("text-field-valid"));
					txtEmail1.getStyleClass().add("text-field-error");
					validEmail = false;
				} else {
					txtEmail1.getStyleClass().removeIf(style -> style.equals("text-field-error"));
//					txtEmail1.getStyleClass().add("text-field-valid");
					if(!txtEmail1.getText().strip().equals(txtEmail2.getText().strip())) {
//						txtEmail2.getStyleClass().removeIf(style -> style.equals("text-field-valid"));
						txtEmail2.getStyleClass().add("text-field-error");
						validEmail = false;
					} else {
						txtEmail2.getStyleClass().removeIf(style -> style.equals("text-field-error"));
//						txtEmail2.getStyleClass().add("text-field-valid");
						validEmail = true;
					}				
				}
			}
		};
		
		txtEmail1.textProperty().addListener(emailListener);
		txtEmail2.textProperty().addListener(emailListener);	
		
		for(TextField tf : textFields)
			tf.textProperty().addListener(checkEnableBtnRegister);
		
		try {
			NonSoComeChiamarla.addTextLimiter(txtName, 32);
			NonSoComeChiamarla.addTextLimiter(txtSurname, 32);
			NonSoComeChiamarla.addTextLimiter(txtUsername, 16);
			NonSoComeChiamarla.addTextLimiter(txtEmail1, 128);
			NonSoComeChiamarla.addTextLimiter(txtEmail2, 128);
			NonSoComeChiamarla.addTextLimiter(txtPassword1, 64);
			NonSoComeChiamarla.addTextLimiter(txtPassword2, 64);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void register() throws SQLException, IOException {
		if(!txtPassword1.getText().equals(txtPassword2.getText())) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR, "Registration Error", "Passwords do not match", "The two passwords you entered do not match.\n\n Please try again...", null);
			txtPassword1.setText("");
			txtPassword2.setText("");
			txtPassword1.requestFocus();
			return;
		}
		
		String name = txtName.getText().strip();
		String surname = txtSurname.getText().strip();
		String username = txtUsername.getText().strip();
		String email = txtEmail1.getText().strip();
		String password = txtPassword1.getText().strip();
		
		User newUser = new User(name, surname, username, email, password);
		
		try {
			UserDAO.setUser(newUser);
			NonSoComeChiamarla.showTimedAlert(AlertType.INFORMATION, "Success!" , "Registration succesfully completed!" , "Hi " + username + "!\nWelcome to BikersLand!", null);
			App.setRoot("Homepage");
		} catch (UsernameException e) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR, "Registration Error" , "Username already exists!" , "A user already exists with username ", username);
			txtUsername.setText("");
			txtUsername.requestFocus();
		} catch (EmailException e) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR, "Registration Error" , "Email already exists!" , "A user already exists with email ", email);
			txtEmail1.setText("");
			txtEmail2.setText("");
			txtEmail1.requestFocus();
		}
				

	}
}
