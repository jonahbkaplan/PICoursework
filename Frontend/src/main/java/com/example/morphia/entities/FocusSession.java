package com.example.morphia.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.util.List;

@Entity("FocusSession")
public class FocusSession {
    @Id
    ObjectId id;
    @Reference
    List<Task> tasks;
    double timeElapsed;
    public FocusSession(){}
    public FocusSession(List<Task> tasks, double timeElapsed) {
        this.tasks = tasks;
        this.timeElapsed = timeElapsed;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public double getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(double timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    @Override
    public String toString() {
        return "FocusSession{" +
                "id=" + id +
                ", tasks=" + tasks +
                ", timeElapsed=" + timeElapsed +
                '}';
    }
}
