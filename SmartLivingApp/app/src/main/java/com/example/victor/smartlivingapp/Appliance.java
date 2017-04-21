package com.example.victor.smartlivingapp;

import android.widget.ProgressBar;

import java.util.*;

public class Appliance {

    private String type;
    private boolean completed;
    private int progress;
    private double powerConsumption;

    public Appliance(String type, ProgressBar bar) {
        this.type = type;
        if(type.equals("vacuum")) powerConsumption = 0.61;
        if(type.equals("lawnmower")) powerConsumption = 0.53;
        this.progress = 0;
        incrementProgress(bar);
    }

    public boolean getCompleted() {
        return this.completed;
    }

    public int getProgress() {
        return this.progress;
    }

    public void incrementProgress(final ProgressBar bar) {
        int delay = 5000; // delay for 5 sec.
        int period = 1000; // repeat every sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                progress++;
                bar.setProgress(progress);
            }
        }, delay, period);
    }

    public double getPowerConsumption() {
        return this.powerConsumption;
    }
}
