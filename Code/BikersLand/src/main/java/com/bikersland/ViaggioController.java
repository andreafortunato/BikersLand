package com.bikersland;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ViaggioController {
	
	private static final int COLUMNS  = 7;
	private static final int COUNT    = 7;
	private static final int OFFSET_X = 0;
	private static final int OFFSET_Y = 0;
	private static final int WIDTH    = 128;
	private static final int HEIGHT   = 128;
	private Animation animation = null;
	
	@FXML
    private ImageView imgViaggio;

    @FXML
    private ImageView imgStar;

    @FXML
    private Label lblPartenza;
    
    public void initialize() {
    	Rectangle imgRound = new Rectangle(400, 170);
    	imgRound.setArcHeight(10);
    	imgRound.setArcWidth(10);
    	
    	imgViaggio.setClip(imgRound);
    }
    
    @FXML
    void focusStar() {
//    	imgStar.setImage(new Image(getClass().getResource("img/star2.png").toString()));
//    	imgStar.setViewport(new Rectangle2D(175, 0, WIDTH, HEIGHT));
    }
    
    @FXML
    void unfocusStar() {
//    	if(animation != null)
//    		animation.stop();
//    	imgStar.setImage(new Image(getClass().getResource("img/star2.png").toString()));
//    	imgStar.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
    }

    @FXML
    void selectStar() {
//    	imgStar.setImage(new Image(getClass().getResource("img/star_sprite.png").toString()));
    	
    	animation = new SpriteAnimation(
                imgStar,
                Duration.millis(200.0),
                COUNT, COLUMNS,
                OFFSET_X, OFFSET_Y,
                WIDTH, HEIGHT
        );

        animation.setCycleCount(1);
        animation.play();
    }
	
	public void setTxt(int str) {
		lblPartenza.setText(String.valueOf(str));
	}

}
