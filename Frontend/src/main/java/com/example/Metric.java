package com.example;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.awt.*;

public class Metric {

    String name;
    TimeChart chart;
    MetricsPane metricsPane;
    public Metric(String name,TimeChart chart, MetricsPane metricsPane){
        this.name = name;
        this.chart = chart;
        this.metricsPane = metricsPane;
    }

    public TimeChart getChart(){
        return chart;
    }


    public GridPane getPreview(){
       GridPane pane = new GridPane();
       pane.setGridLinesVisible(true);
       pane.setMaxSize(110,110);
       pane.getColumnConstraints().add(new ColumnConstraints(220));
       Text title = new Text(this.name);
       title.setFont(Font.font("default", FontWeight.BOLD,20));
       GridPane.setHalignment(title, HPos.CENTER);

       if(chart.getChart() instanceof LineChart){
           ((LineChart<?, ?>) chart.getChart()).getYAxis().setLabel("");
       }

       title.setFill(Color.WHITE);
       title.setTextAlignment(TextAlignment.CENTER);

       pane.add(title,0,0);
       pane.add(chart,0,1);
       pane.setOnMouseClicked(this::onMouseClicked);

       return pane;
    }

    private void onMouseClicked(MouseEvent mouseEvent) {

    }


}
