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
import android.widget.ViewFlipper;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.content.Context;
import java.io.File;
import java.util.*;
import android.content.DialogInterface;


public class MainActivity extends AppCompatActivity {

    private ViewFlipper vf;
    private ArrayList<Appliance> vacuumList = new ArrayList<Appliance>();
    private ArrayList<Appliance> lawnmowerList = new ArrayList<Appliance>();

    //elements in appliances view
    private Button vacuumButton;
    private Button lawnmowerButton;
    private AlertDialog alertDialog;

    //elements in lifestyle view
    private ListView diet;
    private ListView fitness;
    private String[] dietOptions = {"Roast Beef and Horseradish Cream on Pear",
            "Beet Chips With Curried Yogurt", "Sweet Potato Fries With Chipotle Yogurt"};
    private String[] fitnessOptions = {"Ab wheel rollout", "Front squat", "Romanian Deadlift"};

    //elements in records view
    private Button clearButton;
    private TextView recordtext;
    private LinearLayout compContainer;
    private LayoutInflater vi;

    //elements in in-progress view
    private TextView inprogresstext;
    private ProgressBar vacuumProgress;
    private ProgressBar lawnmowerProgress;
    private TextView vPower;
    private TextView lPower;
    private TextView vDate;
    private TextView lDate;
    private LinearLayout IPcontainer;
    private LinearLayout vViewGroupIP;
    private LinearLayout lViewGroupIP;

    //elements in control view
    private TextView directionControl;
    private Button leftButton;
    private Button rightButton;
    private Button upButton;
    private Button downButton;
    private Button backButton;

    //bottom navigation menu listener
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

        //initialize bottom navbar
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //viewflipper to view different views in navbar
        vf = (ViewFlipper)findViewById( R.id.viewFlipper );

        //elements in appliances view
        vacuumButton = (Button) findViewById(R.id.vacuum_button);
        lawnmowerButton = (Button) findViewById(R.id.lawnmower_button);

        //elements in lifestyle view
        diet = (ListView) findViewById(R.id.diet_list);
        fitness = (ListView)findViewById(R.id.fitness_list);
        ArrayAdapter dietAdapter = new ArrayAdapter<String>(this,R.layout.listview_settings,dietOptions);
        diet.setAdapter(dietAdapter);
        ArrayAdapter fitnessAdapter = new ArrayAdapter<String>(this,R.layout.listview_settings,fitnessOptions);
        fitness.setAdapter(fitnessAdapter);

        //elements in records view
        clearButton = (Button) findViewById(R.id.clear_button);
        recordtext = (TextView) findViewById(R.id.recordtext);
        compContainer = (LinearLayout) findViewById(R.id.comp_container);
        vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //elements in in-progress view
        vacuumProgress = (ProgressBar) findViewById(R.id.vacuum_progress);
        lawnmowerProgress = (ProgressBar) findViewById(R.id.lawnmower_progress);
        vPower= (TextView) findViewById(R.id.vacuum_power);
        lPower = (TextView) findViewById(R.id.lawnmower_power);
        vDate = (TextView) findViewById(R.id.vacuum_date);
        lDate = (TextView) findViewById(R.id.lawnmower_date);
        inprogresstext = (TextView) findViewById(R.id.inprogresstext);
        vViewGroupIP = (LinearLayout) findViewById(R.id.vacuum_inprogress);
        lViewGroupIP = (LinearLayout) findViewById(R.id.lawnmower_inprogress);
        IPcontainer = (LinearLayout) findViewById(R.id.IP_container);

        //elements in control viewe
        leftButton = (Button) findViewById(R.id.left_button);
        rightButton = (Button) findViewById(R.id.right_button);
        downButton = (Button) findViewById(R.id.down_button);
        upButton = (Button) findViewById(R.id.up_button);
        directionControl = (TextView) findViewById(R.id.direction_control);
        backButton = (Button) findViewById(R.id.back_button);


        //make progress bars larger
        vacuumProgress.setScaleY(3f);
        lawnmowerProgress.setScaleY(3f);

        //Initially remove in-progress views since no appliances will be running when app is launched
        IPcontainer.removeView(vViewGroupIP);
        IPcontainer.removeView(lViewGroupIP);

        //remember records from previous login
        Appliance.setupExistingRecords(this,compContainer,vi,recordtext);

        //set title
        setTitle("SmartLiving");
        vf.setDisplayedChild(0);
        directionControl.setText("Moving Forward");

        //diet listview listener
        diet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) diet.getItemAtPosition(position);
                setupLifestyleDialog(selected,"diet");

            }
        });

        //fitness listview listener
        fitness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) fitness.getItemAtPosition(position);
                setupLifestyleDialog(selected,"fitness");

            }
        });

        //clear button listener
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setTitle("Clearing all records");
                dialogBuilder.setMessage("Are you sure you want to clear all existing smart appliance records?");
                dialogBuilder.setNegativeButton("No", null);
                dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface di, int selection) {
                        Appliance.clearFile(new File(MainActivity.this.getFilesDir(), "records"));
                        compContainer.removeAllViews();
                        recordtext.setVisibility(View.VISIBLE);
                    }
                });

                dialogBuilder.show();

            }
        });

        //back button listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vf.setDisplayedChild(3);
                navigation.setVisibility(View.VISIBLE);
            }
        });

        //in-progress listener to display control screen
        vViewGroupIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vf.setDisplayedChild(4);
                navigation.setVisibility(View.GONE);
            }
        });

        //in-progress listener to display control screen
        lViewGroupIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vf.setDisplayedChild(4);
                navigation.setVisibility(View.GONE);
            }
        });

        //alert dialogue that shows if an appliance is already running
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This appliance is already running. Please wait until it is " +
                "finished to start it again.");

        //set okay button
        alertDialogBuilder.setPositiveButton("Okay",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        //create the dialogue
        alertDialog = alertDialogBuilder.create();

        //vacuum button listener
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

        //lawnmower button listener
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

        //control view left button listener
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionControl.setText("Moving Left");
            }
        });

        //control view right button listener
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionControl.setText("Moving Right");
            }
        });

        //control view forward button listener
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionControl.setText("Moving Forward");
            }
        });

        //control view backwards button listener
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionControl.setText("Moving Backward");
            }
        });

    }
    //end of onCreate method

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

    //method is called when vacuum or lawnmower button is pushed in appliance tab
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

    // This method gets the viewflipper for the app
    public ViewFlipper getViewFlipper() {
        return vf;
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
            case "Ab wheel rollout": info = "Kneel on the floor and hold an ab wheel beneath your shoulders. " +
                    "Brace your abs and roll the wheel forward until you feel you’re about to lose tension in your core and your " +
                    "hips might sag. Roll yourself back to start. Do as many reps as you can with perfect form and end the set when you " +
                    "think you might break form.\n\n" +
                    "From mensfitness.com";
                break;
            case "Front squat": info = "Set a barbell on a power rack at about shoulder height. Grab the power with an overhand grip at shoulder " +
                    "width and raise your elbows until your upper arms are parallel to the floor. Take the bar out of the rack and let it rest on your " +
                    "fingertips. Your elbows should be all the way up throughout the movement. Step back and set your feet at shoulder width with toes " +
                    "turned out slightly. Squat as low as you can without losing the arch in your lower back.\n\n" +
                    "From mensfitness.com";
                break;
            case "Romanian Deadlift": info = "A killer deadlift variation, hold a barbell with a shoulder-width grip and stand with feet hip-width apart. " +
                    "Bend your hips back as far as you can. Allow your knees to bend as needed while you lower the bar along your shins until you feel a " +
                    "stretch in your hamstrings. Keep your lower back in its natural arched position throughout.\n\n" +
                    "From mensfitness.com";
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("Your " + fitnessOrDiet + " details:");
        dialogBuilder.setMessage(info);
        dialogBuilder.setPositiveButton("Ok",null);
        dialogBuilder.show();
    }

}