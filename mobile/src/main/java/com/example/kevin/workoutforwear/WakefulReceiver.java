package com.example.kevin.workoutforwear;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Kevin on 9/24/2014.
 */
public class WakefulReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Start the service, keeping the device awake while the service is
        // launching. This is the Intent to deliver to the service.
        Intent service = new Intent(context, DataService.class);
        System.out.println("Debug");
        //service.putExtra("data", intent.getStringExtra(DataListener.COPA_MESSAGE));
        service.putExtra("data", intent.getBundleExtra(DataListener.COPA_MESSAGE));
        startWakefulService(context, service);
    }
}
