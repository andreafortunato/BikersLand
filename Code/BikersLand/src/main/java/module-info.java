module com.bikersland {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.bikersland to javafx.fxml;
    exports com.bikersland;
}