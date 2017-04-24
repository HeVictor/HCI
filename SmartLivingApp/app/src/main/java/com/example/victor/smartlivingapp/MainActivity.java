package com.example.victor.smartlivingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.content.Context;
import java.util.*;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class MainActivity extends AppCompatActivity {

    private ViewFlipper vf;
    private Button vacuumButton;
    private Button lawnmowerButton;
    private ProgressBar vacuumProgress;
    private ProgressBar lawnmowerProgress;
    private TextView vPower;
    private TextView lPower;
    private LinearLayout vViewGroupIP;
    private LinearLayout lViewGroupIP;
    private LinearLayout IPcontainer;
    private LinearLayout compContainer;
    private LayoutInflater vi;
    private ArrayList<Appliance> vacuumList = new ArrayList<Appliance>();
    private ArrayList<Appliance> lawnmowerList = new ArrayList<Appliance>();
    private AlertDialog alertDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_app:
                    setTitle("SmartLiving");
                    vf.setDisplayedChild(0);
                    return true;
                case R.id.navigation_lifestyle:
                    setTitle("SmartLiving");
                    vf.setDisplayedChild(1);
                    return true;
                case R.id.navigation_records:
                    setTitle("SmartLiving");
                    vf.setDisplayedChild(2);
                    return true;
                case R.id.navigation_control:
                    setTitle("SmartLiving");
                    vf.setDisplayedChild(3);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        vf = (ViewFlipper)findViewById( R.id.viewFlipper );
        vacuumButton = (Button) findViewById(R.id.vacuum_button);
        lawnmowerButton = (Button) findViewById(R.id.lawnmower_button);
        vacuumProgress = (ProgressBar) findViewById(R.id.vacuum_progress);
        lawnmowerProgress = (ProgressBar) findViewById(R.id.lawnmower_progress);
        vPower= (TextView) findViewById(R.id.vacuum_power);
        lPower = (TextView) findViewById(R.id.lawnmower_power);
        vViewGroupIP = (LinearLayout) findViewById(R.id.vacuum_inprogress);
        lViewGroupIP = (LinearLayout) findViewById(R.id.lawnmower_inprogress);
        IPcontainer = (LinearLayout) findViewById(R.id.IP_container);
        compContainer = (LinearLayout) findViewById(R.id.comp_container);
        vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        vacuumProgress.setScaleY(3f);
        lawnmowerProgress.setScaleY(3f);

        IPcontainer.removeView(vViewGroupIP);
        IPcontainer.removeView(lViewGroupIP);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This appliance is already running. Please wait until it is " +
                "finished to start it again.");

        alertDialogBuilder.setPositiveButton("Okay",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();

        vacuumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vacuumList.size() == 0 || vacuumList.get(vacuumList.size() - 1).getCompleted()) {
                    createAppliance("vacuum", vacuumProgress, vPower, vViewGroupIP, IPcontainer, compContainer, vi);
                } else {
                    alertDialog.show();
                }

            }
        });

        lawnmowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lawnmowerList.size() == 0 || lawnmowerList.get(lawnmowerList.size() - 1).getCompleted()) {
                    createAppliance("lawnmower", lawnmowerProgress, lPower, lViewGroupIP, IPcontainer, compContainer, vi);
                } else {
                    alertDialog.show();
                }
            }
        });

        //set title
        setTitle("SmartLiving");
        vf.setDisplayedChild(0);

    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btn_logout) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void createAppliance(String type, ProgressBar bar, TextView power, LinearLayout ip,
                                LinearLayout IPcont, LinearLayout compCont,
                                LayoutInflater vi) {
        Appliance newAppliance = new Appliance(type, bar, power, this, ip, IPcont, compCont, vi);
        if(type.equals("vacuum")) {
            vacuumList.add(newAppliance);
        }
        if(type.equals("lawnmower")) {
            lawnmowerList.add(newAppliance);
        }
    }

}