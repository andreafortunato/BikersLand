package com.bikersland;

import org.controlsfx.control.SearchableComboBox;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

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
    private SearchableComboBox<?> comboPartenzaCitta;

    @FXML
    private SearchableComboBox<?> comboPartenzaDistanza;

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
    private SearchableComboBox<?> comboArrivoCitta;

    @FXML
    private SearchableComboBox<?> comboArrivoDistanza;
    
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
    	
    	sliderPartenzaDistanza.setOnMousePressed(event -> pnlHeader.requestFocus());
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
        	sliderPartenzaDistanza.setDisable(false);
        	lblArrivoDistanza.setDisable(false);
        	comboArrivoDistanza.setDisable(false);
        	comboArrivoCitta.setDisable(true);
    	}
    }

}
