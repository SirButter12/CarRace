package com.programacion3.carrace.model;

/**
* Represents a single race car and stores its state.
*
* @author Ian
*/
public class Car {

    private final String type;
    private final int number;
    private final String colorName;
    private final String colorHex;
    private double x;
    private double y;
    private double speed;
    private boolean finished;

    /**
    * Creates a new car.
    *
    * @param type      the car type (e.g. "Sport", "Muscle")
    * @param number    the car number painted on the body
    * @param colorName the readable color name (e.g. "Red")
    * @param colorHex  the color in hexadecimal format (e.g. "#E53935")
    * @param x         the initial x coordinate
    * @param y         the initial y coordinate
    */
    public Car(String type, int number, String colorName, String colorHex, double x, double y) {
        this.type = type;
        this.number = number;
        this.colorName = colorName;
        this.colorHex = colorHex;
        this.x = x;
        this.y = y;
        this.speed = 0;
        this.finished = false;
    }
       
    public String getType() { return type; }
    public int getNumber() { return number; }
    public String getColorName() { return colorName; }
    public String getColorHex() { return colorHex; }
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
    public boolean isFinished() { return finished; }
    public void setFinished(boolean finished) { this.finished = finished; }

    /**
    * Returns a short readable description of the car.
    *
    * @return the description, e.g. "Car #7 (Red Sport)"
    */
    @Override
    public String toString() {
        return "Car #" + number + " (" + colorName + " " + type + ")";
    }
}
