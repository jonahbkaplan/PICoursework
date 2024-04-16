package com.example.morphia.entities;

import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.util.List;

public class Calendar {
    @Id
    ObjectId id;
    @Reference
    List<Task> tasks;

    public Calendar(){};
    public Calendar(List<Task> tasks) {
        this.tasks = tasks;
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

    @Override
    public String toString() {
        return "Calendar{" +
                "id=" + id +
                ", tasks=" + tasks +
                '}';
    }
}
