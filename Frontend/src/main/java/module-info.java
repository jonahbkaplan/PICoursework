
module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires morphia.core;
    requires org.mongodb.bson;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;

    opens com.example to javafx.fxml;
    opens com.example.morphia.entities to morphia.core;
    exports com.example;
    exports com.example.morphia.entities;
}


