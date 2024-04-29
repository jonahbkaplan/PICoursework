package com.example;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class DisplayTaskTable {

    public ArrayList<DisplayTask> taskRows = new ArrayList<DisplayTask>();
    private GridPane grid;
    private int startRow;

    public DisplayTaskTable(GridPane grid, int startRow, String[] columnStrings) {
        this.grid = grid;
        for (int i = 0; i < columnStrings.length; i++) {
            grid.add(new Label(columnStrings[i]), i, startRow);
        }

        this.startRow = startRow;
    }

    public void addToTable(DisplayTask displaytask) {
        taskRows.add(displaytask);
        displaytask.addAllToGridAsRow(this.grid, taskRows.size() + this.startRow + 1);
    }

    public void reformatTable() {
        for (DisplayTask displaytask : this.taskRows) {
            displaytask.removeFromPane(this.grid);
            addToTable(displaytask);
        }
    }

    public void removeDisplayTask(DisplayTask displaytask) {
        displaytask.removeFromPane(this.grid);
        taskRows.remove(displaytask);
        reformatTable();
    }

}
