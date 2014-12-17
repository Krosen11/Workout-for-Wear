package com.example.kevin.workoutforwear;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kevin on 9/22/2014.
 */
public class DataListener extends WearableListenerService {
    static final public String COPA_RESULT = "com.example.kevin.workoutforwear.DataListener.REQUEST_PROCESSED";
    static final public String COPA_MESSAGE = "com.example.kevin.workoutforwear.DataListener.MESSAGE";

    static public Vector<Exercise> storedExercises = new Vector<Exercise>();
    static public boolean isContinue = true;

    private LocalBroadcastManager broadcaster;
    private boolean cont;

    private String exercise;
    private String sets;
    private ArrayList<String> weight = new ArrayList<String>();
    private ArrayList<String> reps = new ArrayList<String>();

    private boolean broadcast = false;
    private final static Bundle bundle = new Bundle();

    public String getExercise(){
        return exercise;
    }

    public String getSets(){
        return sets;
    }

    public ArrayList<String> getWeight(){
        return weight;
    }

    public ArrayList<String> getReps(){
        return reps;
    }

    public void sendResult(Bundle bundle) {
        Intent intent = new Intent(COPA_RESULT);
        if(bundle != null)
            intent.putExtra(COPA_MESSAGE, bundle);
        broadcaster.sendBroadcast(intent);
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    //Source: http://stackoverflow.com/questions/25196033/android-wear-data-items
    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        System.out.println("1");
        if(!broadcast) {
            broadcaster = LocalBroadcastManager.getInstance(this);
            broadcast = true;
        }

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);

        for(DataEvent event : events) {
            System.out.println("2");
            final Uri uri = event.getDataItem().getUri();
            final String path = uri!=null ? uri.getPath() : null;
            if("/EXERCISE".equals(path)){
                final DataMap map = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                cont = map.getBoolean("continue");
                isContinue = cont;
                exercise = map.getString("exercise");
                sets = map.getString("sets");
                weight = map.getStringArrayList("weight");
                reps = map.getStringArrayList("reps");

                Bundle localBundle = new Bundle();
                localBundle.putString("exercise", exercise);
                localBundle.putString("sets", sets);
                localBundle.putStringArrayList("weight", weight);
                localBundle.putStringArrayList("reps", reps);

                Exercise test = new Exercise(exercise, sets, weight, reps);
                storedExercises.add(test);

                int currSize = bundle.size();
                String temp = "Bundle " + Integer.toString(currSize);
                bundle.putBundle(temp, localBundle);

                sendResult(bundle);
            }
        }
    }
}
