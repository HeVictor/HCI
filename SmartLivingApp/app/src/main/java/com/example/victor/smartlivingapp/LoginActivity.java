package com.example.victor.smartlivingapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends Activity {
    Button btn_login_or_register, btn_back;
    EditText username_field, password_field;
    TextView notification, register_link;

    // Code below inspired by https://www.tutorialspoint.com/android/android_login_screen.htm
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login_or_register = (Button)findViewById(R.id.button);
        btn_back = (Button)findViewById(R.id.btn_back);
        username_field = (EditText)findViewById(R.id.username);
        password_field = (EditText)findViewById(R.id.password);
        notification = (TextView)findViewById(R.id.notification);
        register_link = (TextView)findViewById(R.id.register_link);

        // set starting activity version to be login
        setActivityVersion("Login");

        // Listen to back button clicks
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //transform to register version activity
                setActivityVersion("Login");
            }
        });

        // Listen to register link clicks
        register_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //transform to register version activity
                setActivityVersion("Register");

            }
        });

        // Listen to login attempts
        btn_login_or_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Handles when the user is at the login-version of the activity
                if (btn_login_or_register.getText().equals("Login")) {

                    if (username_field.getText().toString().equals("420") && password_field.getText().toString().equals("blazeit")) {
                        // correct login
                        //login_status.setText("Successful!");

                        launchMainAct();

                    } else {
                        // wrong login
                        notification.setText("WRONG!");
                    }
                    notification.setVisibility(View.VISIBLE);

                    // Handles when the user is at the register-version of the activity
                } else {

                    // Get the new user credentials
                    String new_username = username_field.getText().toString();
                    String new_password = password_field.getText().toString();

                    if (new_username.toString().isEmpty() || new_password.toString().isEmpty()) {
                        notification.setText("Username or password must not be empty!");
                        notification.setVisibility(View.VISIBLE);
                        return;
                    } else if (new File(new_username).exists()) {
                        setTitle("Username already in use!");
                    }
                    writeToFile(new_password,new_username);

                    readFromFile(new_username);


                }


            }
        });
    }

    // This function writes data to an internal storage file
    private void writeToFile(String data, String fileName) {
        try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
        }
        catch (IOException e) {
            //From StackOverflow users VSB and R9J
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void readFromFile(String fileName) {

        byte[] byteArray = new byte[100];

        try {
            FileInputStream fis = openFileInput(fileName);
            fis.read(byteArray);
            String readResult = new String(byteArray);

            setTitle(readResult);
        }
        catch (IOException e) {
            //From StackOverflow users VSB and R9J
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    // Code from https://www.learn2crack.com/2016/10/android-switching-between-activities-example.html
    private void launchMainAct() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    // Transform current activity into either login or register version.
    private void setActivityVersion(String version) {

        // Set to login version
        if (version.equals("Login")) {
            setTitle("Please login");
            btn_login_or_register.setText("Login");
            btn_back.setVisibility(View.GONE);
            register_link.setVisibility(View.VISIBLE);

            username_field.setHint("Username");
            password_field.setHint("Password");

            // Set to register version
        } else {
            setTitle("Register a new account");
            btn_login_or_register.setText("Register new account");
            btn_back.setVisibility(View.VISIBLE);
            register_link.setVisibility(View.GONE);

            username_field.setHint("Set your username");
            password_field.setHint("Set your password");
        }


    }

}