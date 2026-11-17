package com.programacion3.carrace.view;

import com.programacion3.carrace.model.Car;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Part of the View. Draws one car using only JavaFX shapes.
 * Each car type (Cheyenne, Tsuru, Ruta, Vocho, Humvee) has its own design.
 * All the shapes are inside one Group, so they move together.
 *
 * @author Ian
 */
public class CarShape extends Group {

    /** Total length of every car, in pixels. */
    public static final double LENGTH = 110;
    private static final double NUMBER_Y = 35;
    
    private static final double WHEEL_RADIUS = 9;
    private static final double WHEEL_Y = 42;

    /**
     * Coordinates that describe the look of one car model.
     *
     * @param body        points (x, y, x, y...) of the body polygon
     * @param roof        points of the roof polygon
     * @param windows     one {x, y, width, height} array per window
     * @param numberX     x coordinate of the car number
     * @param backWheelX  x coordinate of the back wheel center
     * @param frontWheelX x coordinate of the front wheel center
     */
    private record Design(double[] body, double[] roof, double[][] windows,
            double numberX, double backWheelX, double frontWheelX) {
    }

    private final Car car;
    private final List<Group> wheels;

    /**
     * Builds the shapes of a car using the design of its type.
     *
     * @param car the model car that this shape represents
     */
    public CarShape(Car car) {
        this.car = car;
        this.wheels = new ArrayList<>();
        Color color = Color.web(car.getColorHex());
        Design design = designFor(car.getType());

        // Body of the car (Polygon)
        Polygon body = new Polygon(design.body());
        body.setFill(color);
        body.setStroke(Color.BLACK);
        body.setStrokeWidth(1.5);

        // Roof of the car (Polygon)
        Polygon roof = new Polygon(design.roof());
        roof.setFill(color.darker());
        roof.setStroke(Color.BLACK);
        roof.setStrokeWidth(1.5);

        getChildren().addAll(body, roof);
        
        for (double[] w : design.windows()) {
            Rectangle window = new Rectangle(w[0], w[1], w[2], w[3]);
            window.setFill(Color.LIGHTSKYBLUE);
            window.setStroke(Color.BLACK);
            getChildren().add(window);
        }

        // Police stripe and lights for the patrol car
        if ("Patrulla de los Federales".equals(car.getType())) {
            addPoliceDetails();
        }

        // Car number (Text)
        Text numberText = new Text(design.numberX(), NUMBER_Y, String.valueOf(car.getNumber()));
        numberText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        numberText.setFill(Color.WHITE);
        numberText.setStroke(Color.BLACK);
        numberText.setStrokeWidth(0.6);

        getChildren().add(numberText);
    }
    
    private void addPoliceDetails() {
        Rectangle stripe = new Rectangle(2, 28, 104, 5);
        stripe.setFill(Color.WHITE);

        Rectangle redLight = new Rectangle(63, 0, 10, 4);
        redLight.setFill(Color.web("#E53935"));
        Rectangle blueLight = new Rectangle(73, 0, 10, 4);
        blueLight.setFill(Color.web("#1E88E5"));

        getChildren().addAll(stripe, redLight, blueLight);
    }

    /**
     * Returns the design of a car type. The front of every car is on the
     * right (x = LENGTH) and the wheels are always at the same height.
     *
     * @param type the car type stored in the model
     * @return the design for that type, or a sport car if the type is unknown
     */
    private static Design designFor(String type) {
        return switch (type) {
            case "Vocho" -> new Design(
                    new double[]{0, 40, 1, 32, 6, 26, 18, 22, 92, 22, 104, 27, 109, 33, 110, 40},
                    new double[]{20, 22, 26, 14, 34, 7, 44, 3, 64, 3, 74, 7, 82, 14, 88, 22},
                    new double[][]{{36, 8, 16, 12}, {56, 8, 16, 12}},
                    44, 22, 88);
            case "Ruta del Vitor" -> new Design(
                    new double[]{0, 40, 0, -4, 96, -4, 106, 8, 110, 20, 110, 40},
                    new double[]{2, -4, 4, -9, 94, -9, 96, -4},
                    new double[][]{{6, 0, 16, 12}, {26, 0, 16, 12}, {46, 0, 16, 12},
                        {66, 0, 16, 12}, {88, 0, 9, 12}},
                    40, 22, 88);
            case "Patrulla de los Federales" -> new Design(
                    new double[]{0, 20, 58, 20, 58, 22, 98, 22, 108, 27, 110, 40, 0, 40},
                    new double[]{58, 22, 60, 4, 84, 4, 94, 22},
                    new double[][]{{63, 7, 11, 12}, {76, 7, 9, 12}},
                    20, 22, 88);
            case "Tsuru Tuneado" -> new Design(
                    new double[]{0, 40, 0, 22, 1, 12, 14, 12, 12, 20, 28, 20, 82, 20, 100, 24, 110, 30, 110, 40},
                    new double[]{26, 20, 38, 5, 70, 5, 84, 20},
                    new double[][]{{40, 8, 14, 10}, {57, 8, 14, 10}},
                    44, 24, 88);
            case "Humvee de la SEDENA" -> new Design(
                    new double[]{0, 14, 100, 14, 110, 20, 110, 40, 0, 40},
                    new double[]{10, 14, 12, 6, 88, 6, 96, 14},
                    new double[][]{{20, 8, 24, 5}, {50, 8, 24, 5}},
                    44, 22, 88);
            default -> new Design(
                    new double[]{0, 22, 18, 18, 88, 18, 106, 26, 110, 40, 0, 40},
                    new double[]{24, 18, 36, 2, 74, 2, 90, 18},
                    new double[][]{{38, 5, 15, 11}, {57, 5, 16, 11}},
                    44, 25, 88);
        };
    }

    /**
    * Creates a wheel: a Circle, a small hub and a Line from the
    * center to the edge, so the rotation is visible.
    *
    * @param centerX the x coordinate of the wheel center
    * @return a Group that contains the wheel shapes
    */
    private Group createWheel(double centerX) {
        Circle tire = new Circle(centerX, WHEEL_Y, WHEEL_RADIUS, Color.web("#212121"));
        tire.setStroke(Color.GRAY);
        tire.setStrokeWidth(2);

        Circle hub = new Circle(centerX, WHEEL_Y, 3, Color.SILVER);

        Line spoke = new Line(centerX, WHEEL_Y, centerX + WHEEL_RADIUS - 1, WHEEL_Y);
        spoke.setStroke(Color.WHITE);
        spoke.setStrokeWidth(2);

        return new Group(tire, spoke, hub);
    }
    
    public Car getCar() {
        return car;
    }

    public List<Group> getWheels() {
        return wheels;
    }
}