package com.example.victor.smartlivingapp;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Activity;

import java.util.*;

public class Appliance {

    private String type;
    private boolean completed;
    private int progress;
    private double rate;

    public Appliance(String type, ProgressBar bar, TextView power, Activity mainActivity) {
        this.type = type;
        if(type.equals("vacuum")) rate = 0.61;
        if(type.equals("lawnmower")) rate = 0.53;
        this.progress = 0;
        incrementProgress(bar, power, mainActivity);
    }

    public boolean getCompleted() {
        return this.completed;
    }

    public int getProgress() {
        return this.progress;
    }

    public void incrementProgress(final ProgressBar bar, final TextView power, final Activity mainActivity) {
        int delay = 5000; // delay for 5 sec.
        int period = 1000; // repeat every sec.
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress++;
                        bar.setProgress(progress);
                        Double consumption = progress * rate;
                        power.setText(consumption.toString() + " kW");
                    }
                });
            }
        }, delay, period);
    }

    public double getPowerConsumption() {
        return this.rate;
    }
}
