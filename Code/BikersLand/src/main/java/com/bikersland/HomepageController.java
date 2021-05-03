package com.bikersland;

import java.io.IOException;
import java.util.Locale;

import org.controlsfx.control.SearchableComboBox;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HomepageController {
	
	@FXML
    private AnchorPane pnlHeader;

    @FXML
    private ImageView imgBackground;

    @FXML
    private StackPane pnlMain;

    @FXML
    private RadioButton radioPartenzaCitta;

    @FXML
    private ToggleGroup tgPartenza;

    @FXML
    private RadioButton radioPartenzaDistanza;

    @FXML
    private Slider sliderPartenzaDistanza;

    @FXML
    private Label lblPartenzaDistanza;

    @FXML
    private SearchableComboBox<String> comboPartenzaCitta;

    @FXML
    private SearchableComboBox<String> comboPartenzaDistanza;

    @FXML
    private RadioButton radioArrivoCitta;

    @FXML
    private ToggleGroup tgArrivo;

    @FXML
    private RadioButton radioArrivoDistanza;

    @FXML
    private Slider sliderArrivoDistanza;

    @FXML
    private Label lblArrivoDistanza;

    @FXML
    private SearchableComboBox<String> comboArrivoCitta;

    @FXML
    private SearchableComboBox<String> comboArrivoDistanza;
    
    @FXML
    private HeaderController headerController; /* IMPORTANTE: questa variabile deve avere come nome <fx:id value>Controller, dove
     											  <fx:id value> equivale in questo caso a "header", ovvero l'id del file fxml incluso */
    @FXML
    private Button btnSearch;
    
    @FXML
    private GridPane gridViaggi;
    
    
    public void initialize() {
    	System.out.println("Init");
    	    	
    	imgBackground.fitWidthProperty().bind(pnlMain.widthProperty());
//    	imgBackground.fitHeightProperty().bind(spMain.heightProperty());
    	imgBackground.setFitHeight(0.0);
//    	imgBackground.setFitWidth(0.0);
    	imgBackground.setPreserveRatio(true);
    	
//    	partenzaDistanzaSlider.setSnapToTicks(true);
    	sliderPartenzaDistanza.setMajorTickUnit(25);
    	sliderPartenzaDistanza.setMinorTickCount(2);
    	sliderPartenzaDistanza.setShowTickLabels(true);
    	
    	
    	sliderPartenzaDistanza.valueProperty().addListener((observable, oldValue, newValue) -> {
    		sliderPartenzaDistanza.setValue(newValue.intValue());
            lblPartenzaDistanza.setText(Integer.toString(newValue.intValue()) + " Km da");
        });
    	
    	sliderPartenzaDistanza.setOnMousePressed(event -> headerController.imgLogo.requestFocus());
    	
    	ObservableList<String> items = FXCollections.observableArrayList();
        
        String[] locales1 = Locale.getISOCountries();
        for (String countrylist : locales1) {        	
            Locale obj = new Locale("", countrylist);
            items.add(obj.getDisplayCountry());
        }
        
        FXCollections.sort(items);
        
        comboPartenzaCitta.setItems(items);
    }
    
    public void populateGrid(int totalCols) throws IOException {
    	gridViaggi.getChildren().clear();
    	System.out.println("Colonne: " + totalCols);
    	FXMLLoader fxmlLoader;
    	
    	int totalElements = 15;
    	int count = 0;
    	
    	int row = 0;
    	int col = 0;
    	
    	while(totalElements > 0) {
    		fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("cardViaggio.fxml"));
            
            VBox viaggioBox = fxmlLoader.load();
            ViaggioController vc = fxmlLoader.getController();
            vc.setTxt(count++);
            
            gridViaggi.add(viaggioBox, col, row);
            
            if(++col == totalCols) {
            	col = 0;
            	row++;
            }
            totalElements--;            
    	}
	}
    
    public int getNumViaggi() {
    	return gridViaggi.getColumnCount();
    }
    
    @FXML
    private void enableDepartureCitta() {    	
    	if(radioPartenzaCitta.isSelected()) {
	    	sliderPartenzaDistanza.setDisable(true);
	    	lblPartenzaDistanza.setDisable(true);
	    	comboPartenzaDistanza.setDisable(true);
	    	comboPartenzaCitta.setDisable(false);
    	}
    }
    
    @FXML
    private void enableDepartureDistance() {
    	headerController.setRegTxt();
    	if(radioPartenzaDistanza.isSelected()) {
        	sliderPartenzaDistanza.setDisable(false);
        	lblPartenzaDistanza.setDisable(false);
        	comboPartenzaDistanza.setDisable(false);
        	comboPartenzaCitta.setDisable(true);
    	}
    }
    
    @FXML
    private void enableDestinationCitta() {
    	if(radioArrivoCitta.isSelected()) {
	    	sliderArrivoDistanza.setDisable(true);
	    	lblArrivoDistanza.setDisable(true);
	    	comboArrivoDistanza.setDisable(true);
	    	comboArrivoCitta.setDisable(false);
    	}
    }
    
    @FXML
    private void enableDestinationDistance() {
    	if(radioArrivoDistanza.isSelected()) {
        	sliderArrivoDistanza.setDisable(false);
        	lblArrivoDistanza.setDisable(false);
        	comboArrivoDistanza.setDisable(false);
        	comboArrivoCitta.setDisable(true);
    	}
    }
    
    @FXML
    private void search() throws IOException {
    	System.out.println("ss");
    	App.setRoot("primary");
    }

}
