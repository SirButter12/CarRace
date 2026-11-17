package com.programacion3.carrace.controller;

import com.programacion3.carrace.App;
import com.programacion3.carrace.model.Car;
import com.programacion3.carrace.model.CarRaceModel;
import java.net.URL;
import javafx.animation.KeyFrame; 
import javafx.animation.Timeline; 
import javafx.util.Duration;
import java.util.List;
import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Controller of the slideshow view. Shows one car image at a time,
 * plays a sound and opens the race when the slideshow ends.
 *
 * @author Ian
 */
public class SlideShowController {

    @FXML
    private Label greetingLabel;
    @FXML
    private ImageView carImageView;
    @FXML
    private Label captionLabel;
    @FXML
    private ProgressBar slideProgress;

    private List<Car> cars;
    private int currentSlide;
    
    private static final Duration SLIDE_DELAY = Duration.seconds(4.0);
    
    private Timeline slideTimeline;
    
    private MediaPlayer mediaPlayer;
    /**
     * Called automatically after the FXML is loaded.
     */
    @FXML
    public void initialize() {
        cars = new CarRaceModel().getCars();
        currentSlide = 0;
        showSlide(currentSlide);
        playMusic();
        startSlideShow();
    }

    /**
     * Shows the image and caption of a car.
     *
     * @param index the index of the car to show
     */
    private void showSlide(int index) {
        Car car = cars.get(index);
        URL imageUrl = App.class.getResource("images/car" + (index + 1) + ".png");
        if (imageUrl != null) {
            carImageView.setImage(new Image(imageUrl.toExternalForm()));
        }
        captionLabel.setText(car + "  -  ready to race!");
        
        slideProgress.setProgress((index + 1) / (double) cars.size());

        FadeTransition fade = new FadeTransition(Duration.millis(600), carImageView);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
    
    /**
    * Creates a Timeline that shows the next car after each delay.
    */
    private void startSlideShow() {
        slideTimeline = new Timeline(new KeyFrame(SLIDE_DELAY, e -> nextSlide()));
        slideTimeline.setCycleCount(cars.size());
        slideTimeline.play();
    }

    /**
    * Moves to the next slide if there is one.
    */
    private void nextSlide() {
        currentSlide++;
        if (currentSlide < cars.size()) {
            showSlide(currentSlide);
        }
    }
    
    private void playMusic() {
        Random random = new Random();
        
        int i = random.nextInt(1, 4); 
        
        URL soundUrl = App.class.getResource(String.format("sounds/intro%d.wav", i));
        if (soundUrl == null) {
            System.err.printf("Sound file not found: sounds/intro%d.wav\n", i);
            return;
        }
        mediaPlayer = new MediaPlayer(new Media(soundUrl.toExternalForm()));
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.6);
        mediaPlayer.play();
    }

    /**
     * Skips the slideshow and opens the race right away.
     *
     * @param event the button event
     */
    @FXML
    private void handleSkip(ActionEvent event) {
    }
}