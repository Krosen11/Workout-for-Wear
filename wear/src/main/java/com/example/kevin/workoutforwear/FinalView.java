package com.example.kevin.workoutforwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class FinalView extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rect_activity_final_view);
        
        Intent intent = getIntent();

        TextView textView = (TextView) findViewById(R.id.finished);
        textView.setText("Exercise Completed");

        TextView textView1 = (TextView) findViewById(R.id.next_exercise);
        textView1.setText("Another Exercise?");
    }

    public void buttonPress(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.confirm_yes:
                if(checked){
                    nextExercise(view);
                }
                break;
            case R.id.confirm_no:
                if(checked){
                    exit(view);
                }
                break;
        }
    }

    public void exit(View view){
        DataSync data = DataSync.getInstance();
        data.setContinue(false);
        data.syncExerciseData();

        finish();
        return;
    }

    public void nextExercise(View view){
        DataSync data = DataSync.getInstance();
        data.setContinue(true);
        data.syncExerciseData();

        Intent intent = new Intent(this, HomeScreenWatch.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
