package com.bikersland;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.SearchableComboBox;

import com.bikersland.db.EventDAO;
import com.bikersland.db.TagDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class NewEventController {
	
	@FXML
    private CheckComboBox<String> comboTags;

    @FXML
    private SearchableComboBox<String> comboDepartureCity;

    @FXML
    private SearchableComboBox<String> comboDestinationCity;

    @FXML
    private DatePicker dateDeparture;

    @FXML
    private DatePicker dateReturn;

    @FXML
    private TextArea txtDescription;

    @FXML
    private Label lblCharacters;

    @FXML
    private ListView<Tappa> lvTappe;
    
    @FXML
    private TextField txtTitle;
    
    private int maxDescriptionCharacters = 250;
    
    public static ObservableList<Tappa> tappaObservableList;

    public NewEventController()  {
    	tappaObservableList = FXCollections.observableArrayList();

        //add some Students
    	for(int i = 0; i < 1; i++) {
			tappaObservableList.add(new Tappa());
		}
    }
    
    public void initialize() throws SQLException {    	
    	comboDepartureCity.getItems().addAll(App.cities);
    	comboDestinationCity.getItems().addAll(App.cities);
    	comboTags.getItems().addAll(TagDAO.getTags());
    	
    	txtDescription.textProperty().addListener((obs, oldVal, newVal) -> {
    		if(newVal.length() > maxDescriptionCharacters) {
    			txtDescription.setText(oldVal);
    			return;
    		}
    		
    		lblCharacters.setText(maxDescriptionCharacters-newVal.length() + " caratteri rimanenti");
    	});
    	
    	
    	
    	lvTappe.setItems(tappaObservableList);
    	lvTappe.setCellFactory(tappaListView -> new CustomListCellController());
    }
    
    @FXML
    private void enableDateReturn() {
    	LocalDate departureDate = dateDeparture.getValue();
    	dateReturn.setValue(departureDate);
    	
    	dateReturn.setDayCellFactory(picker -> new DateCell() {
    		@Override
    		public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                setDisable(empty || date.compareTo(departureDate) < 0 );
            }
        });
    	
    	if(dateReturn.isDisable())
    		dateReturn.setDisable(false);
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
    private void createEvent() throws SQLException {
    	Event event = new Event(null, txtTitle.getText().strip(), txtDescription.getText().strip(), "Galaxy",
    			comboDepartureCity.getValue(), comboDestinationCity.getValue(), Date.valueOf(dateDeparture.getValue()),
    			Date.valueOf(dateReturn.getValue()));
    	
    	EventDAO.setEvent(event);
    	
    	Event newEvent = EventDAO.getEventByID(1);
    	
    	System.out.println(newEvent.getDescription());
    }

}
