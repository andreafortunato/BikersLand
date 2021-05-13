package com.bikersland;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    
    private int viaggioBoxWidth;
    
    private static HomepageController homepageController;
     /*
      * 
      * https://www.google.com/maps/dir/tappa1/tappa2/.../tappaN/ 
      *
      *
      */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Profile"), 1253, 910);
        stage.setScene(scene);
        
        stage.show();
        
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
                        
        viaggioBoxWidth = 420;
        
//        Platform.runLater(() -> {            
//            stage.widthProperty().addListener((obs, oldVal, newVal) -> {
//            	
//            	int o = oldVal.intValue()-16-(homepageController.getNumViaggi())*20;
//            	int n = newVal.intValue()-16-(homepageController.getNumViaggi())*20;
//            	            	
//            	if(o/viaggioBoxWidth != n/viaggioBoxWidth)
//					try {
//						homepageController.populateGrid(n/viaggioBoxWidth);
//						System.out.println("(" + o + ", " + n + ") --> (" + o/viaggioBoxWidth + ", " + n/viaggioBoxWidth + ")");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//            	
//            });
//        });        
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent p = fxmlLoader.load();
        if(fxml.equals("Homepage"))
        	homepageController = fxmlLoader.getController();
        return p;
    }

    public static void main(String[] args) {
        launch();
    }

}