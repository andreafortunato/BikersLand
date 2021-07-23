package com.bikersland;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.bikersland.db.FavoriteEventDAO;
import com.bikersland.db.ParticipationDAO;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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

public class ViaggioController {
	
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

    private Event event;
    private Boolean isFavorite = false;
    private Boolean isJoined = false;
    
    private InstantTooltip favoriteTooltip = new InstantTooltip("");
    
    private User user = null;
    
    public ViaggioController(Event event) {
		this.event = event;
	}
    
    public ViaggioController(Event event, Boolean isFavorite) {
		this.event = event;
		this.setIsFavorite(isFavorite);
	}
    
    public void initialize() {
    	user = LoginSingleton.getLoginInstance().getUser();
    	
    	Tooltip.install(imgStar, favoriteTooltip);
    	
    	Rectangle imgRound = new Rectangle(400, 170);
    	imgRound.setArcHeight(10);
    	imgRound.setArcWidth(10);
    	
    	imgViaggio.setClip(imgRound);
    	
    	Image image = event.getImage();
    	if(image == null)
    		image = new Image(getClass().getResourceAsStream("img/background.jpg"));
    	
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
    	
    	InstantTooltip imgTooltip = new InstantTooltip(App.bundle.getString("see_event_details"));
    	Tooltip.install(imgViaggio, imgTooltip);
    	
    	lblTitle.setText(event.getTitle());
    	lblDepartureCity.setText(event.getDeparture_city());
    	lblDestinationCity.setText(event.getDestination_city());
//    	System.out.println(String.join(", ", event.getTags()));
    	if(event.getTags().size() == 0)
    		lblTags.setText("No tags!");
    	else {
    	    lblTags.setText(String.join(", ", event.getTags()));
    	    InstantTooltip tp = new InstantTooltip(lblTags.getText());
    		lblTags.setTooltip(tp);
    	}
    	lblDepartureDate.setText(NonSoComeChiamarla.dateToString(event.getDeparture_date()));
    	lblReturnDate.setText(NonSoComeChiamarla.dateToString(event.getReturn_date()));
    	lblOwnerUsername.setText(event.getOwner_username());
    	
    	Platform.runLater(() -> {
    		AnchorPane.setLeftAnchor(lblTitle, (400-lblTitle.getWidth())/2);
        	AnchorPane.setRightAnchor(lblTitle, (400-lblTitle.getWidth())/2);
        	
//        	btnJoin.setPrefWidth(btnJoin.getWidth()+8);
    	});
    	  
    	
		
		List<String> participants;
		try {
			participants = ParticipationDAO.getJoinedUsersByEvent(event);
			btnParticipants.setText(String.valueOf(participants.size()));
			String participantList = "";
			for(String participant: participants)
				participantList += participant + "\n";
			
			InstantTooltip participantsTooltip;
			if(participants.size() == 0)			
				participantsTooltip = new InstantTooltip(App.bundle.getString("no_participants"));
			else
				participantsTooltip = new InstantTooltip(App.bundle.getString("participants") + ":\n\n" + participantList);
			btnParticipants.setTooltip(participantsTooltip);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		if(user == null) {
			imgStar.setVisible(false);
			btnJoin.setVisible(false);
		}
		else {
			try {
				setIsFavorite(FavoriteEventDAO.isFavoriteEvent(user, event));
				setIsJoined(ParticipationDAO.isJoinedEvent(user, event));
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
    void selectStar() throws SQLException, IOException {
    	if(isFavorite) {
    		FavoriteEventDAO.removeFavoriteEvent(user, event);
    		
    		setIsFavorite(false);
    	} else {
    		FavoriteEventDAO.addFavoriteEvent(user, event);    		
            
            setIsFavorite(true);
    	}
    }
    
    @FXML
    private void goToEventDetails() throws IOException {
    	App.setRoot("EventDetails", event);
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
            
            favoriteTooltip.setText(App.bundle.getString("remove_from_favorites"));
		}
		else {
			if(animation != null)
    			animation.stop();
			imgStar.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
			
			favoriteTooltip.setText(App.bundle.getString("add_to_favorites"));
		}
	}
	
	private void setIsJoined(Boolean isJoined) {
		this.isJoined = isJoined;
		if(isJoined) {
			btnJoin.setText(App.bundle.getString("remove_participation"));
		}
		else {
			btnJoin.setText(App.bundle.getString("join"));
		}
	}
}
