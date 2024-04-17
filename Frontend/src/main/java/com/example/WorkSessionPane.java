package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class WorkSessionPane extends GridPane {

    private LocalDateTime starttime;
    private long minutes;
    private Boolean inSession = false;

    public void formatPane() {

        this.setHgap(20);
        this.setVgap(20);
        this.setPadding(new Insets(0, 10, 0, 10));

        this.add(new Label("Start Work Session"), 0, 0);

        Button startButton = new Button("start");
        this.add(startButton, 1, 0);
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (!inSession) {
                    starttime = LocalDateTime.now();
                    minutes = starttime.getLong(ChronoField.MINUTE_OF_DAY);
                    startButton.setText("stop");
                } else {
                    startButton.setText("start");
                }

                inSession = !inSession;

            }
        });

        Label displayTimer = new Label("00:00");

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (inSession) {
                    displayTimer.setText(
                            LocalDateTime.now().minus(minutes, ChronoUnit.MINUTES)
                                    .format((DateTimeFormatter.ofPattern("HH:mm"))));
                } else {
                    displayTimer.setText("00:00");
                }
            }
        };
        timer.start();
        this.add(displayTimer, 1, 1);

    }

}