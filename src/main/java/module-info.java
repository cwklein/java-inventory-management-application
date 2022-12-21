module klein.firstscreen {
    requires javafx.controls;
    requires javafx.fxml;


    exports klein.c482_project.controller;
    opens klein.c482_project.controller to javafx.fxml;
    exports klein.c482_project.model;
    opens klein.c482_project.model to javafx.fxml;
}