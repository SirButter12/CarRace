package com.programacion3.carrace;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
* Entry point of the Car Race application. Shows the slideshow first,
* then the race simulation.
*
* @author Tu Nombre
*/
public class App extends Application {

    /** Width of the window. */
    public static final double WIDTH = 1000;
    /** Height of the window. */
    public static final double HEIGHT = 700;

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("view/SlideShow"), WIDTH, HEIGHT);
        stage.setTitle("Grand Prix Car Race");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
    * Replaces the current view with another FXML view.
    *
    * @param fxml the FXML path without extension (e.g. "view/CarRace")
    * @throws IOException if the FXML file cannot be loaded
    */
    public static void setRoot(String fxml) throws IOException {
       scene.setRoot(loadFXML(fxml));
    }
  
    /**
    * Loads an FXML file located in the com/programacion3/carrace resources.
    *
    * @param fxml the FXML path without extension
    * @return the root node of the loaded view
    * @throws IOException if the FXML file cannot be loaded
    */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}