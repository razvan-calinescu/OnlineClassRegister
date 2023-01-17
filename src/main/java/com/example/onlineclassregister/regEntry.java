package com.example.onlineclassregister;

import javafx.scene.control.Button;

import java.util.Date;

public class regEntry implements Gradable, motivateable {

    public String subject, motivateStatus;
    double value=0;
    boolean motivated=false;

    Button motivate;

    public Button getMotivate() {
        return motivate;
    }

    public String getMotivateStatus() {
        return motivateStatus;
    }

    public String getSubject() {
        return subject;
    }

    public boolean isMotivated() {
        return motivated;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public Date getDate() {
        return date;
    }

    int subjectId;
    Date date;

    public regEntry(int subjectId, Date date) {
        this.subjectId = subjectId;
        this.date = date;
    }

    @Override
    public void setMotivated(boolean value) {
        this.motivated = value;
    }

    @Override
    public boolean getMotivated() {
        return this.motivated;
    }

    @Override
    public void setValue(double val) {
        this.value = val;
    }

    @Override
    public double getValue() {
        return this.value;
    }
}
