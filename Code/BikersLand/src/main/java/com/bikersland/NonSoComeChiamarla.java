package com.bikersland;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class NonSoComeChiamarla {
	
	public static void populateGrid(GridPane gp, ObservableList<StackPane> obsViaggiList, int totalCols){
		gp.getChildren().clear();
		
    	System.out.println("Colonne: " + totalCols);
    	    	
    	int totalElements = obsViaggiList.size();
    	
    	int row = 0;
    	int col = 0;
    	
    	while(totalElements > 0) {
            gp.add(obsViaggiList.get(obsViaggiList.size()-totalElements), col, row);
            
            if(++col == totalCols) {
            	col = 0;
            	row++;
            }
            totalElements--;            
    	}
	}
	
}
