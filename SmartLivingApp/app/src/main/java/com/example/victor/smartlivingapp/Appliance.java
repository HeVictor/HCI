package com.example.victor.smartlivingapp;

import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Activity;
import java.text.DecimalFormat;
import android.widget.LinearLayout;


import java.util.*;

public class Appliance {

    private String type;
    private boolean completed;
    private int progress;
    private double rate;

    public Appliance(String type, ProgressBar bar, TextView power, Activity mainActivity,
                     LinearLayout ip, LinearLayout comp, LinearLayout contIP, LinearLayout compCont) {
        this.type = type;
        if(type.equals("vacuum")) rate = 0.61;
        if(type.equals("lawnmower")) rate = 0.53;
        this.progress = 0;
        contIP.addView(ip);
        incrementProgress(bar, power, mainActivity, ip, comp, contIP, compCont);
    }

    public boolean getCompleted() {
        return this.completed;
    }

    public int getProgress() {
        return this.progress;
    }

    public void incrementProgress(final ProgressBar bar, final TextView power,
                                  final Activity mainActivity, final ViewGroup ip,
                                  final ViewGroup comp, final ViewGroup contIP, final ViewGroup compCont) {
        int delay = 5000; // delay for 5 sec.
        int period = 1000; // repeat every sec.
        final Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress += 2;
                        bar.setProgress(progress);
                        Double consumption = progress * rate;
                        DecimalFormat df = new DecimalFormat("#.00");
                        power.setText(df.format(consumption) + " kW");
                        if(progress >= 100) {
                            contIP.removeView(ip);
                            compCont.addView(comp);
                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }
        }, delay, period);
    }

    public double getPowerConsumption() {
        return this.rate;
    }
}
