package com.google.goodnote.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.goodnote.Adapters.NotesAdapter;
import com.google.goodnote.Models.NoteClass;
import com.google.goodnote.R;
import com.google.goodnote.databinding.FragmentNotesBinding;

import java.util.ArrayList;
import java.util.Objects;

public class NotesFragment extends Fragment {

    FragmentNotesBinding binding;
    ArrayList<NoteClass> notelist;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notelist = new ArrayList<>();
        NotesAdapter notesAdapter = new NotesAdapter(getContext(), notelist);
        binding.noteRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.noteRecyclerView.setAdapter(notesAdapter);

        mDatabase = FirebaseDatabase.getInstance("https://goodnote-b26ff-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mReference =
                mDatabase.getReference().child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

        mReference.child("Notes")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        notelist.clear();
                        for(DataSnapshot snapshot1: snapshot.getChildren())
                        {
                            NoteClass note = snapshot1.getValue(NoteClass.class);
                            notelist.add(note);
                        }
                        notesAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewNoteFragment.newInstance().show(getFragmentManager(),
                        AddNewNoteFragment.newInstance().getTag());
            }
        });

    }
}