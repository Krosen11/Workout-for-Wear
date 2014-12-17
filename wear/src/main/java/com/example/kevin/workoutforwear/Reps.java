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

public class Reps extends Activity {

    public final static String NEW_SET = "com.example.kevin.workoutforwear.Reps.MESSAGE";
    public final static String TOTAL_SETS = "com.example.kevin.workoutforwear.Reps.MESSAGE2";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rect_activity_reps);
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
        final String currSet = bundle.getString(SetWeight.SET);
        String weight = bundle.getString(SetWeight.WEIGHT);
        final int totalSets = bundle.getInt(SetWeight.TOTAL_SETS);

        TextView textSet = (TextView) findViewById(R.id.current_set);
        textSet.setText("Set: " + currSet);
        TextView textWeight = (TextView) findViewById(R.id.weight);
        textWeight.setText("Weight: " + weight);

        final DataSync data = DataSync.getInstance();

        Spinner spinner = (Spinner) findViewById(R.id.rep_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.reps_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int index = adapterView.getSelectedItemPosition();
                String[] reps = getResources().getStringArray(R.array.reps_list);
                String selected = reps[index];
                if(!selected.equals("Reps Completed")){
                    data.setReps(selected);
                    nextSet(view, currSet, totalSets);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });
    }

    public void nextSet(View view, String currSet, int totalSets){
        Intent intent = new Intent(this, SetWeight.class);
        intent.putExtra(NEW_SET, currSet);
        intent.putExtra(TOTAL_SETS, totalSets);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
