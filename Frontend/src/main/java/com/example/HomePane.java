package com.example;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class HomePane extends GridPane {

    // probably a better way to take data from the main class than passing params:
    public void formatPane(double workTarget, double workDone) {

        this.getChildren().clear();

        this.setHgap(20);
        this.setVgap(20);
        this.setPadding(new Insets(0, 10, 0, 10));

        this.add(new Label("Welcome"), 0, 0);

        Label workLabel = new Label("Work progress:");
        workLabel.getStyleClass().add("work-label");
        this.add(workLabel, 1, 0);

        Arc progressArc = new Arc();

        progressArc.setCenterX(100);
        progressArc.setCenterY(100);

        progressArc.setRadiusX(100.0);
        progressArc.setRadiusY(100.0);
        progressArc.setStartAngle(90.0);
        progressArc.setLength((workDone / workTarget) * 360.0);

        progressArc.setFill(Color.WHITE);
        progressArc.setType(ArcType.ROUND);

        Circle blackcirc = new Circle(100.0, 100.0, 70.0, Color.WHITE);

        Shape progress = Shape.subtract(progressArc, blackcirc);
        progress.setFill(Color.WHITE);

        VBox stats = new VBox(1);
        stats.getStyleClass().add("progress-arc");

        Label progText = new Label(
                Math.round(100 * Math.min(workDone / workTarget, 1)) + "%");
        progText.getStyleClass().add("progress-percent");

        Label completed = new Label(
                Math.round(10 * workDone) * 0.1 + "/" + Math.round(10 * workTarget) * 0.1 + " hours");
        completed.getStyleClass().add("progress-small");

        stats.getChildren().addAll(progText, completed);

        this.add(progress, 1, 1);
        this.add(stats, 1, 1);
    }

}
