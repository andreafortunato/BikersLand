package com.bikersland.controller.graphics;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.bikersland.Main;
import com.bikersland.InstantTooltip;
import com.bikersland.NonSoComeChiamarla;
import com.bikersland.bean.UserBean;
import com.bikersland.controller.application.RegisterControllerApp;
import com.bikersland.exception.AutomaticLoginException;
import com.bikersland.exception.ImageFileException;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.user.DuplicateEmailException;
import com.bikersland.exception.user.DuplicateUsernameException;
import com.bikersland.exception.user.EmailException;
import com.bikersland.exception.user.NameException;
import com.bikersland.exception.user.PasswordException;
import com.bikersland.exception.user.SurnameException;
import com.bikersland.exception.user.UsernameException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RegisterControllerView {

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
    
    private InstantTooltip previewProfileImageTooltip = new InstantTooltip();
    private File imageFile = null;
    
    private UserBean userBean = new UserBean();
    
    private ChangeListener<String> checkEnableBtnRegister = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			List<TextField> textFields = Arrays.asList(txtName, txtSurname, txtUsername, txtEmail1, txtEmail2, txtPassword1, txtPassword2);
			
			for(TextField tf : textFields) {
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
	
	private ChangeListener<String> emailListener = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			try {
				/* L'email 'txtEmail1' deve essere valida, mentre l'email 'txtEmail2' deve essere uguale alla prima */
				userBean.setEmail(txtEmail1.getText());
				
				txtEmail1.getStyleClass().removeIf(style -> style.equals("text-field-error"));
				if(!txtEmail1.getText().strip().equals(txtEmail2.getText().strip())) {
					txtEmail2.getStyleClass().add("text-field-error");
					validEmail = false;
				} else {
					txtEmail2.getStyleClass().removeIf(style -> style.equals("text-field-error"));
					validEmail = true;
				}
			} catch (EmailException e) {
				txtEmail1.getStyleClass().add("text-field-error");
				validEmail = false;
			}
		}
	};
	
	public void initialize() {
		txtEmail1.textProperty().addListener(emailListener);
		txtEmail2.textProperty().addListener(emailListener);
		
		List<TextField> textFields = Arrays.asList(txtName, txtSurname, txtUsername, txtEmail1, txtEmail2, txtPassword1, txtPassword2);
		for(TextField tf : textFields)
			tf.textProperty().addListener(checkEnableBtnRegister);
		
		lblImageName.setTooltip(previewProfileImageTooltip);
		
//		try {
//			NonSoComeChiamarla.addTextLimiter(txtName, 32);
//			NonSoComeChiamarla.addTextLimiter(txtSurname, 32);
//			NonSoComeChiamarla.addTextLimiter(txtUsername, 16);
//			NonSoComeChiamarla.addTextLimiter(txtEmail1, 128);
//			NonSoComeChiamarla.addTextLimiter(txtEmail2, 128);
//			NonSoComeChiamarla.addTextLimiter(txtPassword1, 64);
//			NonSoComeChiamarla.addTextLimiter(txtPassword2, 64);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	@FXML
	private void register() {
		if(!txtPassword1.getText().equals(txtPassword2.getText())) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("timedalert_register_error_title"),
					Main.bundle.getString("timedalert_register_password_error_header"),
					Main.bundle.getString("timedalert_register_password_error_content"), null);
			
//			txtPassword1.setText("");
//			txtPassword2.setText("");
//			txtPassword1.requestFocus();
			
			txtPassword2.setText("");
			txtPassword2.requestFocus();
			return;
		}
		
		try {
			userBean.setName(txtName.getText().strip());
			userBean.setSurname(txtSurname.getText().strip());
			userBean.setUsername(txtUsername.getText().strip());
			userBean.setPassword(txtPassword1.getText().strip());
			
			RegisterControllerApp.register(userBean);
			
			Main.setRoot("Homepage");
		} catch (DuplicateUsernameException due) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("timedalert_register_error_title"),
					Main.bundle.getString("timedalert_register_error_username_header"),
					due.getMessage(), userBean.getUsername());
			txtUsername.setText("");
			txtUsername.requestFocus();
		} catch (DuplicateEmailException dee) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("timedalert_register_error_title"),
					Main.bundle.getString("timedalert_register_error_email_header"),
					dee.getMessage(), userBean.getEmail());
			txtEmail1.setText("");
			txtEmail2.setText("");
			txtEmail1.requestFocus();
		} catch (NameException ne) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("error"),
					Main.bundle.getString("timedalert_register_name_error"),
					ne.getMessage(), null);
		} catch (SurnameException se) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("error"),
					Main.bundle.getString("timedalert_register_surname_error"),
					se.getMessage(), null);
		} catch (UsernameException ue) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("error"),
					Main.bundle.getString("timedalert_register_username_error"),
					ue.getMessage(), null);
		} catch (PasswordException pe) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("error"),
					Main.bundle.getString("timedalert_register_password_error"),
					pe.getMessage(), null);
		} catch (AutomaticLoginException ale) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("timedalert_internal_error"),
					Main.bundle.getString("timedalert_sql_ex_header"),
					ale.getMessage(), Main.logFile);
			
			Main.setRoot("Login");
		} catch (InternalDBException idbe) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("timedalert_internal_error"),
					Main.bundle.getString("timedalert_sql_ex_header"),
					idbe.getMessage(), Main.logFile);
		}
		
		
//		String name = txtName.getText().strip();
//		String surname = txtSurname.getText().strip();
//		String username = txtUsername.getText().strip();
//		String email = txtEmail1.getText().strip();
//		String password = txtPassword1.getText().strip();
//		
//		Image userImage = this.imageFile == null ? null : new Image(this.imageFile.toURI().toString());
//		User newUser = new User(name, surname, username, email, password, userImage);
//		
//		try {
//			UserDAO.setUser(newUser);
//			NonSoComeChiamarla.showTimedAlert(AlertType.INFORMATION,
//					App.bundle.getString("success"),
//					App.bundle.getString("timedalert_register_success_header"),
//					App.bundle.getString("timedalert_register_success_content_1")
//					+ username +
//					App.bundle.getString("timedalert_register_success_content_2"), null);
//			
//			LoginSingleton.getLoginInstance().setUser(UserDAO.getUserByUsername(username));
//			App.setRoot("Homepage");
//		} catch (UsernameException e) {
//			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
//					App.bundle.getString("timedalert_register_error_title"),
//					App.bundle.getString("timedalert_register_error_username_header"),
//					App.bundle.getString("timedalert_register_error_username_content"), username);
//			txtUsername.setText("");
//			txtUsername.requestFocus();
//		} catch (EmailException e) {
//			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
//					App.bundle.getString("timedalert_register_error_title"),
//					App.bundle.getString("timedalert_register_error_email_header"),
//					App.bundle.getString("timedalert_register_error_email_content"), email);
//			txtEmail1.setText("");
//			txtEmail2.setText("");
//			txtEmail1.requestFocus();
//		}
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
	    	try {
				userBean.setImage(choosedFile);
				
				this.imageFile = choosedFile;
				
				Image profileImage = new Image(this.imageFile.toURI().toString(), 100, 100, true, true);
				ImageView profileImagePreview = new ImageView(profileImage);
				Circle imgCircle = new Circle(50);
			    imgCircle.setCenterX(profileImage.getWidth()*0.5);
			    imgCircle.setCenterY(profileImage.getHeight()*0.5);
			    profileImagePreview.setClip(imgCircle);
				
				previewProfileImageTooltip.setGraphic(profileImagePreview);
				lblImageName.setText(this.imageFile.getName());
				btnImage.setVisible(false);
				hbImageSelected.setVisible(true);
			} catch (ImageFileException e) {
				NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
						Main.bundle.getString("timedalert_image_error_title"),
						Main.bundle.getString("timedalert_image_error_header"),
						e.getMessage(), null);
			}
    	} else {
    		Image nullImage = null;
    		userBean.setImage(nullImage);
    	}
    	
//    	if(choosedFile != null) {
//    		if(choosedFile.length() < 4194304) {
//    			this.imageFile = choosedFile;
//    			
//    			Image profileImage = new Image(this.imageFile.toURI().toString(), 100, 100, true, true);
//    			ImageView profileImagePreview = new ImageView(profileImage);
//    			Circle imgCircle = new Circle(50);
//    		    imgCircle.setCenterX(profileImage.getWidth()*0.5);
//    		    imgCircle.setCenterY(profileImage.getHeight()*0.5);
//    		    profileImagePreview.setClip(imgCircle);
//    			
//    			imageTooltip.setGraphic(profileImagePreview);
//    			lblImageName.setText(this.imageFile.getName());
//    			btnImage.setVisible(false);
//    			hbImageSelected.setVisible(true);
//    		} else {
//    			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
//    					App.bundle.getString("timedalert_image_error_title"),
//    					App.bundle.getString("timedalert_image_error_header"),
//    					App.bundle.getString("timedalert_image_error_content"), null);
//    		}
//    	}
    }
    
    @FXML
    private void removeImage() {
    	this.imageFile = null;
    	hbImageSelected.setVisible(false);
    	btnImage.setVisible(true);
    	
    	Image nullImage = null;
		userBean.setImage(nullImage);
    }
}
