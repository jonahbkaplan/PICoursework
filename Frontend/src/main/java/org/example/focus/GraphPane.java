package org.example.focus;
import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class GraphPane extends GridPane {

    List<TimeRange> ranges = List.of(TimeRange.Day,TimeRange.Week,TimeRange.Month,TimeRange.HalfYear,TimeRange.Year);
    HashMap<ToggleButton,TimeRange> buttons = new HashMap<>();
    TimeRange timeRange = TimeRange.Day;

    List<TimeChart> timeCharts = new ArrayList<>();
    public GraphPane(){
        TimeLineChart graph = new TimeLineChart("Heartrate","b/s",timeRange);
        TimePieChart graph2 = new TimePieChart("Not Heartrate","b/s",timeRange);
        graph.refresh();
        graph2.refresh();
        List<LocalDateTime> time = new ArrayList<>();
        List<Float> heartRate = new ArrayList<>();
        List<Float> other = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 24*30; i++) {
            time.add(LocalDateTime.now().minusDays(30).plusHours(i));
            heartRate.add(rand.nextFloat(0,100));
            other.add(rand.nextFloat(0,100));
        }
        graph.addSeries("Firsthhh",time,heartRate);
        graph2.addSlice("Thing1",time,heartRate);
        graph2.addSlice("Thing2",time,other);
        this.add(graph,0,1);
        this.add(graph2,0,2);
        timeCharts.add(graph);
        timeCharts.add(graph2);
        addTimeButtons();


    }

    public void onTimeButtonSelected(ActionEvent e){
        TimeRange range = buttons.get((ToggleButton) e.getSource());
        if(range == timeRange) return;
        for (TimeChart chart : timeCharts){
            chart.setTimeRange(range);
            timeRange = range;
            chart.refresh();
        }

    }

    private void addTimeButtons(){
        ToggleGroup group = new ToggleGroup();
        HBox box = new HBox();
        for (TimeRange range : ranges){
            ToggleButton button = new ToggleButton(range.name);
            button.setToggleGroup(group);
            box.getChildren().add(button);
            button.setOnAction(this::onTimeButtonSelected);
            buttons.put(button,range);
            if(range == timeRange) button.setSelected(true);
        }

        this.add(box,0,0);
    }



}


