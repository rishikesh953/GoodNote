package com.google.goodnote.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.goodnote.Adapters.TasksAdapter;
import com.google.goodnote.Fragments.AddNewTaskFragment;
import com.google.goodnote.Models.NewTask;
import com.google.goodnote.databinding.FragmentTasksBinding;

import java.util.ArrayList;
import java.util.Objects;

public class TasksFragment extends Fragment {

    FragmentTasksBinding binding;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ArrayList<NewTask> tasklist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTasksBinding.inflate(inflater, container, false);

        mDatabase = FirebaseDatabase.getInstance("https://goodnote-b26ff-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mReference = mDatabase.getReference();

        tasklist = new ArrayList<>();

        TasksAdapter adapter = new TasksAdapter(tasklist, getContext());

        binding.listRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listRecyclerView.setAdapter(adapter);


        mReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .child("Tasks")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        tasklist.clear();
                        for(DataSnapshot snapshot1: snapshot.getChildren())
                        {
                            NewTask newTask = snapshot1.getValue(NewTask.class);
                            tasklist.add(newTask);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        binding.plusFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTaskFragment.newInstance().show(getFragmentManager(), AddNewTaskFragment.newInstance().getTag());
            }
        });


        return binding.getRoot();
    }

}