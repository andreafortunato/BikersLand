package com.bikersland.controller.graphics;

import java.io.IOException;
import java.sql.SQLException;

import com.bikersland.App;
import com.bikersland.NonSoComeChiamarla;
import com.bikersland.bean.EventBean;
import com.bikersland.controller.application.EventDetailsControllerApp;
import com.bikersland.db.EventTagDAO;
import com.bikersland.db.ParticipationDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.NoEventParticipantsException;
import com.bikersland.model.User;
import com.bikersland.model.Event;
import com.bikersland.singleton.LoginSingleton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class EventDetailsControllerView {
	
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
    
    private EventBean eventBean;
    
    private Boolean isJoined;
    
    private Integer loggedUserId;
    private String loggedUserUsername;
    
    public EventDetailsControllerView(EventBean eventBean) {
		this.eventBean = eventBean;
	}
	
	public void initialize() {
		if (LoginSingleton.getLoginInstance().getUser() != null) {
			loggedUserId = LoginSingleton.getLoginInstance().getUser().getId();
			loggedUserUsername = LoginSingleton.getLoginInstance().getUser().getUsername();
		} else {
			loggedUserId = -1;
		}
		
		lblTitle.setText(eventBean.getTitle());
		lblDepartureDate.setText(NonSoComeChiamarla.dateToString(eventBean.getDeparture_date()));
		lblReturnDate.setText(NonSoComeChiamarla.dateToString(eventBean.getReturn_date()));
		lblDepartureCity.setText(eventBean.getDeparture_city());
		lblDestinationCity.setText(eventBean.getDestination_city());
		lblDescription.setText(eventBean.getDescription());
		lvTags.setItems(FXCollections.observableArrayList(eventBean.getTags()));
		if(lvTags.getItems().size() == 0)
			lvTags.setItems(FXCollections.observableArrayList(App.bundle.getString("no_tags")));
		lvTags.setOnMousePressed(e -> lvTags.getSelectionModel().clearSelection());
		lblCreateTime.setText(NonSoComeChiamarla.dateToString(eventBean.getCreate_time()));
		lblOwnerUsername.setText(eventBean.getOwner_username());
		
		try {
			lvPartecipants.setItems(FXCollections.observableArrayList(EventDetailsControllerApp.getEventParticipants(eventBean.getId())));
			lblPartecipants.setText(String.valueOf(lvPartecipants.getItems().size()));
		} catch (InternalDBException idbe) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					App.bundle.getString("timedalert_internal_error"),
					App.bundle.getString("timedalert_sql_ex_header"),
					idbe.getMessage(), App.logFile);
			
			App.setRoot("Homepage");
		} catch (NoEventParticipantsException nepe) {
			lvPartecipants.setItems(FXCollections.observableArrayList(App.bundle.getString("no_participants")));
			lblPartecipants.setText("0");
		}
		
		Image image = eventBean.getImage();
		if(image == null)
			image = EventDetailsControllerApp.getDefaultEventImage();
		
		imgBackground.setImage(image);
		
		if(image.getWidth() < image.getHeight()) {
	    	imgBackground.fitHeightProperty().bind(pnlMain.heightProperty());
	    	imgBackground.setFitWidth(0.0);
		} else {
	    	imgBackground.fitWidthProperty().bind(pnlMain.widthProperty());
	    	imgBackground.setFitHeight(0.0);
		}
    	imgBackground.setPreserveRatio(true);
    	
    	if(loggedUserId != -1) {
    		btnJoin.setVisible(false);
    	} else {
			try {
				setIsJoined(EventDetailsControllerApp.userJoinedEvent(loggedUserId, eventBean.getId()));
			} catch (InternalDBException idbe) {
				NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
						App.bundle.getString("timedalert_internal_error"),
						App.bundle.getString("timedalert_sql_ex_header"),
						idbe.getMessage(), App.logFile);
				
				App.setRoot("Homepage");
			}
    	}
	}
	
	@FXML
    private void joinEvent() {
		try {
			if(isJoined) {
	    		EventDetailsControllerApp.removeUserParticipation(loggedUserId, eventBean.getId());
	    		
	    		setIsJoined(false);
	    	} else {
	    		EventDetailsControllerApp.addUserParticipation(loggedUserId, eventBean.getId());
	    		
	    		setIsJoined(true);
	    	}
		} catch (InternalDBException idbe) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					App.bundle.getString("timedalert_internal_error"),
					App.bundle.getString("timedalert_sql_ex_header"),
					idbe.getMessage(), App.logFile);
		}
    }
	
	private void setIsJoined(Boolean isJoined) {
		this.isJoined = isJoined;
		if(isJoined) {
			btnJoin.setText(App.bundle.getString("remove_participation"));
			
			if(!lvPartecipants.getItems().contains(loggedUserUsername)) {
				ObservableList<String> participants = lvPartecipants.getItems();
				if(participants.size() == 1 && participants.get(0).equals(App.bundle.getString("no_participants"))) {
					participants.clear();
				}
				participants.add(loggedUserUsername);
				FXCollections.sort(participants);
				lvPartecipants.setItems(participants);
			}
		}
		else {
			btnJoin.setText(App.bundle.getString("join"));
			lvPartecipants.getItems().remove(loggedUserUsername);
			if(lvPartecipants.getItems().size() == 0)
				lvPartecipants.setItems(FXCollections.observableArrayList(App.bundle.getString("no_participants")));
		}
	}
}
