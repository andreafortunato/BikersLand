package com.bikersland;

import com.bikersland.bean.EventBean;
import com.bikersland.controller.graphics.EventCardControllerView;
import com.bikersland.exception.InternalDBException;
import com.bikersland.model.Event;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class NonSoComeChiamarla {
	
	public static List<Node> eventsToNodeList(List<EventBean> eventList) {
		FXMLLoader fxmlLoader;
    	
    	List<Node> nodeList = FXCollections.observableArrayList();
    	
    	for(EventBean eventBean: eventList) {
    		fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("EventCard.fxml"));
            fxmlLoader.setController(new EventCardControllerView(eventBean));
            fxmlLoader.setResources(Main.bundle);
            
            StackPane viaggioBox = null;
			try {
				viaggioBox = fxmlLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Questa riga sotto non serve. E' utile per utilizzare il controller qualora servisse
//            EventCardController vc = fxmlLoader.getController();
            
            if(viaggioBox != null)
            	nodeList.add(viaggioBox);
		}
    	
    	return nodeList;
	}
	
	// TODO: Meglio "ObservableList<Node> obsViaggiList" oppure "ObservableList<StackPane> obsViaggiList"???
	public static void populateGrid(GridPane gp, List<Node> nodeList, int columns){
		gp.getChildren().clear();
		
    	int totalElements = nodeList.size();
    	
    	int row = 0;
    	int col = 0;
    	
    	while(totalElements > 0) {
            gp.add(nodeList.get(nodeList.size()-totalElements), col, row);
            
            if(++col == columns) {
            	col = 0;
            	row++;
            }
            totalElements--;            
    	}
	}
	
	public static void showTimedAlert(AlertType alertType, String title, String header, String content, String contentRed) {
		int seconds = 0;
		
		TextFlow flow = new TextFlow();

		Text txt1=new Text(content);
		txt1.setStyle("-fx-font-size: 15px; -fx-text-fill: white; -fx-fill: white;");
		txt1.setWrappingWidth(100);
		txt1.setFontSmoothingType(FontSmoothingType.LCD);

		Text txt2=new Text(contentRed);
		txt2.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: red; -fx-fill: red;");

		flow.getChildren().addAll(txt1, txt2);
		
		Alert alert = new Alert(alertType);
		alert.getDialogPane().getScene().getWindow().setOnCloseRequest(e -> e.consume());
		alert.getDialogPane().getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
		alert.getDialogPane().getStyleClass().add("errorDialog");
		alert.setTitle(" " + title);
		alert.setHeaderText(header);
		flow.setPrefWidth(400);
		alert.getDialogPane().setContent(flow);
		((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setStyle("-fx-font-size: 15px;");;
		((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("(" + seconds + ") OK");
		((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setDisable(true);
		((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setFocusTraversable(false);
						
		Timeline idlestage = new Timeline( new KeyFrame( Duration.seconds(1), new EventHandler<ActionEvent>()
	    {
			int i=seconds-1;

	        @Override
	        public void handle( ActionEvent event )
	        {
	        	((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText(String.format("(%d) OK", i--));
	        }
	    } ) );
	    idlestage.setCycleCount(seconds);
	    idlestage.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("OK");
				((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setDisable(false);
				alert.getDialogPane().getScene().getWindow().setOnCloseRequest(e -> alert.hide());
			}
		});
	    idlestage.play();
		alert.showAndWait();
	}
	
	public static void addTextLimiter(Control control, int maxLength) throws Exception {
		if(control instanceof TextField) {
		    ((TextField)control).textProperty().addListener(new ChangeListener<String>() {
		        @Override
		        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
		            if (((TextField)control).getText().length() > maxLength) {
		                String s = ((TextField)control).getText().substring(0, maxLength);
		                ((TextField)control).setText(s);
		            }
		        }
		    });
		} else {
			throw new Exception("The control is not a kind-of TextField");
		}
	}
	
	public static String dateToString(Date date) {
		SimpleDateFormat dateOnly = new SimpleDateFormat("dd-MM-yyyy");
		return dateOnly.format(date);
	}

	public static void test() throws InternalDBException {
		throw new InternalDBException("Messaggio di prova", new SQLException("boh"), "metodo(String)", "File.java");
	}
}
