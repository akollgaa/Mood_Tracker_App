package com.example.moodtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class basicTab extends Fragment {

    private TextView happyText;
    private TextView productiveText;
    private TextView sadText;
    private TextView boringText;
    private TextView tiredText;
    private TextView stressedText;
    private TextView fantasticText;

    private String SHARED_PREFS = "SharedPrefs";
    private String MOOD_KEY = "moodKey";

    public basicTab() {
        // Required empty public constructor
    }

    private int checkOptions(String option) {
        switch(Integer.parseInt(option)) {
            case R.id.happy_Option :
                return 0;
            case R.id.productive_Option :
                return 1;
            case R.id.sad_Option :
                return 2;
            case R.id.boring_Option :
                return 3;
            case R.id.tired_Option :
                return 4;
            case R.id.stressed_Option :
                return 5;
            case R.id.fantastic_Option :
                return 6;
        }
        return -1;
    }

    private int[] assignMoodValues() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        int[] moodValues = new int[7];
        if(sharedPreferences.getStringSet(MOOD_KEY, null) != null) {
            // This iterates over every mood tracked in shared Preferences.
            for(String text : sharedPreferences.getStringSet(MOOD_KEY, null)) {
                // Here we find the mood Id which is part of the String in the set.
                text = text.substring(11);
                String moodId = "";
                for(int i = 0; i < text.length(); i++) {
                    if(text.charAt(i) == '|') {
                        break;
                    }
                    moodId += text.charAt(i);
                }
                // Here we use the id to add it to the array using checkOptions
                if(checkOptions(moodId) != -1) {
                    moodValues[checkOptions(moodId)]++;
                }
            }
        }

        return moodValues;
    }

    private void uploadMoodValues(int[] moodValues) {
        String hText = "Happy count: " + moodValues[0];
        happyText.setText(hText);
        String pText = "Productive count: " + moodValues[1];
        productiveText.setText(pText);
        String sText = "Sad count: " + moodValues[2];
        sadText.setText(sText);
        String bText = "Boring count: " + moodValues[3];
        boringText.setText(bText);
        String tText = "Tired count: " + moodValues[4];
        tiredText.setText(tText);
        String stText = "Stressed count: " + moodValues[5];
        stressedText.setText(stText);
        String fText = "FANTASTIC count: " + moodValues[6];
        fantasticText.setText(fText);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basic_tab, container, false);
        happyText = view.findViewById(R.id.happy_text);
        productiveText = view.findViewById(R.id.productive_text);
        sadText = view.findViewById(R.id.sad_text);
        boringText = view.findViewById(R.id.boring_text);
        tiredText = view.findViewById(R.id.tired_text);
        stressedText = view.findViewById(R.id.stressed_text);
        fantasticText = view.findViewById(R.id.fantastic_text);

        // Here we assign and get all moodValues

        int[] moodValues = assignMoodValues();
        uploadMoodValues(moodValues);

        return view;
    }
}
