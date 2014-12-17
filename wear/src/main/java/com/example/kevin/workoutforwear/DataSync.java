package com.example.kevin.workoutforwear;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 9/22/2014.
 */
public final class DataSync {
    private final static DataSync ourInstance = new DataSync();
    private boolean isContinue;
    private String exercise;
    private String sets;
    private ArrayList<String> weight = new ArrayList<String>();
    private ArrayList<String> reps = new ArrayList<String>();
    private GoogleApiClient mGoogleApiClient;
    private int weightCounter = 0;

    public void setGoogleApiClient(GoogleApiClient client){
        mGoogleApiClient = client;
    }

    public static DataSync getInstance() {
        return ourInstance;
    }

    private DataSync(){
        return;
    }

    public void setContinue(boolean param) { isContinue = param; }

    public void setExercise(String param){
        exercise = param;
    }

    public void setSets(String param){
        sets = param;
    }

    public void clearWeight() { weight.clear(); weightCounter = 0; }
    public void setWeight(String param){
        weight.add(param);
    }

    public void clearReps() { reps.clear(); }
    public void setReps(String param){
        String output = param + " reps completed at " + weight.get(weightCounter) + "lbs";
        reps.add(output);
        weightCounter++;
    }

    public void syncExerciseData() {
        if(mGoogleApiClient==null)
            return;

        final PutDataMapRequest putRequest = PutDataMapRequest.create("/EXERCISE");
        final DataMap map = putRequest.getDataMap();
        map.putBoolean("continue", isContinue);
        map.putString("exercise", exercise);
        map.putString("sets", sets);
        map.putStringArrayList("weight", weight);
        map.putStringArrayList("reps", reps);
        Wearable.DataApi.putDataItem(mGoogleApiClient,  putRequest.asPutDataRequest());
    }

    public void syncSampleDataItem(GoogleApiClient mGoogleApiClient) {
        if(mGoogleApiClient==null)
            return;

        final PutDataMapRequest putRequest = PutDataMapRequest.create("/SAMPLE");
        final DataMap map = putRequest.getDataMap();
        map.putInt("color", 4);
        map.putString("string_example", "Sample String3");
        Wearable.DataApi.putDataItem(mGoogleApiClient,  putRequest.asPutDataRequest());
        System.out.println(putRequest.getDataMap().getInt("color"));
        System.out.println(putRequest.getDataMap().getString("string_example"));
    }
}
