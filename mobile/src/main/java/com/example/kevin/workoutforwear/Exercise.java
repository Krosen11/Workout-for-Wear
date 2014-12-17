package com.example.kevin.workoutforwear;

import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Kevin on 9/24/2014.
 */
public class Exercise {
    private String exercise;
    private String sets;
    private HashMap<String,Vector<String>> reps = new HashMap<String, Vector<String>>();

    public Exercise(String name, String totalSets, ArrayList<String> weight, ArrayList<String> rep){
        exercise = name;
        sets = totalSets;
        int numSets = new Integer(sets);
        for (int i = 0; i < numSets; i++){
            String currSet = Integer.toString(i+1);
            Vector<String> newVec = new Vector<String>();
            newVec.add(weight.get(i));
            newVec.add(rep.get(i));
            reps.put(currSet, newVec);
        }
    }

    public String getExercise(){
        return exercise;
    }

    public String getSets(){
        return sets;
    }

    public HashMap<String,Vector<String>> getReps(){
        return reps;
    }
}
