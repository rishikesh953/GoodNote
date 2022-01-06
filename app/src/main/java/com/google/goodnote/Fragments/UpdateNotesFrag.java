package com.google.goodnote.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.goodnote.Models.NoteClass;
import com.google.goodnote.R;
import com.google.goodnote.databinding.FragmentBottomSheetNoteBinding;

import java.util.Objects;

public class UpdateNotesFrag  extends BottomSheetDialogFragment {

    FragmentBottomSheetNoteBinding binding;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ProgressDialog progressDialog;

    private final static String TAG = "AddNewTask";

    public static AddNewNoteFragment newInstance()
    {
        return new AddNewNoteFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBottomSheetNoteBinding.inflate(inflater, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance("https://goodnote-b26ff-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mReference = mDatabase.getReference();
        progressDialog = new ProgressDialog(getContext());

        binding.noteContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (s.toString().trim().length() > 0) {
                    binding.uploadFb.setEnabled(true);
                } else {
                    binding.uploadFb.setEnabled(false);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() > 0) {
                    binding.uploadFb.setEnabled(true);
                } else {
                    binding.uploadFb.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Bundle args = getArguments();
        String Id = args.getString("Id");

        binding.uploadFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.noteTitleEditText.getText().toString();
                String content = binding.noteContentEditText.getText().toString();

                NoteClass newNote = new NoteClass(content, title);
                progressDialog.setMessage("Uploading Note");
                progressDialog.show();
                mReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                        .child("Notes")
                        .child(Id)
                        .setValue(newNote).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        progressDialog.dismiss();
                        dismiss();
                    }
                });

            }
        });


    }

}
