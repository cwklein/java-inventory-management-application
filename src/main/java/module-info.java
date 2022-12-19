module klein.firstscreen {
    requires javafx.controls;
    requires javafx.fxml;


    exports klein.firstscreen.controller;
    opens klein.firstscreen.controller to javafx.fxml;
    exports klein.firstscreen.main;
    opens klein.firstscreen.main to javafx.fxml;
}