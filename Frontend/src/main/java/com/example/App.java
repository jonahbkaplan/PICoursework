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

    Scene loginScene;
    Scene scene2;

    Scene signupScene;

    Boolean sidePanelOpen = false;

    @Override
    public void start(Stage stage) throws IOException {

        stage.setTitle("focus");

        // Layout 1

        LogInPane loginpane = new LogInPane();
        loginpane.formatPane();

        SignupPane signupPane = new SignupPane();
        signupPane.formatPane();

        loginpane.signupPageSwitch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                signupScene = new Scene(signupPane, 350, 300);
                signupScene.getStylesheets()
                        .add("style1.css");
                stage.setScene(signupScene);
            }
        });

        loginpane.submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String givenUsername = loginpane.nameEntry.getText().toString();
                String givenPassword = loginpane.passEntry.getText().toString();

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

        loginScene = new Scene(loginpane, 350, 250);
        loginScene.getStylesheets()
                .add("style1.css");
        stage.setScene(loginScene);

        // Layout 2

        // Defines the top bar

        BorderPane border = new BorderPane();
        HBox top = new HBox();
        top.setSpacing(1);
        top.setPadding(new Insets(10));
        top.getStyleClass().add("top-pane");

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

        // Menu panel

        GridPane blank = new GridPane();
        MenuPane menugrid = new MenuPane();
        menugrid.formatPane();

        menubutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!sidePanelOpen) {
                    border.setLeft(menugrid);
                } else {
                    border.setLeft(blank);
                }
                sidePanelOpen = !sidePanelOpen;
            }
        });

        // Home page

        HomePane home = new HomePane();
        home.formatPane(4.0, 3.5);

        border.setCenter(home);
        menugrid.homebutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                border.setCenter(home);
            }
        });

        // Settings

        SettingsPane settings = new SettingsPane();
        settings.formatPane();
        menugrid.settingsbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                border.setCenter(settings);
            }
        });

        // Journalling

        JournalPane journal = new JournalPane();
        journal.formatPane();

        menugrid.journalButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                border.setCenter(journal);
            }
        });

        // Calendar

        CalendarPane calendar = new CalendarPane();
        calendar.formatPane();
        menugrid.calendarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                border.setCenter(calendar);
            }
        });

        ///// Work session

        WorkSessionPane session = new WorkSessionPane();
        session.formatPane();

        menugrid.sessionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                border.setCenter(session);
            }
        });

        // Metrics window

        MetricsPane metrics = new MetricsPane();
        metrics.formatPane();

        menugrid.metricsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                border.setCenter(metrics);
            }
        });

        //////

        scene2 = new Scene(border, 1200, 700);
        scene2.getStylesheets().add("style1.css");

        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static boolean checkdetails(String username, String password) { // Use this for authention
        return true;
    }
}