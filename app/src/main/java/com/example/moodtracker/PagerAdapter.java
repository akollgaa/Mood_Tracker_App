package com.example.moodtracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public PagerAdapter(@NonNull FragmentManager fm, int tabCount) {
        super(fm);
        numberOfTabs = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0 :
                return new basicTab();
            case 1 :
                return new calendarTab();
            default :
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
