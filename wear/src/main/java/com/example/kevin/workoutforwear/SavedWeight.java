package com.example.kevin.workoutforwear;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kevin on 9/22/2014.
 */
public class SavedWeight extends Application{
    public String weight;
    public boolean selectedWeight = false;

    public String getWeight(){
        return weight;
    }

    public boolean toggleBoolean(){
        if(selectedWeight){
            selectedWeight = false;
        }
        else{
            selectedWeight = true;
        }

        return selectedWeight;
    }

    public void setWeight(String param){
        weight = param;
    }
}
