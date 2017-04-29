package com.example.victor.smartlivingapp;

import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    public Appliance(String type, ProgressBar bar, TextView power, Activity mainActivity,
                     LinearLayout ip, LinearLayout contIP, LinearLayout compCont,
                     LayoutInflater vi, TextView dateField, TextView inprogresstext, TextView recordtext) {
        this.type = type;
        this.completed = false;
        if(type.equals("vacuum")) rate = 0.61;
        if(type.equals("lawnmower")) rate = 0.53;
        this.progress = 0;
        contIP.addView(ip);
        incrementProgress(bar, power, mainActivity, ip, contIP, compCont, vi, dateField, inprogresstext, recordtext);
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
                                  final LayoutInflater vi, final TextView dateField,
                                  final TextView inprogresstext, final TextView recordtext) {
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
                        inprogresstext.setVisibility(View.GONE);
                        if(progress >= 100) {
                            completed = true;
                            contIP.removeView(ip);
                            recordtext.setVisibility(View.GONE);
                            if(contIP.getChildCount() == 2) {
                                inprogresstext.setVisibility(View.VISIBLE);
                            }

                            addCompletedRecord(type, vi, compCont, mainActivity, getCurrentDate());
                            saveRecord(mainActivity,"SmartVacuum" + "@" + getCurrentDate());

                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }
        }, delay, period);
    }

    private static void addCompletedRecord(String type, LayoutInflater vi, ViewGroup compCont, Context context, String date) {

        int lOrV = 0;
        LinearLayout recordview;

        if(type.equals("vacuum")) {
            recordview = (LinearLayout) vi.inflate(R.layout.record_inflate_vacuum, compCont, false);
            lOrV = R.id.completed_v_date;
        }
        else {
            recordview = (LinearLayout) vi.inflate(R.layout.record_inflate_lawnmower, compCont, false);
            lOrV = R.id.completed_l_date;
        }

        compCont.addView(recordview);
        TextView completedDateField = (TextView) recordview.findViewById(lOrV);
        completedDateField.setText(date);

    }

    private static void saveRecord(Context context, String rec) {
        FileOutputStream ops;

        try {
            ops = context.openFileOutput("records",Context.MODE_APPEND);
            ops.write(rec.getBytes());
            ops.write("\n".getBytes());

            ops.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setupExistingRecords(Context context, ViewGroup compCont, LayoutInflater vi, TextView recordText) {

        File aFile = new File(context.getFilesDir(), "records");
        int fileLength = (int)aFile.length();

        if (fileLength == 0) {
            return;
        }

        recordText.setVisibility(View.GONE);

        try {
            FileInputStream fis = new FileInputStream(aFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = br.readLine();
            TextView completedDateField;

            while (line != null) {
                String[] aRecord = line.split("@");
                String appType = aRecord[0];
                String appDate = aRecord[1];

                addCompletedRecord(appType, vi, compCont, context, appDate);


                /*if (type.equals("SmartVacuum")) {
                    recordview = (LinearLayout) vi.inflate(R.layout.record_inflate_vacuum, compCont, false);
                    completedDateField = (TextView) recordview.findViewById(R.id.completed_v_date);
                } else {
                    recordview = (LinearLayout) vi.inflate(R.layout.record_inflate_lawnmower, compCont, false);
                    completedDateField = (TextView) recordview.findViewById(R.id.completed_l_date);
                }
                compCont.addView(recordview);
                completedDateField.setText(date);*/
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearFile(File file) {

        try {
            PrintWriter pw = new PrintWriter(file);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getCurrentDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
}
