package org.example.focus;

import javafx.scene.chart.Chart;
import javafx.scene.chart.PieChart;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimePieChart extends TimeChart{

    List<ChartData> slices = new ArrayList<>();
    public TimePieChart(String label, String unit, TimeRange range) {
        super(label, unit, range);
    }

    @Override
    public Chart createChart(String label) {
        PieChart pie = new PieChart();

        return pie;
    }

    @Override
    public void refresh() {
        ((PieChart) chart).getData().clear();
        for(ChartData slice : slices){
            float sum = 0;
            for (int i = 0; i < slice.timeData.size(); i++) {
                if(slice.timeData.get(i).isBefore(range.timeRange.getTimeRange(LocalDateTime.now()))) continue;
                sum += slice.yData.get(i);
            }
            ((PieChart) chart).getData().add(new PieChart.Data(slice.name,sum));
        }
    }

    public void addSlice(String name, List<LocalDateTime> timeData, List<Float> data){
        slices.add(new ChartData(name,timeData,data));
        refresh();
    }
}
