module com.bikersland {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires org.controlsfx.controls;

    opens com.bikersland to javafx.fxml;
    exports com.bikersland;
}