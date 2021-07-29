package com.bikersland.controller.graphics;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.bikersland.Main;
import com.bikersland.InstantTooltip;
import com.bikersland.NonSoComeChiamarla;
import com.bikersland.SpriteAnimation;
import com.bikersland.bean.EventBean;
import com.bikersland.controller.application.EventDetailsControllerApp;
import com.bikersland.controller.application.EventCardControllerApp;
import com.bikersland.db.FavoriteEventDAO;
import com.bikersland.db.ParticipationDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.NoEventParticipantsException;
import com.bikersland.model.User;
import com.bikersland.model.Event;
import com.bikersland.singleton.LoginSingleton;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class EventCardControllerView {
	
	@FXML
    private StackPane spMain;
	
	@FXML
    private ImageView imgViaggio;

    @FXML
    private ImageView imgStar;

    @FXML
    private Label lblTitle;
    
    @FXML
    private HBox hbEventData;

    @FXML
    private Label lblDepartureCity;

    @FXML
    private Label lblDestinationCity;

    @FXML
    private Label lblTags;

    @FXML
    private Label lblDepartureDate;

    @FXML
    private Label lblReturnDate;

    @FXML
    private Label lblOwnerUsername;
    
    @FXML
    private Button btnJoin;
    
    @FXML
    private Button btnParticipants;
	
	private static final int COLUMNS  = 7;
	private static final int COUNT    = 7;
	private static final int OFFSET_X = 0;
	private static final int OFFSET_Y = 0;
	private static final int WIDTH    = 128;
	private static final int HEIGHT   = 128;
	private Animation animation = null;

    private EventBean eventBean;
    private Boolean isFavorite = false;
    private Boolean isJoined = false;
    
    private InstantTooltip favoriteTooltip = new InstantTooltip("");
    
    private Integer loggedUserId = null;
    
    public EventCardControllerView(EventBean eventBean) {
		this.eventBean = eventBean;
	}
    
    public EventCardControllerView(EventBean eventBean, Boolean isFavorite) {
		this.eventBean = eventBean;
		this.setIsFavorite(isFavorite);
	}
    
    public void initialize() {
    	if (LoginSingleton.getLoginInstance().getUser() != null) {
			loggedUserId = LoginSingleton.getLoginInstance().getUser().getId();
			
		} else {
			loggedUserId = -1;
		}
    	
    	Tooltip.install(imgStar, favoriteTooltip);
    	
    	Rectangle imgRound = new Rectangle(400, 170);
    	imgRound.setArcHeight(10);
    	imgRound.setArcWidth(10);
    	
    	imgViaggio.setClip(imgRound);
    	
    	Image image = eventBean.getImage();
    	if(image == null)
    		image = EventCardControllerApp.getDefaultEventImage();
    	
    	imgViaggio.setImage(image);
    	Double ratio = image.getWidth()/image.getHeight();
    	if(image.getWidth() > image.getHeight()) {
    		imgViaggio.setFitWidth(400);
    		imgViaggio.setFitHeight(0.0);    		
    		VBox.setMargin(hbEventData, new Insets(-(400/ratio - 170), 0, 0, 0));
		} else {
			imgViaggio.setFitHeight(170);
			imgViaggio.setFitWidth(0.0);
		}
    	imgViaggio.setPreserveRatio(true);
    	
    	InstantTooltip imgTooltip = new InstantTooltip(Main.bundle.getString("see_event_details"));
    	Tooltip.install(imgViaggio, imgTooltip);
    	
    	lblTitle.setText(eventBean.getTitle());
    	lblDepartureCity.setText(eventBean.getDeparture_city());
    	lblDestinationCity.setText(eventBean.getDestination_city());
//    	System.out.println(String.join(", ", event.getTags()));
    	if(eventBean.getTags().size() == 0)
    		lblTags.setText("No tags!");
    	else {
    	    lblTags.setText(String.join(", ", eventBean.getTags()));
    	    InstantTooltip tp = new InstantTooltip(lblTags.getText());
    		lblTags.setTooltip(tp);
    	}
    	lblDepartureDate.setText(NonSoComeChiamarla.dateToString(eventBean.getDeparture_date()));
    	lblReturnDate.setText(NonSoComeChiamarla.dateToString(eventBean.getReturn_date()));
    	lblOwnerUsername.setText(eventBean.getOwner_username());
    	
    	Platform.runLater(() -> {
    		AnchorPane.setLeftAnchor(lblTitle, (400-lblTitle.getWidth())/2);
        	AnchorPane.setRightAnchor(lblTitle, (400-lblTitle.getWidth())/2);
        	
//        	btnJoin.setPrefWidth(btnJoin.getWidth()+8);
    	});
    	  
    	
		
		List<String> participants;
		InstantTooltip participantsTooltip = null;
		
		try {
			
			participants = EventCardControllerApp.getEventParticipants(eventBean.getId());
			
			btnParticipants.setText(String.valueOf(participants.size()));
			String participantList = "";
			for(String participant: participants)
				participantList += participant + "\n";
			
			participantsTooltip = new InstantTooltip(Main.bundle.getString("participants") + ":\n\n" + participantList);
			
		} catch (InternalDBException idbe) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("timedalert_internal_error"),
					Main.bundle.getString("timedalert_sql_ex_header"),
					idbe.getMessage(), Main.logFile);
			
			Main.setRoot("Homepage");
		}catch(NoEventParticipantsException nepe) {
			participantsTooltip = new InstantTooltip(Main.bundle.getString("no_participants"));
			btnParticipants.setText("0");
		}
		
		btnParticipants.setTooltip(participantsTooltip);
		
		

		
		if(loggedUserId == -1) {
			imgStar.setVisible(false);
			btnJoin.setVisible(false);
		}
		else {
			try {
				setIsFavorite(EventCardControllerApp.isFavoriteEvent(loggedUserId, eventBean.getId()));
				
				setIsJoined(EventCardControllerApp.isJoinedEvent(loggedUserId, eventBean.getId()));
			} catch (InternalDBException idbe) {
				NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
						Main.bundle.getString("timedalert_internal_error"),
						Main.bundle.getString("timedalert_sql_ex_header"),
						idbe.getMessage(), Main.logFile);
				
				Main.setRoot("Homepage");
			}
		}
    }
    
    @FXML
    void focusStar() {
//    	imgStar.setImage(new Image(getClass().getResource("img/star2.png").toString()));
//    	imgStar.setViewport(new Rectangle2D(175, 0, WIDTH, HEIGHT));
    }
    
    @FXML
    void unfocusStar() {
//    	if(animation != null)
//    		animation.stop();
//    	imgStar.setImage(new Image(getClass().getResource("img/star2.png").toString()));
//    	imgStar.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
    }

    @FXML
    void selectStar(){
    	
    	try {
    		if(isFavorite) {
        		EventCardControllerApp.removeFavoriteEvent(loggedUserId, eventBean.getId());
        		
        		setIsFavorite(false);
        	} else {  
        		EventCardControllerApp.addFavoriteEvent(loggedUserId, eventBean.getId()); 
                
                setIsFavorite(true);
        	}
    	}catch(InternalDBException idbe) {
    		NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("timedalert_internal_error"),
					Main.bundle.getString("timedalert_sql_ex_header"),
					idbe.getMessage(), Main.logFile);
			
			Main.setRoot("Homepage");
    	}
    	
    }
    
    @FXML
    private void goToEventDetails(){
    	Main.setRoot("EventDetails", eventBean);
    }
    
    @FXML
    private void joinEvent() throws SQLException, IOException {
    	try {
			if(isJoined) 
	    		EventCardControllerApp.removeUserParticipation(loggedUserId, eventBean.getId());
	    		
	    	else 
	    		EventCardControllerApp.addUserParticipation(loggedUserId, eventBean.getId());
	    		
	    
			
			setParticipantsText();
			setIsJoined(!isJoined);
		} catch (InternalDBException idbe) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("timedalert_internal_error"),
					Main.bundle.getString("timedalert_sql_ex_header"),
					idbe.getMessage(), Main.logFile);
		}
    }

	public void setIsFavorite(Boolean isFavorite) {
		this.isFavorite = isFavorite;
		if(isFavorite) {
			animation = new SpriteAnimation(
                    imgStar,
                    Duration.millis(200.0),
                    COUNT, COLUMNS,
                    OFFSET_X, OFFSET_Y,
                    WIDTH, HEIGHT
            );

            animation.setCycleCount(1);
            animation.play();
            
            favoriteTooltip.setText(Main.bundle.getString("remove_from_favorites"));
		}
		else {
			if(animation != null)
    			animation.stop();
			imgStar.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
			
			favoriteTooltip.setText(Main.bundle.getString("add_to_favorites"));
		}
	}
	
	private void setIsJoined(Boolean isJoined) {
		this.isJoined = isJoined;
		if(isJoined) {
			btnJoin.setText(Main.bundle.getString("remove_participation"));
		}
		else {
			btnJoin.setText(Main.bundle.getString("join"));
		}
	}
	
	private void setParticipantsText() {
		List<String> participants;
		InstantTooltip participantsTooltip = null;
		
		try {
			
			participants = EventCardControllerApp.getEventParticipants(eventBean.getId());
			
			btnParticipants.setText(String.valueOf(participants.size()));
			String participantList = "";
			for(String participant: participants)
				participantList += participant + "\n";
			
			participantsTooltip = new InstantTooltip(Main.bundle.getString("participants") + ":\n\n" + participantList);
			
		} catch (InternalDBException idbe) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("timedalert_internal_error"),
					Main.bundle.getString("timedalert_sql_ex_header"),
					idbe.getMessage(), Main.logFile);
			
			Main.setRoot("Homepage");
		}catch(NoEventParticipantsException nepe) {
			participantsTooltip = new InstantTooltip(Main.bundle.getString("no_participants"));
			btnParticipants.setText("0");
		}
		
		btnParticipants.setTooltip(participantsTooltip);
	}
}
