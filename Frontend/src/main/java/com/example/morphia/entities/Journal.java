package com.example.morphia.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Entity("Journal")
public class Journal {
    @Id
    ObjectId id;

    LocalDateTime timeRecorded;
    //TODO replace Object with the jounal class
    List<Object> entries;
    public Journal(){};
    public Journal(LocalDateTime timeRecorded, List<Object> entrys) {
        this.timeRecorded = timeRecorded;
        this.entries = entrys;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public LocalDateTime getTimeRecorded() {
        return timeRecorded;
    }

    public void setTimeRecorded(LocalDateTime timeRecorded) {
        this.timeRecorded = timeRecorded;
    }

    public List<Object> getEntries() {
        return entries;
    }

    public void setEntries(List<Object> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "id=" + id +
                ", timeRecorded=" + timeRecorded +
                ", entries=" + entries +
                '}';
    }
}
