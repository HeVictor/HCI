package com.example.victor.smartlivingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class represents a smart appliance controlled by the SmartLivingApp.
 * It is responsible for running the smart application for a set operation and
 * then adding the completed record to the Records screen.
 * It will also handle file saving and reading to save the user's past application runs.
 */
public class Appliance {

    private String type;
    private boolean completed;
    private int progress;
    private double rate;

    /**
     * Constructor for Appliance determines the type of appliance and the electricity consumption
     * rate. It requires an assorted collection of interface components in order to properly
     * update the "in-progress" and "completed" states of each operation.
     *
     * @param type
     * @param bar
     * @param power
     * @param mainActivity
     * @param ip
     * @param contIP
     * @param compCont
     * @param vi
     * @param dateField
     * @param inprogresstext
     * @param recordtext
     */
    public Appliance(String type, ProgressBar bar, TextView power, Activity mainActivity,
                     LinearLayout ip, LinearLayout contIP, LinearLayout compCont,
                     LayoutInflater vi, TextView dateField, TextView inprogresstext,
                     TextView recordtext) {
        this.type = type;
        this.completed = false;

        // Sets the predifine electiricty consumption rate for an appliance type.
        if(type.equals("vacuum")) rate = 0.61;
        if(type.equals("lawnmower")) rate = 0.53;
        this.progress = 0;

        // Starts the operation of running the appliance.
        contIP.addView(ip);
        incrementProgress(bar, power, mainActivity, ip, contIP, compCont, vi, dateField,
                inprogresstext, recordtext);
    }

    /**
     * Returns whether the appliance has finished running or not.
     * @return
     */
    public boolean getCompleted() {
        return this.completed;
    }

    /**
     * This method runs the application and shows a self-incrementing progress bar while it is
     * happening in the "In Progress" tab. After completion, it saves the completed details
     * internally and also adds it to be displayed at the "Records" tab.
     *
     * @param bar
     * @param power
     * @param mainActivity
     * @param ip
     * @param contIP
     * @param compCont
     * @param vi
     * @param dateField
     * @param inprogresstext
     * @param recordtext
     */
    public void incrementProgress(final ProgressBar bar, final TextView power,
                                  final Activity mainActivity, final ViewGroup ip,
                                  final ViewGroup contIP, final ViewGroup compCont,
                                  final LayoutInflater vi, final TextView dateField,
                                  final TextView inprogresstext, final TextView recordtext) {
        int delay = 1000; // delay for 5 sec.
        int period = 100; // repeat every sec.
        final Timer timer = new Timer();

        // The timer periodically increments the progress bar until the appliance finishes
        // running.
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Increment progress and show it on the progress bar.
                        progress += 1;
                        bar.setProgress(progress);

                        // Calculate energy consumption to 2 d.p., and display it.
                        Double consumption = progress * rate;
                        DecimalFormat df = new DecimalFormat("#.00");
                        power.setText(df.format(consumption) + " kW");

                        // Set the current date and time for the running applaince.
                        dateField.setText(getCurrentDate());

                        // Hide the "no appliances currently running" text in "In Progress" tab.
                        inprogresstext.setVisibility(View.GONE);

                        // When the run completes
                        if(progress >= 100) {
                            completed = true;

                            // Remove the progress display in the "In Progress" tab.
                            contIP.removeView(ip);

                            // Hide the "no appliance records" text in "Records" tab.
                            recordtext.setVisibility(View.GONE);

                            // Reveal the "no appliance running" text if there are none running
                            if(contIP.getChildCount() == 2) {
                                inprogresstext.setVisibility(View.VISIBLE);
                            }

                            // Add the completed record to "Records"
                            addCompletedRecord(type, vi, compCont, mainActivity, getCurrentDate());

                            // Save the completed record to internal storage as a line in a
                            // text file.
                            saveRecord(mainActivity,"SmartVacuum" + "@" + getCurrentDate());

                            // Terminate the timer
                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }
        }, delay, period);
    }

    /**
     * This method is used to add a completed record to the "Records" tab.
     *
     * @param type
     * @param vi
     * @param compCont
     * @param context
     * @param date
     */
    private static void addCompletedRecord(String type, LayoutInflater vi, ViewGroup compCont,
                                           Context context, String date) {

        int lOrV = 0;
        LinearLayout recordview;

        // Add the relevant appliance details depending on whether it is the Vacuum or the
        // Lawnmower
        if(type.equals("vacuum")) {
            recordview = (LinearLayout) vi.inflate(R.layout.record_inflate_vacuum, compCont, false);
            lOrV = R.id.completed_v_date;
        }
        else {
            recordview = (LinearLayout) vi.inflate(R.layout.record_inflate_lawnmower, compCont, false);
            lOrV = R.id.completed_l_date;
        }

        // Add the record and set the date.
        compCont.addView(recordview);
        TextView completedDateField = (TextView) recordview.findViewById(lOrV);
        completedDateField.setText(date);
    }

    /**
     * This method saves the record of a running operation to the internal storage.
     *
     * @param context
     * @param rec
     */
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

    /**
     *
     * This method reads from the internal storage file of the completed records and sets up the
     * records screen when a user logs in.
     *
     * @param context
     * @param compCont
     * @param vi
     * @param recordText
     */
    public static void setupExistingRecords(Context context, ViewGroup compCont, LayoutInflater vi,
                                            TextView recordText) {

        File aFile = new File(context.getFilesDir(), "records");
        int fileLength = (int)aFile.length();

        // Return if there are no records.
        if (fileLength == 0) {
            return;
        }

        recordText.setVisibility(View.GONE);

        // Read the records file line by line and add the display accordingly to the
        // "Records" tab.
        try {
            FileInputStream fis = new FileInputStream(aFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = br.readLine();

            while (line != null) {
                String[] aRecord = line.split("@");
                String appType = aRecord[0];
                String appDate = aRecord[1];

                addCompletedRecord(appType, vi, compCont, context, appDate);
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method clears a file.
     *
     * @param file
     */
    public static void clearFile(File file) {

        try {
            PrintWriter pw = new PrintWriter(file);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method gets the current date and time in a string format.
     *
     * @return
     */
    private static String getCurrentDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
}
