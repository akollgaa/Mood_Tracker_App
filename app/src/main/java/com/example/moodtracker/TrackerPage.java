package com.example.moodtracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class TrackerPage extends Fragment {

    private TextView mDateText;
    private TextView mReTrackText;
    private EditText mExplainText;
    private Button mSaveButton;
    private RadioGroup mMoodOption;

    private String SHARED_PREFS = "SharedPrefs";
    private String EXPLAIN_KEY = "explainKey";
    private String MOOD_KEY = "moodKey";

    public TrackerPage() {
        // Required empty public constructor
    }

    private boolean isTrackedToday() {
        Date d = new Date();
        SimpleDateFormat date = new SimpleDateFormat("MM:dd:yyyy");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        if(sharedPreferences.getStringSet(MOOD_KEY, null) != null) {
            String setDate;
            for(String text : sharedPreferences.getStringSet(MOOD_KEY, null)) {
                setDate = "";
                for(int i = 0; i < text.length(); i++) {
                    if(text.charAt(i) == '|') {
                        break;
                    }
                    setDate += text.charAt(i);
                }
                if(setDate.equals(date.format(d))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_tracker_page, container, false);
        mDateText = view.findViewById(R.id.current_date_text);
        mReTrackText = view.findViewById(R.id.reTrack_text);
        mExplainText = view.findViewById(R.id.explanation_text);
        mSaveButton = view.findViewById(R.id.save_button);
        mMoodOption = view.findViewById(R.id.radioGroup);
        // Here we get the current month
        Date d = new Date();
        final String[] months = new String[]{"January", "February",
                "March", "April", "May", "June", "July", "August",
                "September", "November", "October", "December"};
        SimpleDateFormat month = new SimpleDateFormat("MM");
        String currentMonth = months[Integer.parseInt(month.format(d))-1];
        SimpleDateFormat date = new SimpleDateFormat(" dd, yyyy");
        String finalDate = currentMonth + date.format(d);
        mDateText.setText(finalDate);

        // Here we check if we have already saved something from today.
        if(isTrackedToday()) {
            mReTrackText.setVisibility(View.VISIBLE);
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

                if(mMoodOption.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getContext(), "Please Select A Mood", Toast.LENGTH_SHORT).show();
                    Set<String> set = sharedPreferences.getStringSet(MOOD_KEY, null);
                    if(set != null) {
                        for(String text : set) {
                            Log.d("days", text);
                        }
                    }
                    return;
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                // = Here we edit the explain text so it can be accessed later through the date.
                Date d = new Date();
                SimpleDateFormat date = new SimpleDateFormat("MM:dd:yyyy");
                String explainText = date.format(d) + "|" + mExplainText.getText().toString();

                // Here we add the mood option to the moodKey
                Set<String> moodSet;
                if(sharedPreferences.getStringSet(MOOD_KEY, null) == null) {
                    moodSet = new HashSet<String>();
                    String moodText = date.format(d) + "|" + mMoodOption.getCheckedRadioButtonId();
                    moodSet.add(moodText);
                } else {
                    moodSet = sharedPreferences.getStringSet(MOOD_KEY, null);
                    String moodText = date.format(d) + "|" + mMoodOption.getCheckedRadioButtonId();
                    for(String text : moodSet) {
                        String word = "";
                        for(int i = 0; i < text.length(); i++) {
                            if(text.charAt(i) == '|') {
                                break;
                            }
                            word += text.charAt(i);
                        }
                        if(word.equals(date.format(d))) {
                            moodSet.remove(text);
                        }
                    }
                    moodSet.add(moodText);
                }
                editor.remove(MOOD_KEY);
                editor.putStringSet(MOOD_KEY, moodSet);
                editor.apply();

                // Here we add the explain text to the shared preference through a hashSet
                if(sharedPreferences.getStringSet(EXPLAIN_KEY,null) == null) {
                    Set<String> explainSet = new HashSet<String>();
                    explainSet.add(explainText);
                    editor.remove(EXPLAIN_KEY);
                    editor.putStringSet(EXPLAIN_KEY, explainSet);
                    editor.apply();
                } else {
                    Set<String> explainSet = sharedPreferences.getStringSet(EXPLAIN_KEY, null);
                    // This checks if the explain text is already saved
                    assert explainSet != null;
                    if(!explainSet.contains(explainText)) {
                        String explainDate = "";
                        // We check again to see if explainSet contains something from today
                        // If so then we delete it and add the new text
                        for(int i = 0; i < explainText.length(); i++) {
                            if(explainText.charAt(i) == '|') {
                                break;
                            }
                            explainDate += explainText.charAt(i);
                        }
                        for(String text : explainSet) {
                            String setDate = "";
                            for(int i = 0; i < text.length(); i++) {
                                if(text.charAt(i) == '|') {
                                    break;
                                }
                                setDate += text.charAt(i);
                            }
                            if(setDate.equals(explainDate)) {
                                explainSet.remove(text);
                                break;
                            }
                        }
                        explainSet.add(explainText);
                        editor.remove(EXPLAIN_KEY);
                        editor.putStringSet(EXPLAIN_KEY, explainSet);
                        editor.apply();
                    }
                }
                Toast.makeText(getContext(), "Tracked!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
