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

    private String getIdMood(int id) {
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

    private String getExplainText(String date) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if(sharedPreferences.getStringSet(EXPLAIN_KEY, null) != null) {
            for(String text : sharedPreferences.getStringSet(EXPLAIN_KEY, null)) {
                if(date.equals(text.substring(0, 10))) {
                    return text.substring(11);
                }
            }

        }
        return "Nothing saved.";
    }

    private void clearCurrentMood() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Here we clear the mood
        if(sharedPreferences.getStringSet(MOOD_KEY, null) != null) {
            Set<String> set = new HashSet<String>();
            set.addAll(sharedPreferences.getStringSet(MOOD_KEY, null));
            for(String mood : sharedPreferences.getStringSet(MOOD_KEY, null)) {
                if(mood.substring(0, 11).equals(intent.getStringExtra(DAY_INTENT).substring(0, 11))) {
                    set.remove(mood);
                    break;
                }
            }
            editor.putStringSet(MOOD_KEY, set);
            editor.apply();
        }

        // Here we clear the explain text
        if(sharedPreferences.getStringSet(EXPLAIN_KEY, null) != null) {
            Set<String> set = new HashSet<String>();
            set.addAll(sharedPreferences.getStringSet(EXPLAIN_KEY, null));
            for(String mood : sharedPreferences.getStringSet(EXPLAIN_KEY, null)) {
                if(mood.substring(0, 11).equals(intent.getStringExtra(DAY_INTENT).substring(0, 11))) {
                    set.remove(mood);
                    break;
                }
            }
            editor.putStringSet(EXPLAIN_KEY, set);
            editor.apply();
        }
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

        // Here we set the mood text
        String mood = intent.getStringExtra(DAY_INTENT);
        int id = Integer.parseInt(mood.substring(11));
        mMood.setText(getIdMood(id));

        // Here we set the text and use the date based from before
        String date = mood;
        mExplain.setText(getExplainText(date.substring(0, 10)));

        clearMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(selectedDate.this);
                alert.setTitle("Alert Message");
                alert.setMessage("Are you sure you want to delete this mood?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearCurrentMood();
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
