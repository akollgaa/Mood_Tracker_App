package com.example.moodtracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;;import java.util.HashSet;
import java.util.Set;

public class selectedDate extends AppCompatActivity {

    private TextView mMood;
    private TextView mExplain;
    private Toolbar toolbar;
    private Button clearMood;
    private Intent intent;

    private String DAY_INTENT = "dayIntent";
    private String SHARED_PREFS = "SharedPrefs";
    private String EXPLAIN_KEY = "explainKey";
    private String MOOD_KEY = "moodKey";

    private void setToolBarDate() {
        final String[] months = new String[]{"January", "February",
                "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December"};
        String date = intent.getStringExtra(DAY_INTENT);
        String finalDate = months[Integer.parseInt(date.substring(0, 2))-1] + " " +
                date.substring(3, 5) + ", " + date.substring(6, 10);
        TextView toolBarText = findViewById(R.id.selected_date_toolbar_text);
        toolBarText.setText(finalDate);
    }

    private String getStringMood(int id) {
        switch (id) {
            case R.id.happy_Option :
                return "Happy Mood";
            case R.id.productive_Option :
                return "Productive Mood";
            case R.id.sad_Option :
                return "Sad Mood";
            case R.id.boring_Option :
                return "Boring Mood";
            case R.id.tired_Option :
                return "Tired Mood";
            case R.id.stressed_Option :
                return "Stressed Mood";
            case R.id.fantastic_Option :
                return "FANTASTIC Mood";
        }
        return "---- Mood";
    }


    private void clearCurrentMood(String moodValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> moodSet = new HashSet<String>(sharedPreferences.getStringSet(MOOD_KEY, null));
        for(String text : moodSet) {
            if(text.substring(0, 10).equals(moodValue.substring(0, 10))) {
                moodSet.remove(text);
                break;
            }
        }
        editor.clear();
        editor.putStringSet(MOOD_KEY, moodSet);
        editor.apply();
    }

    private String getExplainText(String text) {
        int start = 0;
        for(int i = 0; i < text.length(); i++) {
            if(text.charAt(i) == '|') {
                start = i+1;
                break;
            }
        }
        return text.substring(start);
    }

    private int getMoodId(String text) {
        String moodId = "";
        for(int i = 0; i < text.length(); i++) {
            if(text.charAt(i) == '|') {
                break;
            }
            moodId += text.charAt(i);
        }
        return Integer.parseInt(moodId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_date);
        mMood = findViewById(R.id.selected_mood_text);
        mExplain = findViewById(R.id.selected_explanation);
        toolbar = findViewById(R.id.selectedToolBar);
        clearMood = findViewById(R.id.clear_mood);
        intent = getIntent();
        setSupportActionBar(toolbar);
        setToolBarDate();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String text = intent.getStringExtra(DAY_INTENT).substring(11);
        // Here we set the mood text
        mMood.setText(getStringMood(getMoodId(text)));

        // Here we set the text and use the date based from before
        mExplain.setText(getExplainText(text));

        clearMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(selectedDate.this);
                alert.setTitle("Alert Message");
                alert.setMessage("Are you sure you want to delete this mood?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearCurrentMood(intent.getStringExtra(DAY_INTENT));
                        Toast.makeText(selectedDate.this, "Mood Cleared", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                alert.create().show();
            }
        });
    }
}
