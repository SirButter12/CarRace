package com.programacion3.carrace.controller;

import com.programacion3.carrace.model.Car;
import com.programacion3.carrace.model.CarRaceModel;
import com.programacion3.carrace.view.CarShape;
import com.programacion3.carrace.view.NeighborhoodScenery;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Controller of the MVC pattern. Handles the buttons and the slider,
 * animates the cars, finds the winner and shows the results.
 *
 * @author Ian
 */
public class CarRaceController {

    /** Time between two random speed changes. */
    private static final Duration TICK = Duration.millis(500);

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

    private final List<TranslateTransition> moveAnimations = new ArrayList<>();
    private final List<RotateTransition> wheelAnimations = new ArrayList<>();
    private RaceState state;
    private double[] speedFactors;
    private Timeline raceTicker;
    private final Random random = new Random();

    private CarRaceModel model;
    private final List<CarShape> carShapes = new ArrayList<>();

    /**
     * Called automatically after the FXML is loaded.
     */
    @FXML
    public void initialize() {
        model = new CarRaceModel();
        speedFactors = new double[model.getCars().size()];
        // The scenery goes first (index 0) so it stays behind the road and the cars
        trackPane.getChildren().add(0, new NeighborhoodScenery());
        drawCars();
        createAnimations();
        createRaceTicker();

        speedSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            speedValueLabel.setText(String.format("x%.1f", newValue.doubleValue()));
            applyRates();
        });

        state = RaceState.READY;
        updateButtons();
        showWelcomeMessage();
    }

    /**
     * Starts the race, or resumes it if it was paused.
     *
     * @param event the button event
     */
    @FXML
    private void handleStart(ActionEvent event) {
        for (int i = 0; i < moveAnimations.size(); i++) {
            if (!carShapes.get(i).getCar().isFinished()) {
                moveAnimations.get(i).play();
                wheelAnimations.get(i * 2).play();
                wheelAnimations.get(i * 2 + 1).play();
            }
        }
        raceTicker.play();

        if (state == RaceState.PAUSED) {
            messageArea.appendText("\nRace resumed!");
        } else {
            messageArea.setText("The race has started! Go, go, go!");
        }
        state = RaceState.RUNNING;
        statusLabel.setText("Race in progress...");
        updateButtons();
    }

    /**
     * Pauses every animation of the race.
     *
     * @param event the button event
     */
    @FXML
    private void handlePause(ActionEvent event) {
        moveAnimations.forEach(Animation::pause);
        wheelAnimations.forEach(Animation::pause);
        raceTicker.pause();

        state = RaceState.PAUSED;
        statusLabel.setText("Race paused");
        messageArea.appendText("\nRace paused. Leader: " + model.getLeader());
        updateButtons();
    }

    /**
     * Stops the race and puts every car back on the start line.
     *
     * @param event the button event
     */
    @FXML
    private void handleReset(ActionEvent event) {
        moveAnimations.forEach(Animation::stop);
        wheelAnimations.forEach(Animation::stop);
        raceTicker.stop();

        for (CarShape shape : carShapes) {
            shape.setTranslateX(0);
            shape.setScaleX(1);
            shape.setScaleY(1);
            shape.getWheels().forEach(wheel -> wheel.setRotate(0));
        }
        model.reset();
        createAnimations();

        state = RaceState.READY;
        statusLabel.setText("Ready to race");
        updateButtons();
        showWelcomeMessage();
    }

    /**
     * Creates one CarShape per car of the model and places it on the start line.
     */
    private void drawCars() {
        for (Car car : model.getCars()) {
            CarShape shape = new CarShape(car);
            shape.setLayoutX(CarRaceModel.START_X - CarShape.LENGTH);
            shape.setLayoutY(car.getY());

            // Keep the model position updated while the car moves
            shape.translateXProperty().addListener((obs, oldX, newX)
                    -> model.updateCarPosition(car, CarRaceModel.START_X + newX.doubleValue()));

            carShapes.add(shape);
            trackPane.getChildren().add(shape);
        }
    }

    /**
     * Creates a TranslateTransition for each car using the speeds
     * stored in the model, and a RotateTransition for each wheel.
     */
    private void createAnimations() {
        moveAnimations.clear();
        wheelAnimations.clear();

        for (int i = 0; i < carShapes.size(); i++) {
            CarShape shape = carShapes.get(i);
            Car car = shape.getCar();
            speedFactors[i] = 1.0;

            // Car movement: from the start line to the finish line
            double seconds = model.getRaceDistance() / car.getSpeed();
            TranslateTransition move = new TranslateTransition(Duration.seconds(seconds), shape);
            move.setFromX(0);
            move.setToX(model.getRaceDistance());
            move.setInterpolator(Interpolator.LINEAR);
            move.setOnFinished(e -> carFinished(shape));
            moveAnimations.add(move);

            // Wheel rotation: the spoke line makes the rotation visible
            for (Group wheel : shape.getWheels()) {
                RotateTransition spin = new RotateTransition(Duration.millis(400), wheel);
                spin.setByAngle(360);
                spin.setCycleCount(Animation.INDEFINITE);
                spin.setInterpolator(Interpolator.LINEAR);
                wheelAnimations.add(spin);
            }
        }
        applyRates();
    }

    /**
     * Creates a Timeline that changes the speed of each car a little
     * every TICK, so the leader can change during the race.
     */
    private void createRaceTicker() {
        raceTicker = new Timeline(new KeyFrame(TICK, e -> {
            for (int i = 0; i < speedFactors.length; i++) {
                speedFactors[i] = 0.8 + random.nextDouble() * 0.4;
            }
            applyRates();
            statusLabel.setText("Leader: " + model.getLeader());
        }));
        raceTicker.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Applies the slider value and the random factor to every animation.
     */
    private void applyRates() {
        double sliderValue = speedSlider.getValue();
        for (int i = 0; i < moveAnimations.size(); i++) {
            double rate = sliderValue * speedFactors[i];
            moveAnimations.get(i).setRate(rate);
            wheelAnimations.get(i * 2).setRate(rate);
            wheelAnimations.get(i * 2 + 1).setRate(rate);
        }
    }

    /**
     * Called when a car reaches the finish line.
     *
     * @param shape the car that finished
     */
    private void carFinished(CarShape shape) {
        Car car = shape.getCar();
        int place = model.recordFinish(car);
        int index = carShapes.indexOf(shape);
        wheelAnimations.get(index * 2).stop();
        wheelAnimations.get(index * 2 + 1).stop();

        if (place == 1) {
            messageArea.appendText("\n\nCONGRATULATIONS! " + car.getColorName()
                    + " car #" + car.getNumber() + " wins the race!");
            celebrate(shape);
        }

        if (model.isRaceOver()) {
            raceTicker.stop();
            state = RaceState.FINISHED;
            statusLabel.setText("Winner: " + model.getWinner());
            updateButtons();
            showResults();
        }
    }

    /**
     * Makes the winner car grow and shrink a few times.
     *
     * @param shape the winner car
     */
    private void celebrate(CarShape shape) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(300), shape);
        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.setAutoReverse(true);
        scale.setCycleCount(6);
        scale.play();
    }

    /**
     * Shows the final results in the message area.
     */
    private void showResults() {
        StringBuilder results = new StringBuilder("\n\nFINAL RESULTS:\n");
        List<Car> order = model.getFinishOrder();
        for (int i = 0; i < order.size(); i++) {
            Car car = order.get(i);
            results.append(i + 1).append(". Car #").append(car.getNumber())
                    .append(" - ").append(car.getColorName())
                    .append(" ").append(car.getType()).append("\n");
        }
        results.append("\nAnimations used: TranslateTransition (cars), ")
                .append("RotateTransition (wheels), ScaleTransition (winner), ")
                .append("Timeline (random speed changes).");
        messageArea.appendText(results.toString());
    }

    /**
     * Shows the welcome message with the list of cars.
     */
    private void showWelcomeMessage() {
        StringBuilder text = new StringBuilder("Welcome to the Grand Cuautlitlan Izcalli Prix!\nToday's cars:\n");
        for (Car car : model.getCars()) {
            text.append("  - ").append(car).append("\n");
        }
        text.append("Press Start to begin the race.");
        messageArea.setText(text.toString());
    }

    /**
     * Enables or disables the buttons depending on the race state.
     */
    private void updateButtons() {
        startButton.setDisable(state == RaceState.RUNNING || state == RaceState.FINISHED);
        pauseButton.setDisable(state != RaceState.RUNNING);
        resetButton.setDisable(state == RaceState.READY);
        startButton.setText(state == RaceState.PAUSED ? "Resume" : "Start");
    }

    /** The possible states of the race. */
    private enum RaceState {
        READY, RUNNING, PAUSED, FINISHED
    }
}