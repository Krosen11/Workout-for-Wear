package com.example.kevin.workoutforwear;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Vector;


public class HomeScreen extends ActionBarActivity {

    private final static String FILE_START = "save_";
    public WakefulReceiver receiver;
    public PowerManager.WakeLock wl;
    public String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        final ScrollView scroll = new ScrollView(this);
        final LinearLayout linear = new LinearLayout(this);
        linear.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(linear);

        //To set wakelock
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Keep Awake");
        wl.acquire();

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String currDate = Integer.toString(today.month+1) + "/" + Integer.toString(today.monthDay) + "/" + Integer.toString(today.year);

        TextView date = new TextView(this);
        date.setText(currDate);
        date.setTextColor(Color.RED);
        linear.addView(date);

        //Now we will do I/O
        String output = "Date: " + currDate + "\n";
        //currDate = currDate.concat("_" + Integer.toString(today.hour) + "_" + Integer.toString(today.minute) + ".txt");
        currDate = currDate.replace("/", "_");
        fileName = FILE_START + currDate;

        try {
            FileOutputStream file = openFileOutput(fileName, Context.MODE_PRIVATE);
            file.write(output.getBytes());
            file.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        receiver = new WakefulReceiver();
        /*
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra(DataListener.COPA_MESSAGE);
                // do something here.
                if(s.indexOf("Exercise") != -1){
                    //This means that we have an exercise to deal with
                    System.out.println("debug");
                    TextView view = new TextView(context);
                    view.setText(s);
                    view.setTextColor(Color.BLACK);
                    linear.addView(view);

                    s = s.concat("\n");
                    try {
                        FileOutputStream file = openFileOutput(fileName, Context.MODE_APPEND);
                        file.write(s.getBytes());
                        file.close();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        InputStream input = openFileInput(fileName);
                        Scanner scan = new Scanner(input);
                        System.out.println(scan.nextLine());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else if(s.indexOf("Sets") != -1){
                    TextView view = new TextView(context);
                    view.setText(s);
                    view.setTextColor(Color.BLACK);
                    linear.addView(view);

                    s = s.concat("\n");
                    try {
                        FileOutputStream file = openFileOutput(fileName, Context.MODE_APPEND);
                        file.write(s.getBytes());
                        file.close();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(s.indexOf("Weight") != -1){
                    TextView view = new TextView(context);
                    view.setText("    " + s);
                    view.setTextColor(Color.BLACK);
                    linear.addView(view);

                    s = s.concat("\n");
                    try {
                        FileOutputStream file = openFileOutput(fileName, Context.MODE_APPEND);
                        file.write(s.getBytes());
                        file.close();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(s.indexOf("Reps") != -1){
                    TextView view = new TextView(context);
                    view.setText("    " + s);
                    view.setTextColor(Color.BLACK);
                    linear.addView(view);

                    s = s.concat("\n");
                    try {
                        FileOutputStream file = openFileOutput(fileName, Context.MODE_APPEND);
                        file.write(s.getBytes());
                        file.close();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        */

        this.setContentView(scroll);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause(){
        super.onPause();
        System.out.println("Pause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("Resume");

        if(DataListener.storedExercises.size() == 0){
            return;
        }

        for(int i = 0; i < DataListener.storedExercises.size(); i++) {
            Exercise currEx = DataListener.storedExercises.get(i);
            String exercise = currEx.getExercise() + "\n";
            String sets = currEx.getSets() + "\n";
            try {
                FileOutputStream file = openFileOutput(fileName, this.MODE_APPEND);
                file.write(exercise.getBytes());
                file.write(sets.getBytes());
                for (int j = 0; j < currEx.getReps().size(); j++) {
                    Vector<String> reps = currEx.getReps().get(Integer.toString(j + 1));
                    String cWeight = "Weight: " + reps.get(0) + "\n";
                    String cReps = "Reps: " + reps.get(1) + "\n";
                    file.write(cWeight.getBytes());
                    file.write(cReps.getBytes());
                }
                file.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        if(!DataListener.isContinue){
            //This is when the user is done working out
            String test[] = this.fileList();
            for(int i = 0; i < test.length; i++){
                System.out.println(test[i]);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(DataListener.COPA_RESULT));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        wl.release();
    }
}
