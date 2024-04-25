package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.example.morphia.entities.FocusSession;
import com.example.morphia.entities.Task;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class WorkSessionPane extends BorderPane {

    public Dictionary<DisplayTask, String> prevStatuses = new Hashtable();
    public ArrayList<DisplayTask> currentDisplayTasks = new ArrayList<DisplayTask>();

    public VBox currentTaskList = new VBox();

    public Boolean inSession = false;
    public LocalDateTime currentStartTime;
    public List<Task> allTasks = new ArrayList<Task>();

    public ArrayList<FocusSession> recordedSessions = new ArrayList<FocusSession>();

    public void formatPane() {

        // WARNING DUCKTAPE CODE

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

                    for (DisplayTask displayTask : currentDisplayTasks) {
                        prevStatuses.put(displayTask, displayTask.task.getStatus());
                        displayTask.updateDisplayStatus("In progress");
                    }

                } else {
                    startButton.setText("start");
                    LocalDateTime endTime = LocalDateTime.now();

                    ArrayList<Task> currentTasks = new ArrayList<Task>();

                    for (DisplayTask displayTask : currentDisplayTasks) {
                        displayTask.updateDisplayStatus(prevStatuses.get(displayTask));
                        currentTasks.add(displayTask.task);
                    }

                    if (currentTasks.isEmpty()) {
                        currentTasks.add(new Task(currentStartTime, endTime, "undefined", "null"));
                    }

                    double timeElapsed = endTime
                            .minus(currentStartTime.getLong(ChronoField.MINUTE_OF_DAY), ChronoUnit.MINUTES)
                            .getLong(ChronoField.MINUTE_OF_DAY);
                    recordedSessions.add(new FocusSession(currentTasks, timeElapsed)); // timeElapsed is number of
                                                                                       // minutes

                    currentDisplayTasks.clear();
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
        sessionPane.add(new Label("Session Tasks: "), 0, 3);
        sessionPane.add(currentTaskList, 0, 5);

    }

    public void formatTasksPane(BorderPane tasksPane) {

        GridPane grid = new GridPane();
        tasksPane.setTop(grid);

        grid.getStyleClass().add("task-table-container");

        grid.add(new Label("Manage Tasks"), 0, 0);

        Button addTaskButton = new Button("Create new task");

        addTaskButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                formatCreateTaskPane(tasksPane);
            }
        });

        grid.add(addTaskButton, 4, 20);
        grid.add(new Label("Tasks To Do:"), 0, 2);

        // Table

        String[] columnStrings = { "Task               ", "Set At                  ", "Finish By                   ",
                "Status                  ",
                "  Manage" };

        DisplayTaskTable tasksTable = new DisplayTaskTable(grid, 3, columnStrings);
        for (Task task : allTasks) {
            DisplayTask displayTask = new DisplayTask(task);
            tasksTable.addToTable(displayTask);
            displayTask.addToSession.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    displayTask.addDescription(currentTaskList);
                    currentDisplayTasks.add(displayTask);
                }
            });
        }
    }

    public void formatCreateTaskPane(BorderPane tasksPane) {

        GridPane grid = new GridPane();
        tasksPane.setTop(grid);

        String[] labelStrings = { "Create Task", "Task Description: ", "Start at: ", "Finish by: ", "Status: " };
        for (int i = 0; i < labelStrings.length; i++) {
            grid.add(new Label(labelStrings[i]), 0, i);
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yy");

        TextField taskDescription = new TextField();
        grid.add(taskDescription, 1, 1);
        TextField startTime = new TextField(LocalDateTime.now()
                .format(timeFormatter));
        grid.add(startTime, 1, 2);
        TextField endTime = new TextField(LocalDateTime.now()
                .format(timeFormatter));
        grid.add(endTime, 1, 3);

        String[] statuses = { "Finished", "In progress", "Not started", "Paused" };

        MenuButton statusOptions = new MenuButton(statuses[2]);
        ToggleGroup toggleGroup = new ToggleGroup();
        for (String status : statuses) {
            RadioMenuItem radioItem = new RadioMenuItem(status);
            radioItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    statusOptions.setText(status);
                }
            });
            radioItem.setToggleGroup(toggleGroup);
            statusOptions.getItems().add(radioItem);
        }
        grid.add(statusOptions, 1, 4);
        Button enterTask = new Button("Enter");

        enterTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Task newTask = new Task(LocalDateTime.parse(startTime.getText().toString(), timeFormatter),
                        LocalDateTime.parse(endTime.getText().toString(), timeFormatter),
                        taskDescription.getText().toString(), statusOptions.getText());
                allTasks.add(newTask);
                formatTasksPane(tasksPane);
            }
        });
        grid.add(enterTask, 2, 10);
    }
}