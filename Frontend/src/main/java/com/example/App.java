package com.example;

import java.io.IOException;

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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    String username = "Developer";
    String password = "12345678";

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
        subtitle.setTextFill(Color.WHITE);
        grid.add(subtitle, 0, 0);

        Label nameLabel = new Label("Username: ");
        nameLabel.setTextFill(Color.WHITE);
        grid.add(nameLabel, 0, 1);

        Label passLabel = new Label("Password: ");
        passLabel.setTextFill(Color.WHITE);
        grid.add(passLabel, 0, 2);

        TextField nameEntry = new TextField();
        nameEntry.setMaxWidth(150.0);
        nameEntry.setMaxHeight(8);
        nameEntry.setStyle("-fx-background-color:darkgrey;");
        grid.add(nameEntry, 1, 1);

        TextField passEntry = new TextField();
        passEntry.setMaxWidth(150.0);
        passEntry.setMaxHeight(8);
        passEntry.setStyle("-fx-background-color:darkgrey;");
        grid.add(passEntry, 1, 2);

        button = new Button("Enter");
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                String givenUsername = nameEntry.getText().toString();
                String givenPassword = passEntry.getText().toString();

                if (username.equals(givenUsername) && password.equals(givenPassword)) {
                    stage.setScene(scene2);
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Sign in details not found");
                    alert.setContentText("Please check your username and password");
                    alert.showAndWait();
                    stage.setScene(scene2);
                }
            }
        });

        button.setStyle("-fx-background-color:darkgrey;");
        grid.add(button, 3, 3);

        scene = new Scene(grid, 350, 200);

        grid.setStyle("-fx-background-color:black;");
        scene.setFill(Color.BLACK);
        stage.setScene(scene);

        // Layout 2

        GridPane grid2 = new GridPane();
        grid2.setHgap(20);
        grid2.setVgap(20);
        grid2.setPadding(new Insets(0, 10, 0, 10));

        Label subtitle2 = new Label("Welcome");
        subtitle2.setTextFill(Color.WHITE);
        grid2.add(subtitle2, 0, 0);

        scene2 = new Scene(grid2, 1200, 700);
        grid2.setStyle("-fx-background-color:black;");
        scene2.setFill(Color.BLACK);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}