package com.example.morphia.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Entity("StressRecording")
public class StressRecording {
    @Id
    ObjectId id;
    double averageStressMetric;
    double averageHeartRate;
    double averageBloodPressure;
    double timeElapsed;
    LocalDateTime timeTaken;

    public StressRecording(){};


    public StressRecording(double averageStressMetric, double averageHeartRate, double averageBloodPressure, double timeElapsed, LocalDateTime timeTaken) {
        this.averageStressMetric = averageStressMetric;
        this.averageHeartRate = averageHeartRate;
        this.averageBloodPressure = averageBloodPressure;
        this.timeElapsed = timeElapsed;
        this.timeTaken = timeTaken;
    }



    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public double getAverageStressMetric() {
        return averageStressMetric;
    }

    public void setAverageStressMetric(double averageStressMetric) {
        this.averageStressMetric = averageStressMetric;
    }

    public double getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(double averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    public double getAverageBloodPressure() {
        return averageBloodPressure;
    }

    public void setAverageBloodPressure(double averageBloodPressure) {
        this.averageBloodPressure = averageBloodPressure;
    }

    public double getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(double timeElapsed) {
        this.timeElapsed = timeElapsed;
    }


    public LocalDateTime getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(LocalDateTime timeTaken) {
        this.timeTaken = timeTaken;
    }

    @Override
    public String toString() {
        return "StressRecording{" +
                "id=" + id +
                ", averageStressMetric=" + averageStressMetric +
                ", averageHeartRate=" + averageHeartRate +
                ", averageBloodPressure=" + averageBloodPressure +
                ", timeElapsed=" + timeElapsed +
                ", timeTaken=" + timeTaken +
                '}';
    }
}
