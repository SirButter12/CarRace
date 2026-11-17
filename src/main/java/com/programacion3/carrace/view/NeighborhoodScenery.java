package com.programacion3.carrace.view;

import java.util.Random;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Part of the View. Draws the background of the race: a working-class
 * neighborhood of Estado de Mexico with hills full of little houses,
 * block houses with water tanks, power lines, a taco stand and a
 * Mexican flag in the background. Everything is drawn with JavaFX shapes.
 *
 * @author Tu Nombre
 */
public class NeighborhoodScenery extends Group {

    /** Width of the scenery, in pixels. */
    public static final double WIDTH = 960;
    /** Y coordinate where the sidewalk starts (bottom of the houses). */
    public static final double GROUND_Y = 120;
    /** Height of the sidewalk. */
    public static final double SIDEWALK_HEIGHT = 12;

    /**
     * Houses of the street: {x, width, height, hasWaterTank, hasRebar}.
     * The widths add up to WIDTH so the street has no gaps.
     */
    private static final double[][] HOUSES = {
        {0, 95, 56, 1, 0},
        {95, 80, 70, 0, 1},
        {175, 110, 48, 1, 0},
        {285, 85, 64, 1, 1},
        {370, 95, 44, 0, 1},
        {465, 100, 52, 1, 0},
        {565, 80, 68, 0, 1},
        {645, 120, 42, 1, 0},
        {765, 95, 60, 1, 1},
        {860, 100, 50, 0, 1}
    };

    /** Wall color of each house (same order as HOUSES). */
    private static final String[] HOUSE_COLORS = {
        "#F48FB1", "#9E9E9E", "#FFCC80", "#80CBC4", "#BDBDBD",
        "#CE93D8", "#A5D6A7", "#FFF59D", "#90CAF9", "#BCAAA4"
    };

    /** Colors of the little houses on the hills. */
    private static final String[] HILL_HOUSE_COLORS = {
        "#EF9A9A", "#FFE082", "#B3E5FC", "#C5E1A5", "#F8BBD0", "#FFFFFF", "#BCAAA4"
    };

    /**
     * Builds the whole scenery, from the back (sky) to the front (sidewalk).
     */
    public NeighborhoodScenery() {
        drawSky();
        drawHills();
        drawFlag();
        drawHouses();
        drawPowerLines();
        drawTacoStand();
        drawSidewalk();
    }

    /**
     * Draws the sky with a gradient and the sun.
     */
    private void drawSky() {
        Rectangle sky = new Rectangle(0, 0, WIDTH, GROUND_Y);
        sky.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#81D4FA")),
                new Stop(1, Color.web("#FFF3E0"))));

        Circle sun = new Circle(894, 28, 14, Color.web("#FFEE58"));

        getChildren().addAll(sky, sun);
    }

    /**
     * Draws the hills and fills them with small colored houses.
     * A fixed seed is used so the hills always look the same.
     */
    private void drawHills() {
        Polygon hills = new Polygon(
                0, 80, 110, 52, 250, 64, 390, 38, 560, 58,
                720, 30, 860, 52, 960, 40, 960, GROUND_Y, 0, GROUND_Y);
        hills.setFill(Color.web("#9CA97A"));
        getChildren().add(hills);

        Random random = new Random(7);
        for (int i = 0; i < 400; i++) {
            double x = random.nextDouble() * (WIDTH - 10);
            double y = 30 + random.nextDouble() * (GROUND_Y - 30);
            if (hills.contains(x, y)) {
                Rectangle house = new Rectangle(x, y, 6, 5);
                house.setFill(Color.web(HILL_HOUSE_COLORS[i % HILL_HOUSE_COLORS.length]));
                getChildren().add(house);
            }
        }
    }

    /**
     * Draws a Mexican flag on a tall pole behind the houses.
     */
    private void drawFlag() {
        Rectangle pole = new Rectangle(470, 6, 3, GROUND_Y - 6);
        pole.setFill(Color.web("#9E9E9E"));

        Rectangle green = new Rectangle(473, 8, 18, 28);
        green.setFill(Color.web("#006847"));
        Rectangle white = new Rectangle(491, 8, 18, 28);
        white.setFill(Color.WHITE);
        Rectangle red = new Rectangle(509, 8, 18, 28);
        red.setFill(Color.web("#CE1126"));

        // Simplified coat of arms
        Circle shield = new Circle(500, 22, 5, Color.web("#8D6E63"));

        getChildren().addAll(pole, green, white, red, shield);
    }

    /**
     * Draws the row of block houses along the street.
     */
    private void drawHouses() {
        for (int i = 0; i < HOUSES.length; i++) {
            double x = HOUSES[i][0];
            double width = HOUSES[i][1];
            double height = HOUSES[i][2];
            double top = GROUND_Y - height;
            Color wallColor = Color.web(HOUSE_COLORS[i]);

            Rectangle wall = new Rectangle(x, top, width, height);
            wall.setFill(wallColor);
            wall.setStroke(wallColor.darker());

            Rectangle door = new Rectangle(x + width * 0.15, GROUND_Y - 24, 14, 24);
            door.setFill(Color.web("#6D4C41"));

            Rectangle window = new Rectangle(x + width * 0.55, top + 12, 20, 14);
            window.setFill(Color.web("#B3E5FC"));
            window.setStroke(Color.BLACK);

            getChildren().addAll(wall, door, window);

            // Second floor window for the tall houses
            if (height > 58) {
                Rectangle upperWindow = new Rectangle(x + width * 0.15, top + 10, 18, 12);
                upperWindow.setFill(Color.web("#B3E5FC"));
                upperWindow.setStroke(Color.BLACK);
                getChildren().add(upperWindow);
            }
            if (HOUSES[i][3] == 1) {
                drawWaterTank(x + width - 28, top);
            }
            if (HOUSES[i][4] == 1) {
                drawRebar(x, top);
            }
        }
    }

    /**
     * Draws a black water tank (tinaco) on a roof.
     *
     * @param x   the x coordinate of the tank
     * @param top the y coordinate of the roof
     */
    private void drawWaterTank(double x, double top) {
        Rectangle tank = new Rectangle(x, top - 14, 16, 14);
        tank.setFill(Color.web("#212121"));
        Ellipse lid = new Ellipse(x + 8, top - 14, 8, 2);
        lid.setFill(Color.web("#424242"));
        getChildren().addAll(tank, lid);
    }

    /**
     * Draws steel rods (varillas) sticking out of an unfinished roof.
     *
     * @param x   the x coordinate of the house
     * @param top the y coordinate of the roof
     */
    private void drawRebar(double x, double top) {
        for (int offset = 8; offset <= 24; offset += 8) {
            Line rod = new Line(x + offset, top, x + offset, top - 10);
            rod.setStroke(Color.web("#6D4C41"));
            getChildren().add(rod);
        }
    }

    /**
     * Draws two wooden poles with power cables across the street.
     */
    private void drawPowerLines() {
        double[] poleX = {330, 640};
        for (double x : poleX) {
            Rectangle pole = new Rectangle(x, 24, 5, GROUND_Y - 24);
            pole.setFill(Color.web("#5D4037"));
            Rectangle crossBar = new Rectangle(x - 12, 30, 29, 3);
            crossBar.setFill(Color.web("#5D4037"));
            getChildren().addAll(pole, crossBar);
        }
        for (double y = 31; y <= 35; y += 4) {
            getChildren().addAll(
                    cable(0, y + 8, 332, y),
                    cable(332, y, 642, y),
                    cable(642, y, WIDTH, y + 10));
        }
    }

    /**
     * Creates one thin black cable.
     *
     * @param startX the start x coordinate
     * @param startY the start y coordinate
     * @param endX   the end x coordinate
     * @param endY   the end y coordinate
     * @return the cable line
     */
    private Line cable(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.web("#1E1E1E"));
        return line;
    }

    /**
     * Draws a taco stand with a striped awning.
     */
    private void drawTacoStand() {
        Rectangle stand = new Rectangle(700, 98, 50, 22);
        stand.setFill(Color.WHITE);
        stand.setStroke(Color.BLACK);
        getChildren().add(stand);

        for (int i = 0; i < 5; i++) {
            Rectangle stripe = new Rectangle(700 + i * 10, 90, 10, 8);
            stripe.setFill(i % 2 == 0 ? Color.web("#E53935") : Color.WHITE);
            getChildren().add(stripe);
        }

        Text sign = new Text(707, 113, "TACOS");
        sign.setFont(Font.font("Arial", FontWeight.BOLD, 9));
        sign.setFill(Color.web("#C62828"));
        getChildren().add(sign);
    }

    /**
     * Draws the sidewalk and the curb between the houses and the road.
     */
    private void drawSidewalk() {
        Rectangle sidewalk = new Rectangle(0, GROUND_Y, WIDTH, SIDEWALK_HEIGHT);
        sidewalk.setFill(Color.web("#BDBDBD"));

        Line curb = new Line(0, GROUND_Y + SIDEWALK_HEIGHT, WIDTH, GROUND_Y + SIDEWALK_HEIGHT);
        curb.setStroke(Color.web("#757575"));
        curb.setStrokeWidth(2);

        getChildren().addAll(sidewalk, curb);
    }
}
