package com.example;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CalendarPane extends GridPane {

    public void formatPane() {
        this.setHgap(20);
        this.setVgap(20);
        this.setPadding(new Insets(0, 10, 0, 10));

        this.add(new Label("Calendar"), 0, 0);
    }

}