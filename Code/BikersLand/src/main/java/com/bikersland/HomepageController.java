package com.bikersland;

import java.io.IOException;
import java.util.Locale;

import org.controlsfx.control.SearchableComboBox;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
    
    @FXML
    private ListView<String> lvTags;
    
    private int viaggioBoxWidth = 420;
    
    public void initialize() {
    	imgBackground.fitWidthProperty().bind(pnlMain.widthProperty());
//    	imgBackground.fitHeightProperty().bind(pnlMain.heightProperty());
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
    	
    	sliderPartenzaDistanza.setOnMousePressed(event -> imgBackground.requestFocus());
    	
    	ObservableList<String> items = FXCollections.observableArrayList();
        
        String[] locales1 = Locale.getISOCountries();
        for (String countrylist : locales1) {        	
            Locale obj = new Locale("", countrylist);
            items.add(obj.getDisplayCountry());
        }
        
        FXCollections.sort(items);
        
        comboPartenzaCitta.setItems(items);
        
        ObservableList<String> tagsList = FXCollections.observableArrayList("Tag1", "Tag2", "Tag3");
        lvTags.setItems(tagsList);
        lvTags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvTags.getSelectionModel().getSelectedItems().addListener((Change<? extends String> selectedTags) -> {
            for(String s:selectedTags.getList())
            	System.out.println(s);
            System.out.println();
        });
        lvTags.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
                if (cell.isEmpty()) {
                    return;
                }

                int index = cell.getIndex();
                if (lvTags.getSelectionModel().getSelectedIndices().contains(index))
                	lvTags.getSelectionModel().clearSelection(index);
                else
                	lvTags.getSelectionModel().select(index);

                lvTags.requestFocus();

                e.consume();
                
                tagsList.add("Nuovo");
            });

            return cell ;
        });
        
        Platform.runLater(() -> {
        	int initCols = (((Double)pnlMain.getParent().getScene().getWindow().getWidth()).intValue()-16-(getNumViaggi())*20)/420;
        	
        	FXMLLoader fxmlLoader;
        	
        	ObservableList<StackPane> obsViaggiList = FXCollections.observableArrayList();
        	
        	for (int i=0; i<40; i++) {
        		
        		fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("cardViaggio.fxml"));
                
                StackPane viaggioBox = null;
				try {
					viaggioBox = fxmlLoader.load();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                ViaggioController vc = fxmlLoader.getController();
                vc.setTxt(i);
                
                if(viaggioBox != null)
                	obsViaggiList.add(viaggioBox);
			}
        	
        	NonSoComeChiamarla.populateGrid(gridViaggi, obsViaggiList, initCols);
        	
        	pnlMain.getParent().getScene().getWindow().widthProperty().addListener((obs, oldVal, newVal) -> {            	
		    	int o = oldVal.intValue()-16-getNumViaggi()*20;
		    	int n = newVal.intValue()-16-getNumViaggi()*20;
		    	            	
		    	if(o/viaggioBoxWidth != n/viaggioBoxWidth) {
		    		try {
						NonSoComeChiamarla.populateGrid(gridViaggi, obsViaggiList, n/viaggioBoxWidth);
						System.out.println("(" + o + ", " + n + ") --> (" + o/viaggioBoxWidth + ", " + n/viaggioBoxWidth + ")");
					} catch (Exception e) {
						e.printStackTrace();
					}
		    	}            	
            });
        	
//        	try {
//				populateGrid(initCols);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        });
        
    }
    
//    public void populateGrid(int totalCols) throws IOException {
//    	gridViaggi.getChildren().clear();
//    	System.out.println("Colonne: " + totalCols);
//    	FXMLLoader fxmlLoader;
//    	
//    	int totalElements = 40;
//    	int count = 0;
//    	
//    	int row = 0;
//    	int col = 0;
//    	
//    	while(totalElements > 0) {
//    		fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("cardViaggio.fxml"));
//            
//            StackPane viaggioBox = fxmlLoader.load();
//            ViaggioController vc = fxmlLoader.getController();
//            vc.setTxt(count++);
//            
//            gridViaggi.add(viaggioBox, col, row);
//            
//            if(++col == totalCols) {
//            	col = 0;
//            	row++;
//            }
//            totalElements--;            
//    	}
//	}
    
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
