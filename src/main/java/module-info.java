module com.example.onlineclassregister {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires commons.csv;

    opens com.example.onlineclassregister to javafx.fxml;
    exports com.example.onlineclassregister;
}