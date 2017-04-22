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
    private LinearLayout vViewGroupComplete;
    private LinearLayout lViewGroupComplete;
    private LinearLayout IPcontainer;
    private LinearLayout compContainer;

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
        vViewGroupComplete = (LinearLayout) findViewById(R.id.vacuum_complete);
        lViewGroupComplete = (LinearLayout) findViewById(R.id.lawnmower_complete);
        IPcontainer = (LinearLayout) findViewById(R.id.IP_container);
        compContainer = (LinearLayout) findViewById(R.id.comp_container);

        vacuumProgress.setScaleY(3f);
        lawnmowerProgress.setScaleY(3f);

        IPcontainer.removeView(vViewGroupIP);
        IPcontainer.removeView(lViewGroupIP);
        compContainer.removeView(vViewGroupComplete);
        compContainer.removeView(lViewGroupComplete);

        vacuumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAppliance("vacuum", vacuumProgress, vPower, vViewGroupIP, vViewGroupComplete, IPcontainer, compContainer);
            }
        });

        lawnmowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAppliance("lawnmower", lawnmowerProgress, lPower, lViewGroupIP, lViewGroupComplete, IPcontainer, compContainer);
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

    public void createAppliance(String type, ProgressBar bar, TextView power, LinearLayout ip, LinearLayout comp, LinearLayout IPcont, LinearLayout compCont) {
        Appliance newAppliance = new Appliance(type, bar, power, this, ip, comp, IPcont, compCont);
    }

    public void reinitProgress() {

    }

}