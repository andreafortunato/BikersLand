package com.bikersland;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class EventDetailsController {
	
	@FXML
    private BorderPane pnlMain;

    @FXML
    private ImageView imgBackground;

    @FXML
    private Label lblTitle;
    
    @FXML
    private Label lblDescription;

    @FXML
    private Label lblDepartureDate;

    @FXML
    private Label lblReturnDate;

    @FXML
    private Label lblDepartureCity;

    @FXML
    private Label lblDestinationCity;

    @FXML
    private Label lblPartecipants;

    @FXML
    private Label lblOwnerUsername;
    
    private Event event;
    
    public EventDetailsController(Event event) {
		this.event = event;
	}
	
	public void initialize() {
		
		lblTitle.setText(event.getTitle());
		lblDepartureDate.setText(NonSoComeChiamarla.dateToString(event.getDeparture_date()));
		lblReturnDate.setText(NonSoComeChiamarla.dateToString(event.getReturn_date()));
		lblDepartureCity.setText(event.getDeparture_city());
		lblDestinationCity.setText(event.getDestination_city());
		lblDescription.setText(event.getDescription());
		lblOwnerUsername.setText(event.getOwner_username());
		
		
		
		
		
		Image image = event.getImage();
		//new Image("file:resources/images/1282257.jpg");
		imgBackground.setImage(image);		
		
		if(image.getWidth() > image.getHeight()) {
	    	imgBackground.fitHeightProperty().bind(pnlMain.heightProperty());
	    	imgBackground.setFitWidth(0.0);
		} else {
	    	imgBackground.fitWidthProperty().bind(pnlMain.widthProperty());
	    	imgBackground.setFitHeight(0.0);
		}
    	imgBackground.setPreserveRatio(true);

	}
}
