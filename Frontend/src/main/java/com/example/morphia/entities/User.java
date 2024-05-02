package com.example.morphia.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.util.List;

@Entity("User")
public class User {

    @Id
    ObjectId id;
    String userID;
    String password;
    @Reference
    Journal journal;
    @Reference
    List<FocusSession> focusSessions;
    @Reference
    List<StressRecording> stressRecordings;

    public User(){}
    public User(String userID, String password, Journal journal, List<FocusSession> focusSessions, List<StressRecording> stressRecordings) {
        this.userID = userID;
        this.password = password;
        this.journal = journal;
        this.focusSessions = focusSessions;
        this.stressRecordings = stressRecordings;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public List<FocusSession> getFocusSessions() {
        return focusSessions;
    }

    public void setFocusSessions(List<FocusSession> focusSessions) {
        this.focusSessions = focusSessions;
    }

    public List<StressRecording> getStressRecordings() {
        return stressRecordings;
    }

    public void setStressRecordings(List<StressRecording> stressRecordings) {
        this.stressRecordings = stressRecordings;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userID='" + userID + '\'' +
                ", password='" + password + '\'' +
                ", journal=" + journal +
                ", focusSessions=" + focusSessions +
                ", stressRecordings=" + stressRecordings +
                '}';
    }
}
