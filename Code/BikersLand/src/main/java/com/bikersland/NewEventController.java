package com.bikersland;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

public class NewEventController {
	
	@FXML
    private TextArea txtDescription;

    @FXML
    private Label lblCharacters;

    @FXML
    private ListView<Tappa> lvTappe;
    
    public static ObservableList<Tappa> tappaObservableList;

    public NewEventController()  {

    	tappaObservableList = FXCollections.observableArrayList();

        //add some Students
    	for(int i = 0; i < 1; i++) {
			tappaObservableList.add(new Tappa());
		}
    }
    
    public void initialize() {
    	txtDescription.textProperty().addListener((obs, oldVal, newVal) -> {
    		if(newVal.length() > 10) {
    			txtDescription.setText(oldVal);
    			return;
    		}
    		
    		lblCharacters.setText(10-newVal.length() + " caratteri rimanenti");
    	});
    	
    	
    	
    	lvTappe.setItems(tappaObservableList);
    	lvTappe.setCellFactory(tappaListView -> new CustomListCellController());
    }

    @FXML
    private void uploadImage() {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Open Resource File");
    	fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.jpeg", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
            );
    	File file = fileChooser.showOpenDialog(lblCharacters.getParent().getScene().getWindow());
    	
    	System.out.println(file != null ? file.getAbsolutePath() : "Nessun file selezionato");
    	
    }
    
    @FXML
    private void createEvent() {
    	
    }

}
