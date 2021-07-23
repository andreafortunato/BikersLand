package com.bikersland;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.SearchableComboBox;

import com.bikersland.db.EventDAO;
import com.bikersland.db.EventTagDAO;
import com.bikersland.db.TagDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

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
    private TextField txtTitle;
    
    @FXML
    private Label lblImageName;
    
    @FXML
    private HBox hbImageSelected;
    
    private InstantTooltip imageTooltip = new InstantTooltip();
    
    private File imageFile = null;
    
    private int maxDescriptionCharacters = 250;
    
    public void initialize() throws SQLException {
    	comboDepartureCity.getItems().addAll(App.cities);
    	comboDestinationCity.getItems().addAll(App.cities);
    	comboTags.getItems().addAll(App.tags);
    	
    	dateDeparture.setDayCellFactory(picker -> new DateCell() {
    		@Override
    		public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                setDisable(empty || date.compareTo(LocalDateTime.now().toLocalDate()) < 0 );
            }
        });
    	
    	lblImageName.setTooltip(imageTooltip);
    	
    	txtDescription.textProperty().addListener((obs, oldVal, newVal) -> {
    		if(newVal.length() > maxDescriptionCharacters) {
    			txtDescription.setText(oldVal);
    			return;
    		}
    		
    		lblCharacters.setText(maxDescriptionCharacters-newVal.length() + " " + App.bundle.getString("remaining_characters"));
    	});
    	
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
    	
    	File choosedFile = fileChooser.showOpenDialog(lblCharacters.getParent().getScene().getWindow());
    	
    	if(choosedFile != null) {
    		if(choosedFile.length() < 4194304) {
    			this.imageFile = choosedFile;
    			
    			imageTooltip.setGraphic(new ImageView(new Image(this.imageFile.toURI().toString(), 300, 300, true, true)));
    			lblImageName.setText(this.imageFile.getName());
    			hbImageSelected.setVisible(true);
    		} else {
    			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
    					App.bundle.getString("timedalert_image_error_title"),
    					App.bundle.getString("timedalert_image_error_header"),
    					App.bundle.getString("timedalert_image_error_content"), null);
    		}
    	}    	
    }
    
    @FXML
    private void removeImage() {
    	this.imageFile = null;
    	hbImageSelected.setVisible(false);
    }
    
    @FXML
    private void createEvent() throws SQLException, IOException {
    	String emptyField = "";
    	Boolean isEmpty = false;
    	if(txtTitle.getText().strip().length() == 0) {
    		emptyField = App.bundle.getString("title");
    		isEmpty = true;
    	}
    	else if(comboDepartureCity.getValue() == null){
    		emptyField = App.bundle.getString("departure_city_2");
    		isEmpty = true;
    	}
    	else if(comboDestinationCity.getValue() == null){
    		emptyField = App.bundle.getString("destination_city_2");
    		isEmpty = true;
    	}
    	else if(dateDeparture.getValue() == null){
    		emptyField = App.bundle.getString("departure_date_2");
    		isEmpty = true;
    	}
    	else if(dateReturn.getValue() == null){
    		emptyField = App.bundle.getString("return_date_2");
    		isEmpty = true;
    	}
    	
    	if(isEmpty) {
    		NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
    				App.bundle.getString("timedalert_new_event_empty_error_title"),
    				App.bundle.getString("timedalert_new_event_empty_error_header"),
    				App.bundle.getString("timedalert_new_event_empty_error_content"), emptyField);
    		return;
    	}
    	
    	Image eventImage = this.imageFile == null ? null : new Image(this.imageFile.toURI().toString());
    	
    	Event event = new Event(txtTitle.getText().strip(), txtDescription.getText().strip(), LoginSingleton.getLoginInstance().getUser().getUsername(),
    			comboDepartureCity.getValue(), comboDestinationCity.getValue(), Date.valueOf(dateDeparture.getValue()),
    			Date.valueOf(dateReturn.getValue()), eventImage, comboTags.getItems());
    	
    	event = EventDAO.setEvent(event);
    	
    	if(event != null) {
    		if(!comboTags.getCheckModel().getCheckedItems().isEmpty()) {
    			EventTagDAO.addEventTags(event.getId(), comboTags.getCheckModel().getCheckedItems());
    			event.setTags(comboTags.getCheckModel().getCheckedItems());
    		}
	    	
    		NonSoComeChiamarla.showTimedAlert(AlertType.INFORMATION,
    				App.bundle.getString("success"),
    				App.bundle.getString("timedalert_new_event_success_header"),
    				App.bundle.getString("timedalert_new_event_success_content"), event.getTitle());
	    	App.setRoot("EventDetails", event);
    	} else {
	    	NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
	    			App.bundle.getString("error"),
	    			App.bundle.getString("timedalert_new_event_error_header"),
	    			App.bundle.getString("timedalert_new_event_error_content"), null);
    	}
    }

}
