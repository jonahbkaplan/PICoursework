module org.example.focus {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.focus to javafx.fxml;
    exports org.example.focus;
}