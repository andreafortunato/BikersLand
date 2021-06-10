package com.bikersland;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bikersland.db.EventDAO;
import com.bikersland.db.FavoriteEventDAO;
import com.bikersland.db.PartecipationDAO;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    private GridPane gpJoinedEvents;

    @FXML
    private GridPane gpFavoriteEvents;
    
    private List<Event> joinedEventList;
    private List<Node> joinedEventNodeList;
    
    private List<Event> favoriteEventList;
    private List<Node> favoriteEventNodeList;
    
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
		
		
		final ContextMenu contextMenu = new ContextMenu();
	    final MenuItem item1 = new MenuItem("Change Image...");

	    contextMenu.getItems().add(item1);
		
		imgProfileImage.setOnContextMenuRequested(e -> contextMenu.show(imgProfileImage, e.getScreenX(), e.getScreenY()));
		
		try {
			joinedEventList = PartecipationDAO.getJoinedEventsByUser(user);
			favoriteEventList = FavoriteEventDAO.getFavoriteEventsByUser(user);
		} catch (SQLException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		List<Event> eventsToRemove = new ArrayList<Event>();
//		
//		for(Event event: favoriteEventList)
//			if(!(event instanceof FavoriteEvent))
//				eventsToRemove.add(event);
//		
//		for(Event event: eventsToRemove)
//			favoriteEventList.remove(event);
//		eventsToRemove.clear();
		
		this.joinedEventNodeList = NonSoComeChiamarla.eventsToNodeList(joinedEventList);
    	NonSoComeChiamarla.populateGrid(gpJoinedEvents, joinedEventNodeList, 2);
		
		this.favoriteEventNodeList = NonSoComeChiamarla.eventsToNodeList(favoriteEventList);
    	NonSoComeChiamarla.populateGrid(gpFavoriteEvents, favoriteEventNodeList, 2);
		
		Platform.runLater(() -> {
			vbViaggi.getParent().getScene().getWindow().widthProperty().addListener((obs, oldVal, newVal) -> {
	        	
	        	int o = oldVal.intValue()-16-getNumViaggi()*20;
	        	int n = newVal.intValue()-16-getNumViaggi()*20;
	        	            	
	        	if(o/viaggioBoxWidth != n/viaggioBoxWidth)
					try {
						NonSoComeChiamarla.populateGrid(gpFavoriteEvents, favoriteEventNodeList, n/viaggioBoxWidth);
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
