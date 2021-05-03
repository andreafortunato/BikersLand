package com.bikersland;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RegisterController {
	@FXML
	private TextField email1;
	
	@FXML
	private TextField email2;
	
	public void initialize() {
		ChangeListener<String> emailListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!email1.getText().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
					email1.getStyleClass().removeIf(style -> style.equals("text-field-valid"));
					email1.getStyleClass().add("text-field-error");
				} else {
					email1.getStyleClass().removeIf(style -> style.equals("text-field-error"));
					email1.getStyleClass().add("text-field-valid");
					if(!email1.getText().strip().equals(email2.getText().strip())) {
						email2.getStyleClass().removeIf(style -> style.equals("text-field-valid"));
						email2.getStyleClass().add("text-field-error");
					} else {
						email2.getStyleClass().removeIf(style -> style.equals("text-field-error"));
						email2.getStyleClass().add("text-field-valid");
					}				
				}
			}
		};
		
		email1.textProperty().addListener(emailListener);
		email2.textProperty().addListener(emailListener);	
	}
}
