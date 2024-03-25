module org.example.focus {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.focus to javafx.fxml;
    exports org.example.focus;
}