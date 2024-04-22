package com.example;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class SignupPane extends GridPane {
    public Button submit = new Button("Signup");
    public Button loginPageSwitch = new Button("Have an account? Log In");
    public TextField nameEntry = new TextField();

    public TextField emailEntry = new TextField();
    public TextField passEntry = new TextField();

    public void formatPane() {

        this.setHgap(20);
        this.setVgap(20);
        this.setPadding(new Insets(0, 10, 0, 10));

        Label subtitle = new Label("Sign Up");
        this.add(subtitle, 0, 0);

        Label nameLabel = new Label("Username");
        this.add(nameLabel, 0, 1);

        Label emailLabel = new Label("Email");
        this.add(emailLabel, 0, 2);

        Label passLabel = new Label("Password: ");
        this.add(passLabel, 0, 3);

        nameEntry.setMaxWidth(150.0);
        nameEntry.setMaxHeight(8);
        this.add(nameEntry, 1, 1);

        emailEntry.setMaxWidth(150.0);
        emailEntry.setMaxHeight(8);
        this.add(emailEntry, 1, 2);

        passEntry.setMaxWidth(150.0);
        passEntry.setMaxHeight(8);
        this.add(passEntry, 1, 3);

        this.add(submit, 1, 4);

        this.add(loginPageSwitch, 1, 5);
    }
}
