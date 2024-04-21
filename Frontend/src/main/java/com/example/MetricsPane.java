package com.example;

import com.example.morphia.entities.StressRecording;
import com.example.morphia.entities.User;
import javafx.geometry.Insets;
import javafx.scene.chart.Chart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MetricsPane extends GridPane {

    DBConnection db = new DBConnection();
    List<Metric> metrics = new ArrayList<>();
    TimeRange range = TimeRange.Day;

    public void formatPane() {
        this.setHgap(20);
        this.setVgap(20);
        this.setPadding(new Insets(0, 10, 0, 10));

        this.add(new Label("Metrics"), 0, 0);

        CreateMetrics();
    }

    public void CreateMetrics(){
        User user = new User();//TODO get current logged in user
        user.setStressRecordings(new ArrayList<>());
        List<StressRecording> stressRecordings = user.getStressRecordings();
        TimeLineChart stressChart = new TimeLineChart("Stress","",range);
        stressChart.addSeries("",stressRecordings.stream().map(StressRecording::getTimeTaken).collect(Collectors.toList()), stressRecordings.stream().map(StressRecording::getAverageStressMetric).collect(Collectors.toList()));
        metrics.add(new Metric("Stress",stressChart));

        for (int i = 0; i < metrics.size(); i++) {
            GridPane preview = metrics.get(i).getPreview();
            add(preview,i,1);
        }

    }



}