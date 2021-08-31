package com.bikersland.controller.graphics;

import java.util.ArrayList;
import java.util.List;

import com.bikersland.bean.EventBean;
import com.bikersland.bean.UserBean;
import com.bikersland.controller.application.ProfileControllerApp;
import com.bikersland.exception.InternalDBException;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.utility.ConvertMethods;
import com.bikersland.utility.CustomGridPane;
import com.bikersland.utility.TimedAlert;
import com.bikersland.Main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private CustomGridPane gpJoinedEvents;

    @FXML
    private CustomGridPane gpFavoriteEvents;
    
    private int viaggioBoxWidth = 420;
	
	public void initialize() {
		List<EventBean> joinedEventBeanList;
	    List<Node> joinedEventNodeList;
	    List<EventBean> favoriteEventBeanList;
	    List<Node> favoriteEventNodeList;
	    
	    UserBean userBean = ProfileControllerApp.getLoggedUser();
		
		lblName.setText(userBean.getName());
		lblSurname.setText(userBean.getSurname());
		lblEmail.setText(userBean.getEmail());
		
		Platform.runLater(() -> 
			vbViaggi.prefWidthProperty().bind(vbViaggi.getParent().getScene().getWindow().widthProperty())
		);
		
		Image image = userBean.getImage();
		Circle imgCircle = new Circle(50);
		if(image == null) {
			image = ProfileControllerApp.getDefaultUserImage();
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
		
		List<Integer> gridPaneColumns = new ArrayList<>();
		gridPaneColumns.add((((Double)Main.getCurrentWindow().getWidth()).intValue()-16-(getNumViaggi())*20)/420);
		
		try {
			favoriteEventBeanList = ProfileControllerApp.getFavoriteEventsByUser(userBean.getId());
			joinedEventBeanList = ProfileControllerApp.getJoinedEventsByUser(userBean.getId());
			
			joinedEventNodeList = ConvertMethods.eventsToNodeList(joinedEventBeanList);
			gpJoinedEvents.populateGrid(joinedEventNodeList, gridPaneColumns.get(0));
			
			favoriteEventNodeList = ConvertMethods.eventsToNodeList(favoriteEventBeanList);
			gpFavoriteEvents.populateGrid(favoriteEventNodeList, gridPaneColumns.get(0));			
		} catch (InternalDBException idbe) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					idbe.getMessage(), Main.getLogFile());
			
			Main.setRoot("Homepage");
			return;
		}
		Platform.runLater(() -> 
			vbViaggi.getParent().getScene().getWindow().widthProperty().addListener((obs, oldVal, newVal) -> {
	        	
	        	int o = oldVal.intValue()-16-getNumViaggi()*20;
	        	int n = newVal.intValue()-16-getNumViaggi()*20;
	        	            	
	        	if(o/viaggioBoxWidth != n/viaggioBoxWidth)
	        		gridPaneColumns.set(0, n/viaggioBoxWidth);
	        	
					try {
						gpFavoriteEvents.populateGrid(favoriteEventNodeList, gridPaneColumns.get(0));
						gpJoinedEvents.populateGrid(joinedEventNodeList, gridPaneColumns.get(0));
					} catch (Exception e) {
						e.printStackTrace();
					}
	        	
	        })
		);
	}
	
	public int getNumViaggi() {
    	return gpJoinedEvents.getColumnCount();
    }
}
