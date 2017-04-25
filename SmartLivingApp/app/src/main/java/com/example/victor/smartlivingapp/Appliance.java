package com.example.victor.smartlivingapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Activity;
import java.text.DecimalFormat;
import android.widget.LinearLayout;


import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.*;

public class Appliance {

    private String type;
    private boolean completed;
    private int progress;
    private double rate;
    private LinearLayout recordview;

    public Appliance(String type, ProgressBar bar, TextView power, Activity mainActivity,
                     LinearLayout ip, LinearLayout contIP, LinearLayout compCont,
                     LayoutInflater vi, TextView dateField) {
        this.type = type;
        this.completed = false;
        if(type.equals("vacuum")) rate = 0.61;
        if(type.equals("lawnmower")) rate = 0.53;
        this.progress = 0;
        contIP.addView(ip);
        incrementProgress(bar, power, mainActivity, ip, contIP, compCont, vi, dateField);
    }

    public boolean getCompleted() {
        return this.completed;
    }

    public int getProgress() {
        return this.progress;
    }

    public void incrementProgress(final ProgressBar bar, final TextView power,
                                  final Activity mainActivity, final ViewGroup ip,
                                  final ViewGroup contIP, final ViewGroup compCont,
                                  final LayoutInflater vi, final TextView dateField) {
        int delay = 1000; // delay for 5 sec.
        int period = 100; // repeat every sec.
        final Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress += 1;
                        bar.setProgress(progress);
                        Double consumption = progress * rate;
                        DecimalFormat df = new DecimalFormat("#.00");
                        power.setText(df.format(consumption) + " kW");
                        dateField.setText(getCurrentDate());
                        if(progress >= 100) {
                            completed = true;
                            contIP.removeView(ip);
                            int lOrV = 0;
                            if(type.equals("vacuum")) {
                                recordview = (LinearLayout) vi.inflate(R.layout.record_inflate_vacuum, compCont, false);
                                lOrV = R.id.completed_v_date;
                            }
                            if(type.equals("lawnmower")) {
                                recordview = (LinearLayout) vi.inflate(R.layout.record_inflate_lawnmower, compCont, false);
                                lOrV = R.id.completed_l_date;
                            }
                            compCont.addView(recordview);
                            TextView completedDateField = (TextView) mainActivity.findViewById(lOrV);
                            completedDateField.setText(getCurrentDate());
                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }
        }, delay, period);
    }

    public static String getCurrentDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
}
