package com.example.onlineclassregister;

import java.util.Date;

public class regEntry implements Gradable, motivateable {

    double value=0;
    boolean motivated=false;
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
