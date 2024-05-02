package com.example;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class LogInPane extends GridPane {

    public Button submit = new Button("Login");
    public Button signupPageSwitch = new Button("No Account? Sign Up");
    public TextField nameEntry = new TextField();
    public TextField passEntry = new TextField();

    public void formatPane() {

        this.setHgap(20);
        this.setVgap(20);
        this.setPadding(new Insets(0, 10, 0, 10));

        Label subtitle = new Label("Sign in");
        this.add(subtitle, 0, 0);

        Label nameLabel = new Label("Username/Email: ");
        this.add(nameLabel, 0, 1);

        Label passLabel = new Label("Password: ");
        this.add(passLabel, 0, 2);

        nameEntry.setMaxWidth(150.0);
        nameEntry.setMaxHeight(8);
        this.add(nameEntry, 1, 1);

        passEntry.setMaxWidth(150.0);
        passEntry.setMaxHeight(8);
        this.add(passEntry, 1, 2);

        this.add(submit, 1, 3);

        this.add(signupPageSwitch, 1, 4);
    }
}