package com.bikersland;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.bikersland.db.EventDAO;
import com.bikersland.db.FavoriteEventDAO;
import com.bikersland.db.TagDAO;

/**
 * JavaFX App
 */
public class App extends Application {
	
	public static List<String> cities = null;
	public static List<String> tags = null;

    public static Scene scene;
    
    public static Locale locale = Locale.ITALIAN;
    public static ResourceBundle bundle;
    
    @Override
    public void start(Stage stage) throws IOException, SQLException, InterruptedException {
    	bundle = ResourceBundle.getBundle("com.bikersland.languages.locale", locale);
    	
    	App.cities = CityDAO.getCities();
    	App.tags = TagDAO.getTags();
    	
        scene = new Scene(loadFXML("Homepage"), 1253, 810);
        stage.setScene(scene);
        
        stage.show();
        
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    static void setRoot(String fxml, Event event) throws IOException {
        scene.setRoot(loadFXML(fxml, event));
    }

    private static Parent loadFXML(String fxml) throws IOException {
    	bundle = ResourceBundle.getBundle("com.bikersland.languages.locale", locale);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"), bundle);
        return fxmlLoader.load();
    }
    
    private static Parent loadFXML(String fxml, Event event) throws IOException {
    	bundle = ResourceBundle.getBundle("com.bikersland.languages.locale", locale);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"), bundle);
        fxmlLoader.setController(new EventDetailsController(event));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}