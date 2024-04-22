package com.example;

import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class TimeLineChart extends TimeChart{


    List<ChartData> seriesList = new ArrayList<>();
    Text avgText = new Text();

    public TimeLineChart(String yLabel, String unit, TimeRange range) {
        super(yLabel, unit, range);

        Text lbl = new Text("Average:");
        lbl.setTextAlignment(TextAlignment.CENTER);

        add(lbl,0,0);
        add(avgText,0,1);

    }

    @Override
    public Chart createChart(String yLabel){
        LineChart<Number,Number> graph = new LineChart<>(new NumberAxis(0, range.length, range.unit), new NumberAxis());
        graph.getStyleClass().add("line");
        graph.getYAxis().setLabel(yLabel);
        return graph;
    }

    public void addSeries(String name, List<LocalDateTime> timeData, List<Double> yData){


        XYChart.Series<Number,Number> series = new XYChart.Series<>();
        for (int i = 0; i < timeData.size(); i++) {
            long diff = timeData.get(i).toEpochSecond(ZoneOffset.ofHours(0));
            series.getData().add(new XYChart.Data<>(diff,yData.get(i)));
        }
        series.setName(name);
        ((LineChart<Number,Number>) chart).getData().add(series);
        this.seriesList.add(new ChartData(name,timeData,yData));
        refresh();

    }

    private float GetAverage(){

        int count = 0;
        float sum = 0;
        for(ChartData series : seriesList){
            for (int i = 0; i < series.timeData.size(); i++) {
                if(series.timeData.get(i).isBefore(range.timeRange.getTimeRange(LocalDateTime.now()))) continue;
                sum += series.yData.get(i);
                count++;
            }
        }
        return Math.round(sum / count);
    }

    public void refresh(){
        NumberAxis axis = (NumberAxis) ((LineChart<Number,Number>) chart).getXAxis();
        LocalDateTime startDate = range.timeRange.getTimeRange(LocalDateTime.now());
        long seconds = startDate.toEpochSecond(ZoneOffset.ofHours(0));
        axis.setLowerBound(seconds);
        axis.setUpperBound(seconds+range.length);
        axis.setTickUnit(range.unit);
        avgText.setText(GetAverage() + unit);

        axis.setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number number) {
                return DateTimeFormatter.ofPattern(range.pattern).format(LocalDateTime.ofEpochSecond(number.longValue(), 0, ZoneOffset.ofHours(0)));
            }

            @Override
            public Number fromString(String s) {
                return 0;
            }
        });

    }
}
