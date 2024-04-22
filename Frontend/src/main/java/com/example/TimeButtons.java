package com.example;

import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.List;

public class TimeButtons extends HBox {

    HashMap<ToggleButton,TimeRange> buttons = new HashMap<>();
    List<TimeRange> ranges = List.of(TimeRange.Day,TimeRange.Week,TimeRange.Month,TimeRange.HalfYear,TimeRange.Year);

    OnTimeButton listener;

    public interface OnTimeButton{
        void onButtonSelected(TimeRange range);
    }

    public void onTimeButtonSelected(ActionEvent e){
        listener.onButtonSelected(buttons.get((ToggleButton) e.getSource()));
    }

    public TimeButtons(OnTimeButton listener, TimeRange startingRange){
        this.listener = listener;
        ToggleGroup group = new ToggleGroup();
        for (TimeRange range : ranges){
            ToggleButton button = new ToggleButton(range.name);
            button.setToggleGroup(group);
            getChildren().add(button);
            button.setOnAction(this::onTimeButtonSelected);
            buttons.put(button,range);
            if(range == startingRange) button.setSelected(true);
        }
        this.getStylesheets().add("chart.css");
        this.getStyleClass().add("time_buttons");
    }
}
