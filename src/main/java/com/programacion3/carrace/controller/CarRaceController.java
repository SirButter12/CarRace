package com.programacion3.carrace.controller;

import com.programacion3.carrace.model.CarRaceModel;
import com.programacion3.carrace.view.NeighborhoodScenery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

/**
* Controller of the MVC pattern. Handles the buttons and the slider,
* animates the cars, finds the winner and shows the results.
*
* @author Tu Nombre
*/
public class CarRaceController {

    @FXML
    private Pane trackPane;
    @FXML
    private Button startButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button resetButton;
    @FXML
    private Slider speedSlider;
    @FXML
    private Label speedValueLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private TextArea messageArea;

    private CarRaceModel model;

    /**
    * Called automatically after the FXML is loaded.
    */
    @FXML
    public void initialize() {
        model = new CarRaceModel();
        trackPane.getChildren().add(0, new NeighborhoodScenery());
    }

    /**
    * Starts the race, or resumes it if it was paused.
    *
    * @param event the button event
    */
    @FXML
    private void handleStart(ActionEvent event) {
    }

    /**
    * Pauses every animation of the race.
    *
    * @param event the button event
    */
    @FXML
    private void handlePause(ActionEvent event) {
    }

    /**
    * Stops the race and puts every car back on the start line.
    *
    * @param event the button event
    */
    @FXML
    private void handleReset(ActionEvent event) {
    }
}