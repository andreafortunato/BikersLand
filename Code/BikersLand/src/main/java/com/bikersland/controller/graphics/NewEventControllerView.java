package com.bikersland.controller.graphics;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.SearchableComboBox;

import com.bikersland.App;
import com.bikersland.InstantTooltip;
import com.bikersland.NonSoComeChiamarla;
import com.bikersland.bean.EventBean;
import com.bikersland.controller.application.NewEventControllerApp;
import com.bikersland.db.EventDAO;
import com.bikersland.db.EventTagDAO;
import com.bikersland.exception.ImageFileException;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.event.TitleException;
import com.bikersland.singleton.LoginSingleton;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

public class NewEventControllerView {
	
	@FXML
    private CheckComboBox<String> comboTags;

    @FXML
    private SearchableComboBox<String> comboDepartureCity;

    @FXML
    private SearchableComboBox<String> comboDestinationCity;

    @FXML
    private DatePicker pickerDepartureDate;

    @FXML
    private DatePicker pickerReturnDate;

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
    
    private InstantTooltip previewEventImageTooltip = new InstantTooltip();
    
    private File imageFile = null;
    
    private EventBean eventBean = new EventBean();
    
    private static final int maxDescriptionCharacters = 250;
    
    public void initialize() {
    	comboDepartureCity.getItems().addAll(App.cities);
    	comboDestinationCity.getItems().addAll(App.cities);
    	comboTags.getItems().addAll(App.tags);
    	
    	/* Disabilito le date precedenti a quella odierna */
    	pickerDepartureDate.setDayCellFactory(picker -> new DateCell() {
    		@Override
    		public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                setDisable(empty || date.compareTo(LocalDateTime.now().toLocalDate()) < 0 );
            }
        });
    	
    	lblImageName.setTooltip(previewEventImageTooltip);
    	
    	txtDescription.textProperty().addListener((obs, oldVal, newVal) -> {
    		if(newVal.length() > maxDescriptionCharacters) {
    			txtDescription.setText(oldVal);
    			return;
    		}
    		
    		lblCharacters.setText(maxDescriptionCharacters-newVal.length() + " " + App.bundle.getString("remaining_characters"));
    	});
    }
    
    @FXML
    private void enablePickerReturnDate() {
    	LocalDate departureDate = pickerDepartureDate.getValue();
    	pickerReturnDate.setValue(departureDate);
    	
    	pickerReturnDate.setDayCellFactory(picker -> new DateCell() {
    		@Override
    		public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                setDisable(empty || date.compareTo(departureDate) < 0 );
            }
        });
    	
    	if(pickerReturnDate.isDisable())
    		pickerReturnDate.setDisable(false);
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
	    	try {
				eventBean.setImage(choosedFile);
				
				this.imageFile = choosedFile;
				
				previewEventImageTooltip.setGraphic(new ImageView(new Image(this.imageFile.toURI().toString(), 300, 300, true, true)));
    			lblImageName.setText(this.imageFile.getName());
    			hbImageSelected.setVisible(true);
			} catch (ImageFileException e) {
				NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
						App.bundle.getString("timedalert_image_error_title"),
						App.bundle.getString("timedalert_image_error_header"),
						e.getMessage(), null);
			}
    	} else {
    		Image nullImage = null;
    		eventBean.setImage(nullImage);
    	}
    }
    
    @FXML
    private void removeImage() {
    	this.imageFile = null;
    	hbImageSelected.setVisible(false);
    	
    	Image nullImage = null;
		eventBean.setImage(nullImage);
    }
    
    
    @FXML
    private void createEvent() {    	
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
    	else if(pickerDepartureDate.getValue() == null){
    		emptyField = App.bundle.getString("departure_date_2");
    		isEmpty = true;
    	}
    	else if(pickerReturnDate.getValue() == null){
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
    	
    	try {
    		eventBean.setTitle(txtTitle.getText().strip());
        	eventBean.setDescription(txtDescription.getText());
        	eventBean.setOwner_username(LoginSingleton.getLoginInstance().getUser().getUsername());
        	eventBean.setDeparture_city(comboDepartureCity.getValue());
        	eventBean.setDestination_city(comboDestinationCity.getValue());
        	eventBean.setDeparture_date(Date.valueOf(pickerDepartureDate.getValue()));
        	eventBean.setReturn_date(Date.valueOf(pickerReturnDate.getValue()));
        	eventBean.setTags(comboTags.getCheckModel().getCheckedItems());
        	
        	EventBean createdEventBean = NewEventControllerApp.createNewEvent(eventBean);
        	
    		NonSoComeChiamarla.showTimedAlert(AlertType.INFORMATION,
    				App.bundle.getString("success"),
					App.bundle.getString("timedalert_new_event_success_header"),
					App.bundle.getString("timedalert_new_event_success_content"), createdEventBean.getTitle());
    		
    		App.setRoot("EventDetails", createdEventBean);
		} catch (TitleException te) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					App.bundle.getString("error"),
					App.bundle.getString("timedalert_event_title_error"),
					te.getMessage(), null);
		} catch (InternalDBException idbe) {
			NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
					App.bundle.getString("timedalert_internal_error"),
					App.bundle.getString("timedalert_sql_ex_header"),
					idbe.getMessage(), App.logFile);
		}
    	
//    	event = EventDAO.setEvent(event);
//    	
//    	if(event != null) {
//    		if(!comboTags.getCheckModel().getCheckedItems().isEmpty()) {
//    			EventTagDAO.addEventTags(event.getId(), comboTags.getCheckModel().getCheckedItems());
//    			event.setTags(comboTags.getCheckModel().getCheckedItems());
//    		}
//	    	
//    		NonSoComeChiamarla.showTimedAlert(AlertType.INFORMATION,
//    				App.bundle.getString("success"),
//    				App.bundle.getString("timedalert_new_event_success_header"),
//    				App.bundle.getString("timedalert_new_event_success_content"), event.getTitle());
//	    	App.setRoot("EventDetails", event);
//    	} else {
//	    	NonSoComeChiamarla.showTimedAlert(AlertType.ERROR,
//	    			App.bundle.getString("error"),
//	    			App.bundle.getString("timedalert_new_event_error_header"),
//	    			App.bundle.getString("timedalert_new_event_error_content"), null);
//    	}
    }

}
