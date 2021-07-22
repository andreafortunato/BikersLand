package com.bikersland;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bikersland.db.UserDAO;
import com.bikersland.exceptions.EmailException;
import com.bikersland.exceptions.UsernameException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RegisterController {

	@FXML
    private TextField txtName;

    @FXML
    private TextField txtSurname;

    @FXML
    private TextField txtUsername;

    @FXML
    private Button btnImage;

    @FXML
    private HBox hbImageSelected;

    @FXML
    private Label lblImageName;

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
    
    private Tooltip imageTooltip = new Tooltip();
    private File imageFile = null;
    
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
		
		imageTooltip.setShowDelay(Duration.ZERO);
    	imageTooltip.setHideDelay(Duration.ZERO);
    	lblImageName.setTooltip(imageTooltip);
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
		
		Image userImage = this.imageFile == null ? null : new Image(this.imageFile.toURI().toString());
		User newUser = new User(name, surname, username, email, password, userImage);
		
		try {
			UserDAO.setUser(newUser);
			NonSoComeChiamarla.showTimedAlert(AlertType.INFORMATION, "Success!" , "Registration succesfully completed!" , "Hi " + username + "!\nWelcome to BikersLand!", null);
			LoginSingleton.getLoginInstance().setUser(newUser);
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
	
	@FXML
    private void uploadImage() {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Open Resource File");
    	fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.jpeg", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
            );
    	
    	File choosedFile = fileChooser.showOpenDialog(txtName.getParent().getScene().getWindow());
    	
    	if(choosedFile != null) {
    		if(choosedFile.length() < 4194304) {
    			this.imageFile = choosedFile;
    			
    			Image profileImage = new Image(this.imageFile.toURI().toString(), 100, 100, true, true);
    			ImageView profileImagePreview = new ImageView(profileImage);
    			Circle imgCircle = new Circle(50);
    		    imgCircle.setCenterX(profileImage.getWidth()*0.5);
    		    imgCircle.setCenterY(profileImage.getHeight()*0.5);
    		    profileImagePreview.setClip(imgCircle);
    			
    			imageTooltip.setGraphic(profileImagePreview);
    			lblImageName.setText(this.imageFile.getName());
    			btnImage.setVisible(false);
    			hbImageSelected.setVisible(true);
    		} else {
    			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR, "Image error", "Maximum size exceeded", "The selected image exceeds the maximum size of 4 MB!", null);
    		}
    	}    	
    }
    
    @FXML
    private void removeImage() {
    	this.imageFile = null;
    	hbImageSelected.setVisible(false);
    	btnImage.setVisible(true);
    }
}
