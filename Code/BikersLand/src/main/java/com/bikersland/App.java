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
import java.sql.SQLException;
import java.util.List;

import com.bikersland.db.DB_Connection;
import com.bikersland.db.TagDAO;
import com.bikersland.db.UserDAO;

/**
 * JavaFX App
 */
public class App extends Application {
	
	public static List<String> cities = null;

    private static Scene scene;
        
     /*
      * 
      * https://www.google.com/maps/dir/tappa1/tappa2/.../tappaN/ 
      *
      *
      */
    @Override
    public void start(Stage stage) throws IOException, SQLException {
    	App.cities = CityDAO.getCities();
    	
        scene = new Scene(loadFXML("NewEvent"), 1253, 910);
        stage.setScene(scene);
        
        stage.show();
        
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());        
        
//        try {
//			DB_Connection.getConnection();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("ERRORE CRITICO");
//			System.exit(-1);
//		}
//        
//        try {
//        	User user = new User("Nome3", "Cognome3", "Username3", "Email3", "Password3");
//        	UserDAO.setUser(user);
//			System.out.println(UserDAO.getUserByUsername("Username3"));
//		} catch (SQLException ex) {
//			// TODO Auto-generated catch block
////			ex.printStackTrace();
////			System.out.println("S'È ROTTO TUTTO");
//			DB_Connection.printSQLException(ex);
//			System.exit(-1);
//		}   
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    static void setRoot(String fxml, Event event) throws IOException {
        scene.setRoot(loadFXML(fxml, event));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    private static Parent loadFXML(String fxml, Event event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        fxmlLoader.setController(new EventDetailsController(event));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}