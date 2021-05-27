package com.bikersland;

import java.io.IOException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ProfileController {
	
	@FXML
    private ImageView imgProfileImage;

    @FXML
    private Label lblName;

    @FXML
    private Label lblSurname;

    @FXML
    private Label lblEmail;

    @FXML
    private VBox vbViaggi;

    @FXML
    private GridPane gridViaggi;
    
    private int viaggioBoxWidth = 420;
	
	public void initialize() {
		User user = LoginSingleton.getLoginInstance().getUser();
		
		lblName.setText(user.getName());
		lblSurname.setText(user.getSurname());
		lblEmail.setText(user.getEmail());
		
		Platform.runLater(() -> {
			vbViaggi.prefWidthProperty().bind(vbViaggi.getParent().getScene().getWindow().widthProperty());
		});
		
		
		final ContextMenu contextMenu = new ContextMenu();
	    final MenuItem item1 = new MenuItem("Change Image...");

	    contextMenu.getItems().add(item1);
		
		imgProfileImage.setOnContextMenuRequested(e -> contextMenu.show(imgProfileImage, e.getScreenX(), e.getScreenY()));
		
		ObservableList<StackPane> obsViaggiList = FXCollections.observableArrayList();
		
		FXMLLoader fxmlLoader;
    	for (int i=0; i<40; i++) {
    		
    		fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("cardViaggio.fxml"));
            
            StackPane viaggioBox = null;
			try {
				viaggioBox = fxmlLoader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            ViaggioController vc = fxmlLoader.getController();
            vc.setTxt(i);
            
            if(viaggioBox != null)
            	obsViaggiList.add(viaggioBox);
		}
		
		NonSoComeChiamarla.populateGrid(gridViaggi, obsViaggiList, 2);
		
		Platform.runLater(() -> {
			vbViaggi.getParent().getScene().getWindow().widthProperty().addListener((obs, oldVal, newVal) -> {
	        	
	        	int o = oldVal.intValue()-16-getNumViaggi()*20;
	        	int n = newVal.intValue()-16-getNumViaggi()*20;
	        	            	
	        	if(o/viaggioBoxWidth != n/viaggioBoxWidth)
					try {
						NonSoComeChiamarla.populateGrid(gridViaggi, obsViaggiList, n/viaggioBoxWidth);
						System.out.println("(" + o + ", " + n + ") --> (" + o/viaggioBoxWidth + ", " + n/viaggioBoxWidth + ")");
					} catch (Exception e) {
						e.printStackTrace();
					}
	        	
	        });
		});
	}
	
	public int getNumViaggi() {
    	return gridViaggi.getColumnCount();
    }
}
