package com.bikersland;

import java.io.IOException;

import org.controlsfx.control.SearchableComboBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;

public class CustomListCellController extends ListCell<Tappa>{
	
	@FXML
    private GridPane gridPane;
	
    @FXML
    private SearchableComboBox<String> comboCities;

    @FXML
    private Button btnAddRemove;

    @FXML
    private Label lblAddRemove;
    
    private FXMLLoader tappaLoader = null;
    
    private Tappa tappa;
    
    @Override
    protected void updateItem(Tappa city, boolean empty) {
        super.updateItem(city, empty);
        
        this.tappa = city;

        if(empty || city == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (tappaLoader == null) {
            	tappaLoader = new FXMLLoader(getClass().getResource("CustomListCell.fxml"));
            	tappaLoader.setController(this);

                try {
                	tappaLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if(city.getTappaNumber() < (Tappa.totalTappaNumber-1)) {
            	lblAddRemove.setText("-");
            } else {
            	lblAddRemove.setText("+");
            }
            
            ObservableList<String> obs = FXCollections.observableArrayList("Uno", "Due", "Tre");
            
            comboCities.setItems(obs);

            setText(null);
            setGraphic(gridPane);
        }

    }
    
    @FXML
    private void addRemoveTappa() {
    	if(lblAddRemove.getText().equals("+"))
    		NewEventController.tappaObservableList.add(new Tappa());
    	else {
    		System.out.println("Remove " + tappa.getTappaNumber());
    		NewEventController.tappaObservableList.remove(tappa);
    	}
    }
    
}
