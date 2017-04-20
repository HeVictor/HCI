package com.example.victor.smartlivingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {

    private Button btn_logout;
    private ViewFlipper vf;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_app:
                    setTitle("Appliances");
                    vf.setDisplayedChild(0);
                    return true;
                case R.id.navigation_lifestyle:
                    setTitle("Lifestyle");
                    vf.setDisplayedChild(1);
                    return true;
                case R.id.navigation_records:
                    setTitle("Records");
                    vf.setDisplayedChild(2);
                    return true;
                case R.id.navigation_control:
                    setTitle("In Progress");
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

        btn_logout = (Button) findViewById(R.id.btn_logout);

        vf = (ViewFlipper)findViewById( R.id.viewFlipper );

        //set title
        setTitle("Appliances");
        vf.setDisplayedChild(0);

        // Code from https://www.learn2crack.com/2016/10/android-switching-between-activities-example.html
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }

}