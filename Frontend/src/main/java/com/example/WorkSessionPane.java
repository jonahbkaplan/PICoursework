package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.example.morphia.entities.FocusSession;
import com.example.morphia.entities.Task;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class WorkSessionPane extends BorderPane {

    public List<Task> currenttasks = new ArrayList<Task>();
    public VBox currentTaskList = new VBox();

    public Boolean inSession = false;
    public LocalDateTime currentStartTime;
    public List<Task> allTasks = new ArrayList<Task>();

    public ArrayList<FocusSession> recordedSessions = new ArrayList<FocusSession>();

    public void formatPane() {

        this.setPadding(new Insets(100, 100, 100, 100));
        allTasks.add(new Task(LocalDateTime.now(), LocalDateTime.now(), "Demo Task", "Done"));
        allTasks.add(new Task(LocalDateTime.now(), LocalDateTime.now(), "Demo Task2", "None"));
        BorderPane tasksPane = new BorderPane();
        tasksPane.getStyleClass().add("work-gridpanes");
        GridPane sessionPane = new GridPane();
        sessionPane.getStyleClass().add("work-gridpanes");
        formatTasksPane(tasksPane);
        formatSessionPane(sessionPane);
        this.setCenter(tasksPane);
        this.setLeft(sessionPane);
        this.getStyleClass().add("work-backpane");

    }

    public void formatSessionPane(GridPane sessionPane) {

        sessionPane.add(new Label("Start Session"), 0, 0);

        Button startButton = new Button("start");
        sessionPane.add(startButton, 0, 1);
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!inSession) {
                    startButton.setText("stop");
                    currentStartTime = LocalDateTime.now();
                } else {
                    startButton.setText("start");
                    LocalDateTime endTime = LocalDateTime.now();
                    if (currenttasks.isEmpty()) {
                        currenttasks.add(new Task(currentStartTime, endTime, "undefined", "null"));
                    }
                    double timeElapsed = endTime
                            .minus(currentStartTime.getLong(ChronoField.MINUTE_OF_DAY), ChronoUnit.MINUTES)
                            .getLong(ChronoField.MINUTE_OF_DAY);
                    recordedSessions.add(new FocusSession(currenttasks, timeElapsed)); // timeElapsed is number of
                                                                                       // minutes

                    currenttasks.clear();
                    currentTaskList.getChildren().clear();
                }
                inSession = !inSession;
            }

        });

        Label displayTimer = new Label();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (inSession) {
                    displayTimer.setText(
                            LocalDateTime.now()
                                    .minus(currentStartTime.getLong(ChronoField.MINUTE_OF_DAY),
                                            ChronoUnit.MINUTES)
                                    .format((DateTimeFormatter.ofPattern("HH:mm"))));
                } else {
                    displayTimer.setText("");
                }
            }
        };
        timer.start();
        sessionPane.add(displayTimer, 0, 2);

        sessionPane.add(new Label("Currently doing:"), 0, 3);
        sessionPane.add(currentTaskList, 0, 4);

    }

    public void formatTasksPane(BorderPane tasksPane) {

        GridPane grid = new GridPane();
        tasksPane.setTop(grid);

        grid.add(new Label("Manage Tasks"), 0, 0);

        Button addTaskButton = new Button("Create new task");
        grid.add(addTaskButton, 0, 1);
        grid.add(new Label("Tasks To Do:"), 0, 2);

        // Table

        VBox Vtable = new VBox();
        tasksPane.setCenter(Vtable);

        GridPane tabletop = new GridPane();

        String[] topRowStrings = { "Task                ",
                "Start at            ",
                "Finish by           ",
                "Status              ",
                "Manage " };

        for (int i = 0; i < topRowStrings.length; i++) {
            tabletop.add(new Label(topRowStrings[i]), i, 0);
        }

        for (Task task : allTasks) {
            try {
                TaskTableRow taskrow = new TaskTableRow(task);

                Vtable.getChildren().add(taskrow);

                taskrow.taskFinished.setOnAction(new EventHandler<ActionEvent>() { // Currently throws null pointer
                                                                                   // exception
                    @Override
                    public void handle(ActionEvent event) {
                        taskrow.setRowStatus("Finished");
                        Vtable.getChildren().remove(taskrow);
                    }
                });

                taskrow.doTask.setOnAction(new EventHandler<ActionEvent>() { // Currently throws null pointer
                    // exception
                    @Override
                    public void handle(ActionEvent event) {
                        taskrow.setRowStatus("In progress");
                        task.setStatus("In progress");
                        currenttasks.add(task);
                        currentTaskList.getChildren().add(new Label(task.getDescription()));
                    }
                });

            } catch (NullPointerException e) {
                //
            }

        }
    }
}