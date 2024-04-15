package org.example.focus;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.function.UnaryOperator;

public class TimeRange {

    public interface IRange {
        LocalDateTime getTimeRange(LocalDateTime startTime);
    }
    public static TimeRange Day = new TimeRange((start) -> start.minusDays(1),"HH",60*60*24,60*60,"D");
    public static TimeRange Week = new TimeRange((start) -> start.minusWeeks(1),"EE",60*60*24*7,60*60*24,"W");
    public static TimeRange Month = new TimeRange((start) -> start.minusMonths(1),"dd", 60*60*24*30,60*60*24,"M");
    public static TimeRange HalfYear = new TimeRange((start) -> start.minusMonths(6),"MM", 60*60*24*182, 60*60*24*30,"6M");
    public static TimeRange Year = new TimeRange((start) -> start.minusYears(1),"MM",60*60*24*365, 60*60*24*30,"Y");


    IRange timeRange;
    String pattern;
    long length;
    String name;
    double unit;

    public TimeRange(IRange timeRange, String pattern, long length, double unit, String name){
        this.timeRange = timeRange;
        this.pattern = pattern;
        this.name = name;
        this.length = length;
        this.unit = unit;
    }
}
