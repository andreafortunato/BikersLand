package com.bikersland;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.controlsfx.control.SearchableComboBox;

import com.bikersland.db.EventDAO;
import com.bikersland.db.FavoriteEventDAO;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

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
    private ScrollPane spEventList;
    
    @FXML
    private GridPane gridViaggi;
    
    @FXML
    private ListView<String> lvTags;
    
    private int viaggioBoxWidth = 420;
    
    private List<Event> eventList;
    private List<Node> eventNodeList;
    
    private Integer gridPaneColumns = 2;
    
    
    public void initialize() {
    	imgBackground.setVisible(false);
    	pnlMain.setVisible(false);
    	spEventList.setVisible(false);
    	
    	sliderPartenzaDistanza.setMajorTickUnit(25);
    	sliderPartenzaDistanza.setMinorTickCount(2);
    	sliderPartenzaDistanza.setShowTickLabels(true);
    	sliderPartenzaDistanza.valueProperty().addListener((observable, oldValue, newValue) -> {
    		sliderPartenzaDistanza.setValue(newValue.intValue());
            lblPartenzaDistanza.setText(Integer.toString(newValue.intValue()) + " Km da");
        });
    	sliderPartenzaDistanza.setOnMousePressed(event -> imgBackground.requestFocus());
    	
    	sliderArrivoDistanza.setMajorTickUnit(25);
    	sliderArrivoDistanza.setMinorTickCount(2);
    	sliderArrivoDistanza.setShowTickLabels(true);
    	sliderArrivoDistanza.valueProperty().addListener((observable, oldValue, newValue) -> {
    		sliderArrivoDistanza.setValue(newValue.intValue());
            lblArrivoDistanza.setText(Integer.toString(newValue.intValue()) + " Km da");
        });
    	sliderArrivoDistanza.setOnMousePressed(event -> imgBackground.requestFocus());    	
    	
    	
    	ObservableList<String> cities = FXCollections.observableArrayList(App.cities);
    	cities.add(0, App.bundle.getString("all_female"));
    	
        comboPartenzaCitta.setItems(cities);
        comboPartenzaDistanza.setItems(FXCollections.observableArrayList(App.cities));
        comboArrivoCitta.setItems(cities);
        comboArrivoDistanza.setItems(FXCollections.observableArrayList(App.cities));
        
        comboPartenzaCitta.getSelectionModel().select(0);
        comboArrivoCitta.getSelectionModel().select(0);
        
        ObservableList<String> tags = FXCollections.observableArrayList(App.tags);
        lvTags.setItems(tags);
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
                
                //tags.add("Nuovo");
            });
            return cell ;
        });
        
        Platform.runLater(() -> {
        	
        	try {
        	imgBackground.fitWidthProperty().bind(App.scene.getWindow().widthProperty());
//        	imgBackground.fitHeightProperty().bind(pnlMain.getParent().getScene().getWindow().widthProperty());
        	imgBackground.setFitHeight(0.0);
//        	imgBackground.setFitWidth(0.0);
        	imgBackground.setPreserveRatio(true);
        	} catch (NullPointerException e) {
        		e.printStackTrace();
        		
        		System.out.println("1 " + pnlMain);
        		System.out.println("2 " + pnlMain.getParent());
        		System.out.println("3 " + pnlMain.getParent().getScene());
        		System.out.println("4 " + pnlMain.getParent().getScene().getWindow());
				System.exit(-1);
			}
        	
        	
        	gridPaneColumns = (((Double)App.scene.getWindow().getWidth()).intValue()-16-(getNumViaggi())*20)/420;			
        	
        	
        	try {
				this.eventList = EventDAO.getEventByCities(App.bundle.getString("all_female"), App.bundle.getString("all_female"));
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
        	
        	this.eventNodeList = NonSoComeChiamarla.eventsToNodeList(eventList);
        	NonSoComeChiamarla.populateGrid(gridViaggi, eventNodeList, gridPaneColumns);
        	
        	App.scene.getWindow().widthProperty().addListener((obs, oldVal, newVal) -> {            	
		    	int o = oldVal.intValue()-16-getNumViaggi()*20;
		    	int n = newVal.intValue()-16-getNumViaggi()*20;
		    	            	
		    	if(o/viaggioBoxWidth != n/viaggioBoxWidth) {
		    		gridPaneColumns = n/viaggioBoxWidth;
		    		try {
						NonSoComeChiamarla.populateGrid(gridViaggi, eventNodeList, gridPaneColumns);
						System.out.println("(" + o + ", " + n + ") --> (" + o/viaggioBoxWidth + ", " + n/viaggioBoxWidth + ")");
					} catch (Exception e) {
						e.printStackTrace();
					}
		    	}
            });
        	
        	imgBackground.setVisible(true);
        	pnlMain.setVisible(true);
        	spEventList.setVisible(true);
        });
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
	    	
	    	if(!radioArrivoDistanza.isSelected())
	    		btnSearch.setDisable(false);
    	}
    }
    
    @FXML
    private void enableDepartureDistance() {
    	if(radioPartenzaDistanza.isSelected()) {
        	sliderPartenzaDistanza.setDisable(false);
        	lblPartenzaDistanza.setDisable(false);
        	comboPartenzaDistanza.setDisable(false);
        	comboPartenzaCitta.setDisable(true);
        	
        	btnSearch.setDisable(true);
    	}
    }
    
    @FXML
    private void enableDestinationCitta() {
    	if(radioArrivoCitta.isSelected()) {
	    	sliderArrivoDistanza.setDisable(true);
	    	lblArrivoDistanza.setDisable(true);
	    	comboArrivoDistanza.setDisable(true);
	    	comboArrivoCitta.setDisable(false);
	    	
	    	if(!radioPartenzaDistanza.isSelected())
	    		btnSearch.setDisable(false);
    	}
    }
    
    @FXML
    private void enableDestinationDistance() {
    	if(radioArrivoDistanza.isSelected()) {
        	sliderArrivoDistanza.setDisable(false);
        	lblArrivoDistanza.setDisable(false);
        	comboArrivoDistanza.setDisable(false);
        	comboArrivoCitta.setDisable(true);
        	btnSearch.setDisable(true);
    	}
    }
    
    @FXML
    private void search() throws IOException, SQLException {
    	List<Event> eventList;
    	
    	eventList = EventDAO.getEventByCitiesAndTags(comboPartenzaCitta.getValue(), comboArrivoCitta.getValue(), lvTags.getSelectionModel().getSelectedItems());
    	
    	this.eventNodeList = NonSoComeChiamarla.eventsToNodeList(eventList);
    	NonSoComeChiamarla.populateGrid(gridViaggi, eventNodeList, gridPaneColumns);
    }

}
