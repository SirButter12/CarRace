module com.programacion3.carrace {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.programacion3.carrace to javafx.fxml;
    exports com.programacion3.carrace;
    exports com.programacion3.carrace.model;
}
