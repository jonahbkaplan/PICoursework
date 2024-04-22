package com.example;

import javafx.scene.chart.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.time.LocalDateTime;
import java.util.List;

public abstract class TimeChart extends GridPane {

    protected static class ChartData {
        String name;
        List<LocalDateTime> timeData;
        List<Double> yData;

        public ChartData(String name, List<LocalDateTime> timeData, List<Double> yData) {
            this.name = name;
            this.timeData = timeData;
            this.yData = yData;
        }


    }

    TimeRange range;
    Chart chart;


    String unit;

    public Chart getChart(){return chart;}
    public TimeRange getRange(){return range;}

    public TimeChart(String label, String unit, TimeRange range) {
        this.unit = unit;

        this.range = range;
        this.chart = createChart(label);

        setTimeRange(range);

        add(chart,0,2);
        chart.getStylesheets().add("chart.css");
        chart.getStyleClass().add("root");

    }

    public abstract Chart createChart(String yLabel);



    public void setTimeRange(TimeRange range){
        this.range = range;
    }




    public abstract void refresh();
}

