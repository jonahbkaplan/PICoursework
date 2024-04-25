package com.example;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import com.example.morphia.entities.Task;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class DisplayTask {

    private MenuButton menubutton = new MenuButton();
    public MenuItem addToSession = new MenuItem("Add to session");

    private Dictionary<Pane, Dictionary<String, Label>> labelInstances = new Hashtable<>();
    private ArrayList<Pane> hasMenuButton = new ArrayList<>();

    public Task task;

    private String[] taskLabelTypes = { "Description", "StartTime", "EndTime", "Status" };

    public String[] statuses = { "Finished", "In progress", "Not started", "Paused" };

    public DisplayTask(Task task) {

        Menu statusMenu = new Menu("Change Status");
        ToggleGroup toggleGroup = new ToggleGroup();
        for (String status : statuses) {
            RadioMenuItem radioItem = new RadioMenuItem(status);
            radioItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    updateDisplayStatus(status);
                }
            });
            radioItem.setToggleGroup(toggleGroup);
            statusMenu.getItems().add(radioItem);
        }

        MenuItem remove = new MenuItem("Remove");
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeAll();
            }
        });

        menubutton.getItems().addAll(remove, addToSession, statusMenu);
        this.task = task;
    }

    public void addAllToGridAsRow(GridPane grid, int row) {

        Dictionary<String, Label> gridInstance = new Hashtable<>();

        String[] taskStrings = { task.getDescription(),
                task.getStartTime().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")),
                task.getEndTime().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")),
                task.getStatus() };

        for (int i = 0; i < taskStrings.length; i++) {
            Label lbl = new Label(taskStrings[i]);
            grid.add(lbl, i, row);
            gridInstance.put(taskLabelTypes[i], lbl);
        }

        grid.add(menubutton, taskStrings.length, row);

        labelInstances.put(grid, gridInstance);
        hasMenuButton.add(grid);
    }

    public void removeAll() {
        removeAllLabels();
        removeAllMenuButtons();
    }

    public void removeAllLabels() {

        Enumeration<Pane> keys = labelInstances.keys();

        while (keys.hasMoreElements()) {
            Pane parent = keys.nextElement();
            Dictionary<String, Label> parentInstances = labelInstances.get(parent);
            Enumeration<String> stringkeys = parentInstances.keys();
            while (stringkeys.hasMoreElements()) {
                parent.getChildren().remove(parentInstances.get(stringkeys.nextElement()));
            }
        }
    }

    public void removeAllMenuButtons() {
        for (Pane pane : hasMenuButton) {
            pane.getChildren().remove(menubutton);
        }
        hasMenuButton.clear();
    }

    public void updateDisplayStatus(String status) {

        task.setStatus(status);

        Enumeration<Pane> keys = labelInstances.keys();

        while (keys.hasMoreElements()) {
            Pane parent = keys.nextElement();
            Dictionary<String, Label> parentInstances = labelInstances.get(parent);
            Label label = parentInstances.get(taskLabelTypes[3]);
            if (label != null) {
                label.setText(status);
            }
        }
    }

    public void removeFromPane(Pane pane) {

        Dictionary<String, Label> paneInstances = labelInstances.get(pane);

        if (paneInstances == null) {
            return;
        }

        Enumeration<String> keys = paneInstances.keys();

        while (keys.hasMoreElements()) {
            pane.getChildren().remove(paneInstances.get(keys.nextElement()));
        }

    }

    public void addDescription(Pane pane) {
        Dictionary<String, Label> instance = new Hashtable<>();
        Label lbl = new Label(task.getDescription());
        instance.put(taskLabelTypes[0], lbl);
        pane.getChildren().add(lbl);

        labelInstances.put(pane, instance);

    }
}