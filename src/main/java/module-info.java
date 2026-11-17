module com.programacion3.carrace {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.programacion3.carrace to javafx.fxml;
    opens com.programacion3.carrace.controller to javafx.fxml;

    exports com.programacion3.carrace;
    exports com.programacion3.carrace.model;
    exports com.programacion3.carrace.controller;
}