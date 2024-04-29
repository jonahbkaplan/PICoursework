
package com.example;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class SettingsPane extends GridPane {

    public Button changeuserbutton = new Button();
    public MenuButton targetOptions =  new MenuButton();

    public void formatPane() {

        this.setHgap(20);
        this.setVgap(20);
        this.setPadding(new Insets(0, 10, 0, 10));
        this.add(new Label("Settings"), 0, 0);

        changeuserbutton.setText("Change User");
        this.add(changeuserbutton, 0, 0);

        this.add(new Label("Change target work hours: "), 0, 1);

        String[] targets = {"2", "4", "6", "8", "10"};
        targetOptions.setText(targets[1]);

        ToggleGroup toggleGroup = new ToggleGroup();
        for (String target : targets) {
            RadioMenuItem radioItem = new RadioMenuItem(target);
            radioItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    targetOptions.setText(target);
                }
            });
            radioItem.setToggleGroup(toggleGroup);
            targetOptions.getItems().add(radioItem);
        }

        this.add(targetOptions, 1, 1);
    }
}
