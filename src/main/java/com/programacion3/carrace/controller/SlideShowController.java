package com.programacion3.carrace.controller;

import com.programacion3.carrace.App;
import com.programacion3.carrace.model.Car;
import com.programacion3.carrace.model.CarRaceModel;
import java.net.URL;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    /**
     * Called automatically after the FXML is loaded.
     */
    @FXML
    public void initialize() {
        cars = new CarRaceModel().getCars();
        currentSlide = 0;
        showSlide(currentSlide);
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