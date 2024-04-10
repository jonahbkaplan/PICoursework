package com.example;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    Scene scene;
    Scene scene2;

    Button button;

    @Override
    public void start(Stage stage) throws IOException {

        stage.setTitle("focus");

        // Layout 1

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(0, 10, 0, 10));

        Label subtitle = new Label("Sign in");
        grid.add(subtitle, 0, 0);

        Label nameLabel = new Label("Username: ");
        grid.add(nameLabel, 0, 1);

        Label passLabel = new Label("Password: ");
        grid.add(passLabel, 0, 2);

        TextField nameEntry = new TextField();
        nameEntry.setMaxWidth(150.0);
        nameEntry.setMaxHeight(8);
        grid.add(nameEntry, 1, 1);

        TextField passEntry = new TextField();
        passEntry.setMaxWidth(150.0);
        passEntry.setMaxHeight(8);
        grid.add(passEntry, 1, 2);

        button = new Button("Enter");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String givenUsername = nameEntry.getText().toString();
                String givenPassword = passEntry.getText().toString();

                if (checkdetails(givenUsername, givenPassword)) {
                    stage.setScene(scene2);
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Sign in details not found");
                    alert.setContentText("Please check your username and password");
                    alert.showAndWait();
                }
            }
        });

        grid.add(button, 1, 3);

        scene = new Scene(grid, 350, 200);
        scene.getStylesheets()
                .add("style1.css");
        stage.setScene(scene);

        // Layout 2

        BorderPane border = new BorderPane();
        HBox top = new HBox();
        top.setSpacing(1);
        top.setPadding(new Insets(10));

        border.setTop(top);

        Label focus = new Label("  focus");
        focus.getStyleClass().add("title-font");

        Button menubutton = new Button();
        menubutton.getStyleClass().add("menu-button");

        Label time = new Label();
        time.getStyleClass().add("time-display");

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm\ndd/MM")));
            }
        };
        timer.start();

        top.getChildren().addAll(menubutton, focus, time);

        Line line = new Line(0, 50, 2000, 50);
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(1);
        border.getChildren().add(line);

        GridPane grid2 = new GridPane();
        grid2.setHgap(20);
        grid2.setVgap(20);
        grid2.setPadding(new Insets(0, 10, 0, 10));

        border.setCenter(grid2);

        scene2 = new Scene(border, 1200, 700);
        scene2.getStylesheets().add("style1.css");

        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static boolean checkdetails(String username, String password) {
        return true;
    }
}