package com.example;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

import com.example.morphia.entities.Task;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    Scene loginScene;
    Scene mainscene;

    Scene signupScene;

    Boolean sidePanelOpen = false;

    AuthConnection auth = new AuthConnection();

    AuthToken token;

    @Override
    public void start(Stage stage) throws IOException {
        token = new AuthToken();

        ScheduledService authRefreshService = new ScheduledService() {
            @Override
            protected javafx.concurrent.Task createTask() {
                javafx.concurrent.Task task = new javafx.concurrent.Task() {
                    @Override
                    protected Object call() throws Exception {
                        if(!token.refreshToken()) {
                            System.out.println("Token expired");
                            try{
                                Platform.runLater(() -> {
                                    if (stage.getScene() == mainscene)
                                        stage.setScene(loginScene);
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println("Changed Scene");
                        }
                        return null;
                    }
                };
                return task;
            };
        };
        authRefreshService.setPeriod(javafx.util.Duration.minutes(1));

        stage.setTitle("focus");
        // Layout 1

        LogInPane loginPane = new LogInPane();
        loginPane.formatPane();
        loginScene = new Scene(loginPane, 350, 300);
        loginScene.getStylesheets()
                .add("style1.css");

        SignupPane signupPane = new SignupPane();
        signupPane.formatPane();
        signupScene = new Scene(signupPane, 350, 300);
        signupScene.getStylesheets()
                .add("style1.css");

        loginPane.signupPageSwitch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                signupScene.getStylesheets()
                        .add("style1.css");
                stage.setScene(signupScene);
            }
        });

        signupPane.loginPageSwitch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loginScene.getStylesheets()
                        .add("style1.css");
                stage.setScene(loginScene);
            }
        });

        loginPane.submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String givenUsername = loginPane.nameEntry.getText().toString();
                String givenPassword = loginPane.passEntry.getText().toString();

                if (checklogin(givenUsername, givenPassword)) {
                    stage.setScene(mainscene);
                }
            }
        });

        signupPane.submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String givenUsername = signupPane.nameEntry.getText().toString();
                String givenEmail = signupPane.emailEntry.getText().toString();
                String givenPassword = signupPane.passEntry.getText().toString();

                if (checksignup(givenUsername, givenEmail, givenPassword)) {
                    stage.setScene(mainscene);
                }
            }
        });

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
        menubutton.getStyleClass().add("collapase-menu-button");

        Label time = new Label();
        time.getStyleClass().add("time-display");

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm\ndd/MM")));
            }
        };
        timer.start();

        VBox vbox = new VBox();
        vbox.prefWidth(1200);
        HBox.setHgrow(vbox, Priority.ALWAYS);

        top.getChildren().addAll(menubutton, focus, vbox, time);

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


        MetricsPane metrics = new MetricsPane();
        metrics.formatPane();

        menugrid.metricsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                border.setCenter(metrics);
            }
        });

        ////

        mainscene = new Scene(border, 1200, 700);
        mainscene.getStylesheets().add("style1.css");

        if (token.refreshToken())
        {
            stage.setScene(mainscene);
        } else {
            stage.setScene(loginScene);
        }

        authRefreshService.start();
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public boolean checklogin(String username, String password){ // Use this for authention
        try {
            AuthConnection.LoginResponse response = auth.login(username, password);
            if (response.success()) {
                token.setToken(response.authtoken());
                return true;
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Details");
                alert.setContentText(response.message());
                alert.showAndWait();
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    public boolean checksignup(String username, String email, String password){ // Use this for authention
        try {
            AuthConnection.LoginResponse response = auth.signup(username, email, password);
            if (response.success()) {
                token.setToken(response.authtoken());
                return true;
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Signup Details");
                alert.setContentText(response.message());
                alert.showAndWait();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}