package com.example.victor.smartlivingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.content.Context;
import java.util.*;
//import android.app.AlertDialog;
import android.content.DialogInterface;


public class MainActivity extends AppCompatActivity {

    private ViewFlipper vf;
    private Button vacuumButton;
    private Button lawnmowerButton;
    private Button leftButton;
    private Button rightButton;
    private Button upButton;
    private Button downButton;
    private Button backButton;
    private TextView directionControl;
    private ProgressBar vacuumProgress;
    private ProgressBar lawnmowerProgress;
    private TextView vPower;
    private TextView lPower;
    private TextView vDate;
    private TextView lDate;
    private TextView inprogresstext;
    private TextView recordtext;

    private ListView diet;
    private ListView fitness;
    private String[] dietOptions = {"Roast Beef and Horseradish Cream on Pear","Beet Chips With Curried Yogurt", "Sweet Potato Fries With Chipotle Yogurt"};
    private String[] fitnessOptions = {"Skip leg day", "Tip fedora"};
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
                    vf.setDisplayedChild(0);
                    return true;
                case R.id.navigation_lifestyle:
                    vf.setDisplayedChild(1);
                    return true;
                case R.id.navigation_records:
                    vf.setDisplayedChild(2);
                    return true;
                case R.id.navigation_control:
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
        vDate = (TextView) findViewById(R.id.vacuum_date);
        lDate = (TextView) findViewById(R.id.lawnmower_date);
        inprogresstext = (TextView) findViewById(R.id.inprogresstext);
        recordtext = (TextView) findViewById(R.id.recordtext);

        diet = (ListView) findViewById(R.id.diet_list);
        fitness = (ListView)findViewById(R.id.fitness_list);
        leftButton = (Button) findViewById(R.id.left_button);
        rightButton = (Button) findViewById(R.id.right_button);
        downButton = (Button) findViewById(R.id.down_button);
        upButton = (Button) findViewById(R.id.up_button);
        directionControl = (TextView) findViewById(R.id.direction_control);
        backButton = (Button) findViewById(R.id.back_button);

        ArrayAdapter dietAdapter = new ArrayAdapter<String>(this,R.layout.listview_settings,dietOptions);
        diet.setAdapter(dietAdapter);
        ArrayAdapter fitnessAdapter = new ArrayAdapter<String>(this,R.layout.listview_settings,fitnessOptions);
        fitness.setAdapter(fitnessAdapter);

        diet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selected = (String) diet.getItemAtPosition(position);

                setupLifestyleDialog(selected,"diet");

            }
        });

        fitness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selected = (String) fitness.getItemAtPosition(position);

                setupLifestyleDialog(selected,"fitness");

            }
        });

        vViewGroupIP = (LinearLayout) findViewById(R.id.vacuum_inprogress);
        lViewGroupIP = (LinearLayout) findViewById(R.id.lawnmower_inprogress);
        IPcontainer = (LinearLayout) findViewById(R.id.IP_container);
        compContainer = (LinearLayout) findViewById(R.id.comp_container);
        vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        vacuumProgress.setScaleY(3f);
        lawnmowerProgress.setScaleY(3f);

        IPcontainer.removeView(vViewGroupIP);
        IPcontainer.removeView(lViewGroupIP);

        //inprogresstext.setText(IPcontainer.getChildCount());

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
                    createAppliance("vacuum", vacuumProgress, vPower, vViewGroupIP, IPcontainer,
                            compContainer, vi, vDate, inprogresstext, recordtext);
                } else {
                    alertDialog.show();
                }

            }
        });


        lawnmowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lawnmowerList.size() == 0 || lawnmowerList.get(lawnmowerList.size() - 1).getCompleted()) {
                    createAppliance("lawnmower", lawnmowerProgress, lPower, lViewGroupIP, IPcontainer,
                            compContainer, vi, lDate, inprogresstext, recordtext);
                } else {
                    alertDialog.show();
                }
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionControl.setText("Moving Left");
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionControl.setText("Moving Right");
            }
        });

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionControl.setText("Moving Forward");
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionControl.setText("Moving Backward");
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vf.setDisplayedChild(3);
            }
        });

        //set title
        setTitle("SmartLiving");
        vf.setDisplayedChild(0);
        directionControl.setText("Moving Forward");

        vViewGroupIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vf.setDisplayedChild(4);
            }
        });

        lViewGroupIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vf.setDisplayedChild(4);
            }
        });

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
                                LayoutInflater vi, TextView dateField, TextView inprogresstext, TextView recordtext) {
        Appliance newAppliance = new Appliance(type, bar, power, this, ip, IPcont, compCont, vi, dateField, inprogresstext, recordtext);
        if(type.equals("vacuum")) {
            vacuumList.add(newAppliance);
        }
        else if(type.equals("lawnmower")) {
            lawnmowerList.add(newAppliance);
        }
    }

    // This method sets up a popup dialog box to display details about their selected lifestyle option
    private void setupLifestyleDialog(String selected, String fitnessOrDiet) {

        String info = "";

        switch (selected) {
            // The diet details
            case "Roast Beef and Horseradish Cream on Pear": info = "Mix together 1 tablespoon low-fat sour cream and 1 " +
                    "teaspoon prepared horseradish. Dividing evenly, top ½ sliced pear with 3 slices deli roast beef, " +
                    "the horseradish cream, and fresh herbs (such as chervil, parsley, or tarragon).\n\n" +
                    "From realsimple.com";
                break;
            case "Beet Chips With Curried Yogurt": info = "Mix together 2 tablespoons plain low-fat Greek yogurt and " +
                    "⅛ to ¼ teaspoon curry powder. Serve with 1 cup beet chips.\n\n" +
                    "From realsimple.com";
                break;
            case "Sweet Potato Fries With Chipotle Yogurt": info = "Cook 14 frozen sweet potato fries according to " +
                    "the package directions. Mix together 2 tablespoons plain low-fat yogurt and ½ teaspoon chipotles in " +
                    "adobo sauce and serve for dipping.\n\n" +
                    "From realsimple.com";
                break;

            // The fitness details
            case "Skip leg day": info = "You really shouldn't.";
                break;
            case "Tip fedora": info = "M'lady.";
                break;
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("Your " + fitnessOrDiet + " details:");
        dialogBuilder.setMessage(info);
        dialogBuilder.setPositiveButton("Ok",null);
        dialogBuilder.show();
    }

}