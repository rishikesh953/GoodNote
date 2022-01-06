package com.google.goodnote.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.goodnote.Fragments.CalenderFragment;
import com.google.goodnote.Fragments.NotesFragment;
import com.google.goodnote.Fragments.TasksFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private String Titles[] = new String[] {"Tasks", "Notes", "Calender"};

    public MainViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new TasksFragment();
            case 1:
                return new NotesFragment();
            case 2:
                return new CalenderFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
}
