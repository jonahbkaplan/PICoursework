package com.example;

import com.example.morphia.entities.StressRecording;
import com.example.morphia.entities.User;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MetricPreviewPane extends GridPane {

    DBConnection db = new DBConnection();
    List<Metric> metrics = new ArrayList<>();
    TimeRange range = TimeRange.Day;
    MetricsPane metricsPane;

    public MetricPreviewPane(MetricsPane metricsPane){
        CreateMetrics();
        this.metricsPane = metricsPane;
    }

    public void onTimeChanged(TimeRange newRange){
        this.range = newRange;
        for(Metric metric : metrics){
            metric.getChart().setTimeRange(range);
            metric.getChart().refresh();
        }

    }

    public void CreateMetrics(){
        User user = new User();//TODO get current logged in user
        user.setStressRecordings(new ArrayList<>());
        List<StressRecording> stressRecordings = user.getStressRecordings();
        TimeLineChart stressChart = new TimeLineChart("Stress","",range);
        TimeLineChart hrChart = new TimeLineChart("Stress","",range);
        stressChart.addSeries("",stressRecordings.stream().map(StressRecording::getTimeTaken).collect(Collectors.toList()), stressRecordings.stream().map(StressRecording::getAverageStressMetric).collect(Collectors.toList()));
        hrChart.addSeries("",stressRecordings.stream().map(StressRecording::getTimeTaken).collect(Collectors.toList()), stressRecordings.stream().map(StressRecording::getAverageStressMetric).collect(Collectors.toList()));
        metrics.add(new Metric("Stress",stressChart,metricsPane));
        metrics.add(new Metric("Heartrate",hrChart,metricsPane));

        GridPane pane = new GridPane();


        for (int i = 0; i < metrics.size(); i++) {
            GridPane preview = metrics.get(i).getPreview();
            pane.add(preview,i,1);
        }
        getChildren().add(pane);

    }




}
