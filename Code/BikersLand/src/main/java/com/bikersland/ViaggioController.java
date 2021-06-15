package com.bikersland;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.bikersland.db.FavoriteEventDAO;
import com.bikersland.db.PartecipationDAO;

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
    private Button btnPartecipants;
	
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
    	
    	Rectangle imgRound = new Rectangle(400, 170);
    	imgRound.setArcHeight(10);
    	imgRound.setArcWidth(10);
    	
    	imgViaggio.setClip(imgRound);
    	
    	Image image = event.getImage();
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
    	
    	Tooltip imgTooltip = new Tooltip("Click to see Event Details");
    	imgTooltip.setShowDelay(Duration.ZERO);
    	imgTooltip.setHideDelay(Duration.ZERO);
    	imgTooltip.setFont(Font.font(imgTooltip.getFont().getFamily(), FontWeight.BOLD, 13));
    	Tooltip.install(imgViaggio, imgTooltip);
    	
    	lblTitle.setText(event.getTitle());
    	lblDepartureCity.setText(event.getDeparture_city());
    	lblDestinationCity.setText(event.getDestination_city());
//    	System.out.println(String.join(", ", event.getTags()));
    	if(event.getTags().size() == 0)
    		lblTags.setText("No tags!");
    	else {
    	    lblTags.setText(String.join(", ", event.getTags()));
    	    Tooltip tp = new Tooltip(lblTags.getText());
    		tp.setShowDelay(Duration.ZERO);
    		tp.setHideDelay(Duration.ZERO);
    		tp.setFont(Font.font(tp.getFont().getFamily(), FontWeight.BOLD, 13));
    		lblTags.setTooltip(tp);
    	}
    	lblDepartureDate.setText(NonSoComeChiamarla.dateToString(event.getDeparture_date()));
    	lblReturnDate.setText(NonSoComeChiamarla.dateToString(event.getReturn_date()));
    	lblOwnerUsername.setText("Creato da: " + event.getOwner_username());
    	
    	Platform.runLater(() -> {
    		AnchorPane.setLeftAnchor(lblTitle, (400-lblTitle.getWidth())/2);
        	AnchorPane.setRightAnchor(lblTitle, (400-lblTitle.getWidth())/2);
        	
//        	btnJoin.setPrefWidth(btnJoin.getWidth()+8);
    	});
    	  
    	
		
		List<String> partecipants;
		try {
			partecipants = PartecipationDAO.getJoinedUsersByEvent(event);
			btnPartecipants.setText(String.valueOf(partecipants.size()));
			String partecipantList = "";
			for(String partecipant: partecipants)
				partecipantList += partecipant + "\n";
			
			Tooltip partecipantsTooltip;
			if(partecipants.size() == 0)			
				partecipantsTooltip = new Tooltip("Nobody joined this event!");
			else
				partecipantsTooltip = new Tooltip("Partecipants:\n\n" + partecipantList);
			partecipantsTooltip.setShowDelay(Duration.ZERO);
			partecipantsTooltip.setHideDelay(Duration.ZERO);
			partecipantsTooltip.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 13));
			btnPartecipants.setTooltip(partecipantsTooltip);
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
				setIsJoined(PartecipationDAO.isJoinedEvent(user, event));
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
    		PartecipationDAO.removeJoinedEvent(user, event);
    		
    		setIsJoined(false);
    	} else {
    		PartecipationDAO.addJoinedEvent(user, event);
    		
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
		}
		else {
			if(animation != null)
    			animation.stop();
			imgStar.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
		}
	}
	
	private void setIsJoined(Boolean isJoined) {
		this.isJoined = isJoined;
		if(isJoined) {
			btnJoin.setText("Rimuovi partecipazione");
		}
		else {
			btnJoin.setText("Partecipa");
		}
	}
}
