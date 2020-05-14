package com.example.moodtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class calendarTab extends Fragment {

    private GridLayout calendarView;
    private TextView dateTitle;
    private ImageView backArrow;
    private ImageView forwardArrow;

    private int monthOffSet = 0;
    private int yearOffSet = 0;
    private String currentText;
    private HashMap<String, String> currentMap;

    private String DAY_INTENT = "dayIntent";
    private String SHARED_PREFS = "SharedPrefs";
    private String MOOD_KEY = "moodKey";

    public calendarTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar_tab, container, false);
        dateTitle = view.findViewById(R.id.date_title);
        calendarView = view.findViewById(R.id.calender_view);
        backArrow = view.findViewById(R.id.backArrow);
        forwardArrow = view.findViewById(R.id.fowardArrow);
        calendarView.setColumnCount(7);
        calendarView.setRowCount(5);
        currentMap = new HashMap<String, String>();


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = new Date();
                SimpleDateFormat s = new SimpleDateFormat("MM");
                if(Integer.parseInt(s.format(d))+monthOffSet == 1) {
                    monthOffSet = 12 - Integer.parseInt(s.format(d));
                    yearOffSet -= 1;
                } else {
                    monthOffSet -= 1;
                }
                setUpDate(d, s);
                calendarView.removeAllViews();
                setUpCalendar();
            }
        });

        forwardArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = new Date();
                SimpleDateFormat s = new SimpleDateFormat("MM");
                if(Integer.parseInt(s.format(d))+monthOffSet == 12) {
                    monthOffSet = -(Integer.parseInt(s.format(d)) - 1);
                    yearOffSet += 1;
                } else {
                    monthOffSet += 1;
                }
                setUpDate(d, s);
                calendarView.removeAllViews();
                setUpCalendar();
            }
        });

        setUpCalendar();
        return view;
    }

    private void setUpDate(Date d, SimpleDateFormat s) {
        final String[] months = new String[]{"January", "February",
                "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December"};
        SimpleDateFormat currentYear = new SimpleDateFormat("YYYY");
        int year = Integer.parseInt(currentYear.format(d)) + yearOffSet;
        String title = months[Integer.parseInt(s.format(d))-1+monthOffSet] + " " + year;
        dateTitle.setText(title);
    }

    private int findColor(int id) {
        int color;
        switch(id) {
            case R.id.happy_Option :
                color = getResources().getColor(R.color.happy);
                return color;
            case R.id.productive_Option :
                color = getResources().getColor(R.color.productive);
                return color;
            case R.id.sad_Option :
                color = getResources().getColor(R.color.sad);
                return color;
            case R.id.boring_Option :
                color = getResources().getColor(R.color.boring);
                return color;
            case R.id.tired_Option :
                color = getResources().getColor(R.color.tired);
                return color;
            case R.id.stressed_Option :
                color = getResources().getColor(R.color.stressed);
                return color;
            case R.id.fantastic_Option :
                color = getResources().getColor(R.color.fantastic);
                return color;
        }
        return android.R.color.transparent;
    }

    private int setColor(int day) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        if(sharedPreferences.getStringSet(MOOD_KEY, null) != null) {
            int mMonth, mYear;
            int mDay;
            Date d = new Date();
            SimpleDateFormat sM = new SimpleDateFormat("MM");
            SimpleDateFormat sY = new SimpleDateFormat("yyyy");
            for(String text : sharedPreferences.getStringSet(MOOD_KEY, null)) {
                mMonth = Integer.parseInt(text.substring(0, 2));
                mDay = Integer.parseInt(text.substring(3, 5));
                mYear = Integer.parseInt(text.substring(6, 10));
                if(mMonth == (Integer.parseInt(sM.format(d)) + monthOffSet)
                        && mYear == (Integer.parseInt(sY.format(d)) + yearOffSet)
                        && mDay == day) {
                    currentText = text;
                    return findColor(Integer.parseInt(text.substring(11)));
                }
            }
        }
        return android.R.color.transparent;
    }

    private void setUpCalendar() {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("MM");
        SimpleDateFormat y = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(y.format(d)) + yearOffSet;
        int[] daysInMonth = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        setUpDate(d, s);
        ArrayList<TextView> days = new ArrayList<TextView>();
        int length = daysInMonth[Integer.parseInt(s.format(d))+monthOffSet-1];
        if(length == 28 && (year % 4 == 0)) {
            if(year % 100 == 0 && year % 400 != 0) {
                length--;
            }
        }
        for(int i = 0; i < length; i++) {
            days.add(new TextView(getContext()));
            final String dayNum = "" + (i+1);
            days.get(i).setId(i);
            days.get(i).setText(dayNum);
            days.get(i).setPadding(42, 55, 42, 75);
            int color = setColor(Integer.parseInt(dayNum));
            currentMap.put(String.valueOf(days.get(i).getId()), currentText);
            if(color != android.R.color.transparent) {
                days.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), selectedDate.class);
                        int id = v.getId();
                        String text = currentMap.get(String.valueOf(id));
                        intent.putExtra(DAY_INTENT, text);
                        startActivity(intent);
                    }
                });
            }
            days.get(i).setBackgroundColor(color);
            calendarView.addView(days.get(i));
        }
    }
}
