module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires kotlin.test;
    requires kotlin.test.junit5;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires tornadofx;
    requires org.controlsfx.controls;
    requires java.sql;
    requires java.net.http;
    requires kotlinx.serialization.json;
    requires java.base;
    requires klaxon;
    requires json;

    opens com.example.client to javafx.fxml;
    exports com.example.client;
}
