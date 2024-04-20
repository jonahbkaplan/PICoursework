package com.example;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.example.morphia.entities.Task;

import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;

public class TaskTableRow extends GridPane {

    public Task task;

    public MenuButton manageTasks;

    public MenuItem taskFinished = new MenuItem("Finished");
    public MenuItem doTask = new MenuItem("Do");

    public ArrayList<Label> labels = new ArrayList<Label>();

    public TaskTableRow(Task task) {

        String[] strings = { task.getDescription(),
                task.getStartTime().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")),
                task.getEndTime().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")),
                task.getStatus() };

        for (int i = 0; i < strings.length; i++) {
            Label lbl = new Label(strings[i]);
            lbl.getStyleClass().add("task-table-cell");
            labels.add(lbl);
            this.add(lbl, i, 0);
        }

        MenuButton manageTasks = new MenuButton();
        this.add(manageTasks, strings.length, 0);

        manageTasks.getItems().addAll(taskFinished, doTask);
    }

    public void setRowStatus(String newStatus) {
        labels.get(3).setText(newStatus);
    }

    public void showOnlyDescription() {
        this.getChildren().remove(labels.get(3));
        this.getChildren().remove(labels.get(2));
        this.getChildren().remove(labels.get(1));
        this.getChildren().remove(manageTasks);
    }

    public Task getTask() {
        return task;
    }
}