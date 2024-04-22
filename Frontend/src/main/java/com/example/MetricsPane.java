//package com.example;
//
//import com.example.morphia.entities.StressRecording;
//import com.example.morphia.entities.User;
//import javafx.geometry.Insets;
//import javafx.scene.chart.Chart;
//import javafx.scene.control.Label;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.GridPane;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class MetricsPane extends GridPane {
//
//    MetricPreviewPane previewPane = new MetricPreviewPane(this);
//    MetricViewPane viewPane = new MetricViewPane();
//
//    public void formatPane() {
//        this.setHgap(20);
//        this.setVgap(20);
//        this.setPadding(new Insets(0, 10, 0, 10));
//
//
//        this.add(new Label("Metrics"), 0, 0);
//        TimeButtons buttons = new TimeButtons(this::onTimeChanged,TimeRange.Day);
//        add(buttons,0,18);
//
//        add(previewPane,0,1);
//        viewPane.setVisible(false);
//        add(viewPane,0,1);
//
//
//
//    }
//
//    public void onTimeChanged(TimeRange newRange){
//        previewPane.onTimeChanged(newRange);
//    }
//
//
//
//
//
//
//
//}