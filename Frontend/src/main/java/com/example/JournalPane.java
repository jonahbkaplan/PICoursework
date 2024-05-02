package com.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.morphia.entities.Journal;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class JournalPane extends BorderPane {

    private LocalDate currentDate = LocalDate.now();
    private LocalDateTime currentDateTime = LocalDateTime.of(currentDate, LocalTime.of(00, 00));
    private VBox showEntries = new VBox();
    private ScrollPane scrollpane = new ScrollPane();
    private GridPane emptyGrid = new GridPane();

    public void formatPane() {

        updateEntries(currentDateTime);

        // this.getStyleClass().add("journal-scroll-pane");

        scrollpane.setContent(showEntries);
        scrollpane.getStyleClass().add("journal-scroll-pane");
        showEntries.getStyleClass().add("journal-vbox");

        scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        GridPane topGrid = new GridPane();

        topGrid.setHgap(20);
        topGrid.setVgap(20);
        topGrid.setPadding(new Insets(10, 10, 10, 10));
        topGrid.getStyleClass().add("journal-top-grid");

        topGrid.add(new Label("Jounalling"), 0, 0);

        Button nextDate = new Button("Next");
        Button prevDate = new Button("Previous");
        Button addNewEntry = new Button("Add entry");

        topGrid.add(addNewEntry, 5, 0);

        addNewEntry.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setPrevEntriesRight();
                setAddEntryPanel();
            }
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        TextField date = new TextField(currentDate.format(formatter));

        topGrid.add(date, 2, 0);
        topGrid.add(nextDate, 3, 0);
        topGrid.add(prevDate, 1, 0);

        this.setTop(topGrid);

        this.setMargin(topGrid, new Insets(20));

        nextDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentDateTime = currentDateTime.plusDays(1);
                date.setText(currentDateTime.format(formatter));
                updateEntries(currentDateTime);
            }
        });

        prevDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentDateTime = currentDateTime.minusDays(1);
                date.setText(currentDateTime.format(formatter));
                updateEntries(currentDateTime);
            }
        });

        defaultLayout();
    }

    private void setAddEntryPanel() {
        GridPane expandGrid = new GridPane();

        expandGrid.add(new Label(currentDateTime.format(DateTimeFormatter.ofPattern("dd/MM"))), 0, 0);

        Label timeLabel = new Label(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.getStyleClass().add("journal-expand-entry");
        expandGrid.add(timeLabel, 0, 1);

        TextField entryField = new TextField();
        expandGrid.add(entryField, 0, 2);
        entryField.getStyleClass().add("journal-entry-textfield");

        Button enterEntry = new Button("Enter");
        expandGrid.add(enterEntry, 0, 3);
        enterEntry.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                List<Object> entries = new ArrayList<>();
                entries.add(timeLabel.getText() + "\n"
                        + entryField.getText().toString());

                saveNewEntryToServer(new Journal(currentDateTime, entries));

                defaultLayout();
                clearRightAndLeft();
            }
        });

        this.setLeft(expandGrid);

    }

    private void defaultLayout() {
        this.setCenter(scrollpane);
        showEntries.setPrefWidth(1200);
    }

    private void clearRightAndLeft() {
        this.setLeft(new GridPane());
        this.setRight(new GridPane());
    }

    private void setPrevEntriesRight() {

        GridPane rightGrid = new GridPane();

        this.setCenter(emptyGrid);

        this.setRight(rightGrid);

        Button prevEntries = new Button("Previous Entries");

        rightGrid.add(prevEntries, 0, 0);
        rightGrid.add(scrollpane, 0, 1);
        scrollpane.setPrefWidth(400);
        showEntries.setPrefWidth(400);

        prevEntries.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                defaultLayout();
                clearRightAndLeft();
            }
        });
    }

    private ArrayList<String> getDateEntries(LocalDateTime datetime) { // To return the entries from backend for a given
                                                                       // day.
        ArrayList<String> example = new ArrayList<>();

        example.add(
                "12:12 \n Supposing that truth is a woman - what then? \n Is there not grounds for suspecting that all philosophers, in so far as they have been dogmatists, \n have failed to understand women - that the terrible seriousness and clumsy inportunity with which they have usually paid their addresses to truth, \n have been unskilled and unseemely methods for winning a woman? ");
        example.add(
                "19:56 \nThe Will to Truth, which is to tempt us to many a hazardous enterprise, the famous Truthfulness of which all philosophers have hitherto spoken with respect, \n what questions has this Will to truth not laid before us!");
        return example;
    }

    public void updateEntries(LocalDateTime datetime) {

        showEntries.getChildren().clear();

        for (int i = 0; i < 7; i++) {
            Label date = new Label(datetime.minusDays(i).format(DateTimeFormatter.ofPattern("dd/MM")));

            showEntries.getChildren().add(date);

            ArrayList<String> entries = getDateEntries(datetime.minusDays(i));

            for (String entry : entries) {
                Label entryLabel = new Label(entry);
                entryLabel.getStyleClass().add("entry-theme");
                showEntries.getChildren().add(entryLabel);

                ContextMenu lookAtEntry = new ContextMenu();

                MenuItem selectEntry = new MenuItem("Expand");

                lookAtEntry.getItems().add(selectEntry);
                int idx = i;
                lookAtEntry.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        expandEntry(datetime.minusDays(idx), entry);
                        setPrevEntriesRight();
                    }
                });

                entryLabel.setContextMenu(lookAtEntry);
            }
        }
    }

    public void expandEntry(LocalDateTime dateTime, String entry) {

        GridPane expandGrid = new GridPane();

        expandGrid.add(new Label(dateTime.format(DateTimeFormatter.ofPattern("dd/MM"))), 0, 0);

        Label entryLabel = new Label(entry);
        entryLabel.getStyleClass().add("journal-expand-entry");
        expandGrid.add(entryLabel, 0, 1);

        this.setLeft(expandGrid);
    }

    public void saveNewEntryToServer(Journal journal) {
        System.out.println(journal.toString());
        // do something
    }

}