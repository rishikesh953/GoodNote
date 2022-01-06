package com.google.goodnote.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.goodnote.Adapters.CalenderTaskAdapter;
import com.google.goodnote.Fragments.AddNewTaskCalenderFragment;
import com.google.goodnote.Models.NewTask;
import com.google.goodnote.databinding.FragmentCalenderBinding;

import java.util.ArrayList;
import java.util.Objects;


public class CalenderFragment extends Fragment {

    FragmentCalenderBinding binding;

    ArrayList<NewTask> events;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalenderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        events = new ArrayList<>();
        CalenderTaskAdapter adapter = new CalenderTaskAdapter(getContext(), events);
        binding.calenderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.calenderRecyclerView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance("https://goodnote-b26ff-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mReference =
                mDatabase.getReference().child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));


        mReference.child("CalenderEvents")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        events.clear();
                        for(DataSnapshot snapshot1: snapshot.getChildren())
                        {
                            NewTask event = snapshot1.getValue(NewTask.class);
                            events.add(event);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth+" - "+(month+1) +" - "+year;
                Bundle args = new Bundle();
                args.putString("Date", date);
                DialogFragment newFragment = new AddNewTaskCalenderFragment();
                newFragment.setArguments(args);
                newFragment.show(getFragmentManager(), "TAG");
            }
        });


    }
}