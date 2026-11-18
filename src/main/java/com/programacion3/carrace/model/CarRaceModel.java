package com.programacion3.carrace.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Model of the MVC pattern. Stores and manages all the race data:
 * the cars, their positions and speeds, and the finishing order.
 *
 * @author Ian
 */
public class CarRaceModel {

    /** X coordinate of the start line. */
    public static final double START_X = 60;
    /** X coordinate of the finish line. */
    public static final double FINISH_X = 860;
    /** Y coordinate of the first lane. */
    public static final double FIRST_LANE_Y = 139;
    /** Vertical distance between two lanes. */
    public static final double LANE_HEIGHT = 66;
    /** Minimum base speed of a car, in pixels per second. */
    public static final double MIN_SPEED = 90;
    /** Maximum base speed of a car, in pixels per second. */
    public static final double MAX_SPEED = 160;

    private final List<Car> cars;
    private final List<Car> finishOrder;
    private final Random random;

    /**
     * Creates the model with five cars, each one in its own lane.
     */
    public CarRaceModel() {
        cars = new ArrayList<>();
        finishOrder = new ArrayList<>();
        random = new Random();

        cars.add(new Car("Humvee de la SEDENA", 23, "Brown", "#C2A878", START_X, laneY(4)));
        cars.add(new Car("Patrulla de los Federales", 12, "Blue", "#1A237E", START_X, laneY(0)));
        cars.add(new Car("Ruta del Vitor", 69, "Green", "#66BB6A", START_X, laneY(1)));
        cars.add(new Car("Tsuru Tuneado", 88, "Red", "#FF0000", START_X, laneY(2)));
        cars.add(new Car("Vocho amarillo", 45, "Yellow", "#FFFF00", START_X, laneY(3)));

        randomizeSpeeds();
    }

    /**
     * Gives every car a new random speed between MIN_SPEED and MAX_SPEED.
     */
    public final void randomizeSpeeds() {
        for (Car car : cars) {
            car.setSpeed(MIN_SPEED + random.nextDouble() * (MAX_SPEED - MIN_SPEED));
        }
    }

    /**
     * Updates the x position of a car.
     *
     * @param car  the car to update
     * @param newX the new x coordinate
     */
    public void updateCarPosition(Car car, double newX) {
        car.setX(newX);
    }

    /**
     * Registers that a car crossed the finish line.
     *
     * @param car the car that finished
     * @return the finishing place of the car (1 = winner)
     */
    public int recordFinish(Car car) {
        if (!car.isFinished()) {
            car.setFinished(true);
            car.setX(FINISH_X);
            finishOrder.add(car);
        }
        return finishOrder.indexOf(car) + 1;
    }

    /**
     * Returns the winner of the race.
     *
     * @return the first car that finished, or null if no car finished yet
     */
    public Car getWinner() {
        return finishOrder.isEmpty() ? null : finishOrder.get(0);
    }

    /**
     * Checks if every car crossed the finish line.
     *
     * @return true if the race is over
     */
    public boolean isRaceOver() {
       return finishOrder.size() == cars.size();
    }

    /**
     * Calculates the y coordinate of a lane.
     *
     * @param lane the lane index (0 = first lane)
     * @return the y coordinate of the lane
     */
    private double laneY(int lane) {
        return FIRST_LANE_Y + lane * LANE_HEIGHT;
    }

    public List<Car> getCars() {
        return Collections.unmodifiableList(cars);
    }

    public List<Car> getFinishOrder() {
        return Collections.unmodifiableList(finishOrder);
    }

    /**
     * Returns the car that is currently in the lead.
     *
     * @return the car with the largest x coordinate
     */
    public Car getLeader() {
        Car leader = cars.get(0);
        for (Car car : cars) {
            if (car.getX() > leader.getX()) {
               leader = car;
            }
        }
        return leader;
    }

    /**
     * Returns the race distance in pixels.
     *
     * @return the distance between the start and the finish line
     */
    public double getRaceDistance() {
        return FINISH_X - START_X;
    }

    /**
     * Puts every car back on the start line with a new random speed.
     */
    public void reset() {
        finishOrder.clear();
        for (Car car : cars) {
            car.setX(START_X);
            car.setFinished(false);
        }
        randomizeSpeeds();
    }
}