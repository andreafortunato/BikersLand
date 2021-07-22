package com.bikersland;

import java.io.IOException;
import java.sql.SQLException;

import com.bikersland.db.EventTagDAO;
import com.bikersland.db.ParticipationDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

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
    private Label lblCreateTime;

    @FXML
    private Label lblOwnerUsername;
    
    @FXML
    private ListView<String> lvTags;
    
    @FXML
    private ListView<String> lvPartecipants;
    
    @FXML
    private Button btnJoin;
    
    private Event event;
    
    private Boolean isJoined;
    
    private User user;
    
    public EventDetailsController(Event event) {
		this.event = event;
	}
	
	public void initialize() throws SQLException {
		user = LoginSingleton.getLoginInstance().getUser();
		
		lblTitle.setText(event.getTitle());
		lblDepartureDate.setText(NonSoComeChiamarla.dateToString(event.getDeparture_date()));
		lblReturnDate.setText(NonSoComeChiamarla.dateToString(event.getReturn_date()));
		lblDepartureCity.setText(event.getDeparture_city());
		lblDestinationCity.setText(event.getDestination_city());
		lblDescription.setText(event.getDescription());
		lvTags.setItems(FXCollections.observableArrayList(EventTagDAO.getEventTags(event.getId())).sorted());
		lvTags.setOnMousePressed(e -> lvTags.getSelectionModel().clearSelection());
		lblCreateTime.setText(NonSoComeChiamarla.dateToString(event.getCreate_time()));
		lblOwnerUsername.setText(event.getOwner_username());
		
		lvPartecipants.setItems(FXCollections.observableArrayList(ParticipationDAO.getJoinedUsersByEvent(event)));
		lblPartecipants.setText(String.valueOf(lvPartecipants.getItems().size()));
		
		Image image = event.getImage();
		if(image == null)
			image = new Image(getClass().getResourceAsStream("img/background.jpg"));
		
		imgBackground.setImage(image);
		
		if(image.getWidth() < image.getHeight()) {
	    	imgBackground.fitHeightProperty().bind(pnlMain.heightProperty());
	    	imgBackground.setFitWidth(0.0);
		} else {
	    	imgBackground.fitWidthProperty().bind(pnlMain.widthProperty());
	    	imgBackground.setFitHeight(0.0);
		}
    	imgBackground.setPreserveRatio(true);
    	
    	if(LoginSingleton.getLoginInstance().getUser() == null) {
    		btnJoin.setVisible(false);
    	} else {
    		try {
				setIsJoined(ParticipationDAO.isJoinedEvent(user, event));
			} catch (SQLException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
	}
	
	@FXML
    private void joinEvent() throws SQLException, IOException {
    	if(isJoined) {
    		ParticipationDAO.removeJoinedEvent(user, event);
    		
    		setIsJoined(false);
    	} else {
    		ParticipationDAO.addJoinedEvent(user, event);
    		
    		setIsJoined(true);
    	}
    }
	
	private void setIsJoined(Boolean isJoined) {
		this.isJoined = isJoined;
		if(isJoined) {
			btnJoin.setText(App.bundle.getString("remove_participation"));
			
			if(!lvPartecipants.getItems().contains(user.getUsername())) {
				ObservableList<String> partecipants = lvPartecipants.getItems();
				partecipants.add(user.getUsername());
				FXCollections.sort(partecipants);
				lvPartecipants.setItems(partecipants);
			}
		}
		else {
			btnJoin.setText(App.bundle.getString("join"));
			lvPartecipants.getItems().remove(user.getUsername());
		}
	}
}
