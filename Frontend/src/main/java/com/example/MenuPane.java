package com.example;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class MenuPane extends GridPane {

    public Button settingsbutton = new Button();
    public Button homebutton = new Button();
    public Button journalButton = new Button();
    public Button calendarButton = new Button();
    public Button sessionButton = new Button();
    public Button metricsButton = new Button();

    public void formatPane() {

        this.getStyleClass()
                .add("menu-grid");

        // Button settingsbutton = new Button();
        settingsbutton.getStyleClass().add("settings-button");
        this.add(settingsbutton, 0, 8);

        homebutton.getStyleClass().add("home-button");
        this.add(homebutton, 0, 1);

        // Button journalButton = new Button();
        journalButton.getStyleClass().add("journal-button");
        this.add(journalButton, 0, 2);

        calendarButton.getStyleClass().add("calendar-button");
        this.add(calendarButton, 0, 3);

        sessionButton.getStyleClass().add("work-button");
        this.add(sessionButton, 0, 4);

        metricsButton.getStyleClass().add("metrics-button");
        this.add(metricsButton, 0, 5);
    }
}
