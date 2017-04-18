package com.example.victor.smartlivingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ApplicationActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Button btn_logout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_app:
                    mTextMessage.setText("Appliances");
                    return true;
                case R.id.navigation_lifestyle:
                    mTextMessage.setText("Lifestyle");
                    return true;
                case R.id.navigation_records:
                    mTextMessage.setText("Records");
                    return true;
                case R.id.navigation_control:
                    mTextMessage.setText("Control");
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //set title
        setTitle("Main menu");

        btn_logout = (Button) findViewById(R.id.btn_logout);

        // Code from https://www.learn2crack.com/2016/10/android-switching-between-activities-example.html
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }

}