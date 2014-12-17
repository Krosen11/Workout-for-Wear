package com.example.kevin.workoutforwear;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Kevin on 9/24/2014.
 */
public class DataService extends IntentService {

    public DataService() {
        super("DataService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        //Bundle extras = intent.getExtras();
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String currDate = Integer.toString(today.month+1) + "/" + Integer.toString(today.monthDay) + "/" + Integer.toString(today.year);
        //currDate = currDate.concat("_" + Integer.toString(today.hour) + "_" + Integer.toString(today.minute) + ".txt");
        currDate = currDate.replace("/", "_");
        final String fileName = "save_" + currDate;

        Bundle bundle = intent.getBundleExtra("data");
        for (int i = 0; i < bundle.size(); i++){
            String current = "Bundle " + Integer.toString(i);
            Bundle localBundle = bundle.getBundle(current);
            String exercise = "Exercise: " + localBundle.getString("exercise") + "\n";
            String sets = "Sets: " + localBundle.getString("sets") + "\n";
            ArrayList<String> weight = localBundle.getStringArrayList("weight");
            ArrayList<String> reps = localBundle.getStringArrayList("reps");
            System.out.println(exercise);

            try {
                FileOutputStream file = openFileOutput(fileName, this.MODE_APPEND);
                file.write(exercise.getBytes());
                file.write(sets.getBytes());
                for(int j = 0; j < weight.size(); j++){
                    String cWeight = "Weight: " + weight.get(j) + "\n";
                    String cReps = "Reps: " + reps.get(j) + "\n";
                    file.write(cWeight.getBytes());
                    file.write(cReps.getBytes());
                }
                file.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        WakefulReceiver.completeWakefulIntent(intent);
    }
}
