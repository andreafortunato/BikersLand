module com.bikersland {
    requires transitive javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires org.controlsfx.controls;
	requires javafx.base;

    opens com.bikersland to javafx.fxml;
    exports com.bikersland;
}