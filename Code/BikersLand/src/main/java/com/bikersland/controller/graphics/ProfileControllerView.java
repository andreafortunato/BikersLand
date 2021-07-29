package com.bikersland.controller.graphics;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bikersland.Main;
import com.bikersland.NonSoComeChiamarla;
import com.bikersland.bean.EventBean;
import com.bikersland.bean.UserBean;
import com.bikersland.controller.application.ProfileControllerApp;
import com.bikersland.db.EventDAO;
import com.bikersland.db.FavoriteEventDAO;
import com.bikersland.db.ParticipationDAO;
import com.bikersland.exception.InternalDBException;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class ProfileControllerView {
	
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
    
    private List<EventBean> joinedEventBeanList;
    private List<Node> joinedEventNodeList;
    
    private List<EventBean> favoriteEventBeanList;
    private List<Node> favoriteEventNodeList;
    
    private Integer gridPaneColumns = 2;
    
    private UserBean userBean;
    
    private int viaggioBoxWidth = 420;
	
	public void initialize() {
		
		userBean = ProfileControllerApp.getLoggedUser();
		
		lblName.setText(userBean.getName());
		lblSurname.setText(userBean.getSurname());
		lblEmail.setText(userBean.getEmail());
		
		Platform.runLater(() -> {
			vbViaggi.prefWidthProperty().bind(vbViaggi.getParent().getScene().getWindow().widthProperty());
		});
		
		Image image = userBean.getImage();
		Circle imgCircle = new Circle(50);
		if(image == null) {
			image = new Image(Main.class.getResourceAsStream("img/profile_image.png"), 100, 100, true, true);
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
			
			favoriteEventBeanList = ProfileControllerApp.getFavoriteEventsByUser(userBean.getId());
			joinedEventBeanList = ProfileControllerApp.getJoinedEventsByUser(userBean.getId());
			
			
		} catch (InternalDBException idbe) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					Main.bundle.getString("timedalert_internal_error"),
					Main.bundle.getString("timedalert_sql_ex_header"),
					idbe.getMessage(), Main.logFile);
			
			Main.setRoot("Homepage");
		}
		
		gridPaneColumns = (((Double)Main.scene.getWindow().getWidth()).intValue()-16-(getNumViaggi())*20)/420;
		
		this.joinedEventNodeList = NonSoComeChiamarla.eventsToNodeList(joinedEventBeanList);
    	NonSoComeChiamarla.populateGrid(gpJoinedEvents, joinedEventNodeList, gridPaneColumns);
		
		this.favoriteEventNodeList = NonSoComeChiamarla.eventsToNodeList(favoriteEventBeanList);
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
