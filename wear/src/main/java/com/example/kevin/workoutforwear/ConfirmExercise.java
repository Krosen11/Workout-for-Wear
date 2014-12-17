package com.example.kevin.workoutforwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class ConfirmExercise extends Activity {

    public final static String NUM_SETS = "com.example.kevin.workoutforwear.ConfirmExercise.MESSAGE";
    private TextView mTextView;
    public String sets = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rect_activity_confirm_exercise);
        /*
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
        */

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String message = bundle.getString(SetReps.NUM_SETS);
        message = "Sets: " + message;
        String exercise = bundle.getString(SetReps.EXERCISE_NAME);
        sets = message;

        TextView textView = (TextView) findViewById(R.id.exercise_view);
        textView.setText(exercise);
        TextView textView2 = (TextView) findViewById(R.id.sets_view);
        textView2.setText(message);

        TextView confirmation = (TextView) findViewById(R.id.confirmation);
        confirmation.setText("Is this correct?");
    }

    public void buttonPress(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.confirm_yes:
                if(checked){
                    confirmed(view);
                }
                break;
            case R.id.confirm_no:
                if(checked){
                    goBack(view);
                }
                break;
        }
    }

    public void confirmed(View view){
        Intent intent = new Intent(this, SetWeight.class);
        intent.putExtra(NUM_SETS, sets);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void goBack(View view){
        //We will go back to the beginning
        Intent intent = new Intent(this, HomeScreenWatch.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
