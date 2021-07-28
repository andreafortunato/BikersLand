package com.bikersland;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bikersland.db.EventDAO;
import com.bikersland.db.FavoriteEventDAO;
import com.bikersland.db.ParticipationDAO;
import com.bikersland.model.User;
import com.bikersland.model.Event;
import com.bikersland.singleton.LoginSingleton;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

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
    private GridPane gpJoinedEvents;

    @FXML
    private GridPane gpFavoriteEvents;
    
    private List<Event> joinedEventList;
    private List<Node> joinedEventNodeList;
    
    private List<Event> favoriteEventList;
    private List<Node> favoriteEventNodeList;
    
    private Integer gridPaneColumns = 2;
    
    private User user;
    
    private int viaggioBoxWidth = 420;
	
	public void initialize() {
		user = LoginSingleton.getLoginInstance().getUser();
		
		lblName.setText(user.getName());
		lblSurname.setText(user.getSurname());
		lblEmail.setText(user.getEmail());
		
		Platform.runLater(() -> {
			vbViaggi.prefWidthProperty().bind(vbViaggi.getParent().getScene().getWindow().widthProperty());
		});
		
		Image image = user.getImage();
		Circle imgCircle = new Circle(50);
		if(image == null) {
			image = new Image(getClass().getResourceAsStream("img/profile_image.png"), 100, 100, true, true);
		} else {
			double w = 0;
            double h = 0;

            double ratioX = imgProfileImage.getFitWidth() / image.getWidth();
            double ratioY = imgProfileImage.getFitHeight() / image.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = image.getWidth() * reducCoeff;
            h = image.getHeight() * reducCoeff;

            imgProfileImage.setX((imgProfileImage.getFitWidth() - w) / 2);
            imgProfileImage.setY((imgProfileImage.getFitHeight() - h) / 2);
		}
		
		imgCircle.setCenterX(50);
	    imgCircle.setCenterY(50);
	    imgProfileImage.setImage(image);
		imgProfileImage.setClip(imgCircle);
		
		try {
			joinedEventList = ParticipationDAO.getJoinedEventsByUser(user);
			favoriteEventList = FavoriteEventDAO.getFavoriteEventsByUser(user);
			for(Event event: favoriteEventList)
				System.out.println(event + "\n");
		} catch (SQLException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		gridPaneColumns = (((Double)App.scene.getWindow().getWidth()).intValue()-16-(getNumViaggi())*20)/420;
		
		this.joinedEventNodeList = NonSoComeChiamarla.eventsToNodeList(joinedEventList);
    	NonSoComeChiamarla.populateGrid(gpJoinedEvents, joinedEventNodeList, gridPaneColumns);
		
		this.favoriteEventNodeList = NonSoComeChiamarla.eventsToNodeList(favoriteEventList);
    	NonSoComeChiamarla.populateGrid(gpFavoriteEvents, favoriteEventNodeList, gridPaneColumns);
		
		Platform.runLater(() -> {
			vbViaggi.getParent().getScene().getWindow().widthProperty().addListener((obs, oldVal, newVal) -> {
	        	
	        	int o = oldVal.intValue()-16-getNumViaggi()*20;
	        	int n = newVal.intValue()-16-getNumViaggi()*20;
	        	            	
	        	if(o/viaggioBoxWidth != n/viaggioBoxWidth)
	        		gridPaneColumns = n/viaggioBoxWidth;
	        	
					try {
						NonSoComeChiamarla.populateGrid(gpFavoriteEvents, favoriteEventNodeList, gridPaneColumns);
						NonSoComeChiamarla.populateGrid(gpJoinedEvents, joinedEventNodeList, gridPaneColumns);
						System.out.println("(" + o + ", " + n + ") --> (" + o/viaggioBoxWidth + ", " + n/viaggioBoxWidth + ")");
					} catch (Exception e) {
						e.printStackTrace();
					}
	        	
	        });
		});
	}
	
	public int getNumViaggi() {
    	return gpJoinedEvents.getColumnCount();
    }
}
