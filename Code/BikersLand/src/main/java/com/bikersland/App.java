package com.bikersland;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bikersland.bean.EventBean;
import com.bikersland.controller.graphics.EventDetailsControllerView;
import com.bikersland.db.CityDAO;
import com.bikersland.db.DB_Connection;
import com.bikersland.db.TagDAO;
import com.bikersland.exception.InternalDBException;

/**
 * JavaFX App
 */
public class App extends Application {
	
	public static String logFile = System.getProperty("user.home") + "\\BikersLand.log";
	
	public static List<String> cities = null;
	public static List<String> tags = null;

    public static Scene scene;
    
    public static Locale locale = Locale.ENGLISH;
    public static ResourceBundle bundle;
    
    @Override
    public void start(Stage stage) throws IOException, SQLException {
    	/* File di log per gli errori. Percorso di esempio: C:\Users\NomeUtente\BikersLand.log */
    	FileHandler fh = new FileHandler(logFile, true);
    	Logger.getGlobal().addHandler(fh);
    	
    	try {
			NonSoComeChiamarla.test();
		} catch (InternalDBException e1) {
			e1.printStackTrace();
			System.out.println("\n\nCATTURATA");
		}
    	
    	System.exit(-1);
    	
    	bundle = ResourceBundle.getBundle("com.bikersland.languages.locale", locale);
    	
    	App.cities = CityDAO.getCities();
    	App.tags = TagDAO.getTags();
    	
        scene = new Scene(loadFXML("Homepage"), 1253, 810);
        stage.setScene(scene);
        
        stage.show();
        
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
        
        stage.setOnCloseRequest(event -> {
        	try {
				DB_Connection.getConnection().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });
    }

    public static void setRoot(String fxml) {
        try {
			scene.setRoot(loadFXML(fxml));
		} catch (IOException e) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					App.bundle.getString("timedalert_internal_error"),
					App.bundle.getString("timedalert_system_error_header"),
					App.bundle.getString("timedalert_system_error_content"), logFile);
			Logger.getGlobal().log(Level.SEVERE, "Catched IOException in setRoot(String) method, inside App.java", e);
			
			System.exit(-1);
		}
    }
    
    public static void setRoot(String fxml, EventBean eventBean) {
        try {
			scene.setRoot(loadFXML(fxml, eventBean));
		} catch (IOException e) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					App.bundle.getString("timedalert_internal_error"),
					App.bundle.getString("timedalert_system_error_header"),
					App.bundle.getString("timedalert_system_error_content"), logFile);
			Logger.getGlobal().log(Level.SEVERE, "Catched IOException in setRoot(String, Event) method, inside App.java", e);
			
			System.exit(-1);
		}
    }

    private static Parent loadFXML(String fxml) throws IOException {
    	bundle = ResourceBundle.getBundle("com.bikersland.languages.locale", locale);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"), bundle);
        return fxmlLoader.load();
    }
    
    private static Parent loadFXML(String fxml, EventBean eventBean) throws IOException {
    	bundle = ResourceBundle.getBundle("com.bikersland.languages.locale", locale);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"), bundle);
        fxmlLoader.setController(new EventDetailsControllerView(eventBean));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}