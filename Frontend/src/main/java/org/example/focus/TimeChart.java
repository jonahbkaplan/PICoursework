package org.example.focus;

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
        List<Float> yData;

        public ChartData(String name, List<LocalDateTime> timeData, List<Float> yData) {
            this.name = name;
            this.timeData = timeData;
            this.yData = yData;
        }


    }

    TimeRange range;
    Chart chart;
    Text avgText = new Text();

    String unit;



    public TimeChart(String label, String unit, TimeRange range) {
        this.unit = unit;

        this.range = range;
        this.chart = createChart(label);

        setTimeRange(range);
        Text lbl = new Text("Average:");
        lbl.setTextAlignment(TextAlignment.CENTER);

        add(lbl,0,0);
        add(avgText,0,1);
        add(chart,0,2);


    }

    public abstract Chart createChart(String yLabel);



    public void setTimeRange(TimeRange range){
        this.range = range;
    }




    public abstract void refresh();
}

