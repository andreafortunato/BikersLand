package com.bikersland;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ViaggioController {
	
	@FXML
    private Label lblPartenza;
	
	public void setTxt(int str) {
		lblPartenza.setText(String.valueOf(str));
	}

}
