package com.example.kevin.workoutforwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SetReps extends Activity {

    public final static String NUM_SETS = "com.example.kevin.workoutforwear.SetReps.MESSAGE";
    public final static String EXERCISE_NAME = "com.example.kevin.workoutforwear.SetReps.MESSAGE2";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rect_activity_set_reps);
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
        final String message = intent.getStringExtra(HomeScreenWatch.EXERCISE_NAME);
        TextView textView = (TextView) findViewById(R.id.exercise_name);
        textView.setText(message);

        final DataSync data = DataSync.getInstance();

        Spinner spinner = (Spinner) findViewById(R.id.sets_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sets_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int index = adapterView.getSelectedItemPosition();
                String[] numSets = getResources().getStringArray(R.array.sets_list);
                String selected = numSets[index];
                if(!selected.equals("Select Number of Sets")){
                    data.setSets(selected);
                    confirmChoices(view, selected, message);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });
    }

    public void confirmChoices(View view, String sets, String exercise){
        Intent intent = new Intent(this, ConfirmExercise.class);
        intent.putExtra(NUM_SETS, sets);
        intent.putExtra(EXERCISE_NAME, exercise);
        startActivity(intent);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
}
