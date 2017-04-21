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

public class MainActivity extends AppCompatActivity {

    private ViewFlipper vf;
    private Button vacuumButton;
    private Button lawnmowerButton;
    private ProgressBar vacuumProgress;
    private ProgressBar lawnmowerProgress;

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

        vacuumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAppliance("vacuum", vacuumProgress);
            }
        });

        lawnmowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAppliance("lawnmower", lawnmowerProgress);
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

    public void createAppliance(String type, ProgressBar bar) {
        Appliance newAppliance = new Appliance(type, bar);
    }

}