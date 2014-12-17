package com.example.kevin.workoutforwear;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SetWeight extends Activity {

    public final static String WEIGHT = "com.example.kevin.workoutforwear.SetWeight.MESSAGE";
    public final static String SET = "com.example.kevin.workoutforwear.SetWeight.MESSAGE2";
    public final static String TOTAL_SETS = "com.example.kevin.workoutforwear.SetWeight.MESSAGE3";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rect_activity_set_weight);
        /*
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
        */

        final SavedWeight application = (SavedWeight) this.getApplication();//For saving states
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String message = bundle.getString(ConfirmExercise.NUM_SETS);

        final DataSync data = DataSync.getInstance();

        if(message == null){
            //This means we are coming from Reps.java
            final int totalSets = bundle.getInt(Reps.TOTAL_SETS);
            String currSet = bundle.getString(Reps.NEW_SET);
            int thisSet = Integer.parseInt(currSet);
            thisSet++;//This is now our current set

            if(thisSet > totalSets){
                finalView();
            }

            final String newSet = Integer.toString(thisSet);
            TextView textView = (TextView) findViewById(R.id.weight);
            textView.setText("Current Set: " + newSet);

            String weight = application.getWeight();

            final Spinner spinner = (Spinner) findViewById(R.id.weight_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.weight_list, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            spinner.setAdapter(adapter);
            /*
            final int currWeight = adapter.getPosition(weight);
            spinner.setSelection(currWeight);
            */

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int index = adapterView.getSelectedItemPosition();
                    String[] weight = getResources().getStringArray(R.array.weight_list);
                    String selected = weight[index];
                    if(!selected.equals("Select Weight")) {
                        application.setWeight(selected);
                        data.setWeight(selected);
                        numReps(view, selected, newSet, totalSets);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    //do nothing
                }
            });
        }

        else {
            int sets = Integer.parseInt(message.substring(6));
            final int totalSets = sets;

            TextView textView = (TextView) findViewById(R.id.weight);
            textView.setText("Current Set: 1");

            final Spinner spinner = (Spinner) findViewById(R.id.weight_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.weight_list, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int index = adapterView.getSelectedItemPosition();
                    String[] weight = getResources().getStringArray(R.array.weight_list);
                    String selected = weight[index];
                    if (!selected.equals("Select Weight")) {
                        application.setWeight(selected);
                        data.setWeight(selected);
                        numReps(view, selected, "1", totalSets);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    //do nothing
                }
            });
        }
    }

    public void numReps(View view, String selected, String currSet, int totalSets){
        Intent intent = new Intent(this, Reps.class);
        intent.putExtra(WEIGHT, selected);
        intent.putExtra(SET, currSet);
        intent.putExtra(TOTAL_SETS, totalSets);
        startActivity(intent);
    }

    public void finalView(){
        Intent intent = new Intent(this, FinalView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void onBackPressed(){
        finish();
        return;
    }
}
