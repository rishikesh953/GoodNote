package com.google.goodnote.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.goodnote.Models.NewTask;
import com.google.goodnote.R;
import com.google.goodnote.databinding.FragmentBottomSheetBinding;

import java.util.Objects;

public class UpdateNewTaskFragment  extends BottomSheetDialogFragment {

    FragmentBottomSheetBinding binding;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ProgressDialog progressDialog;

    private final static String TAG = "AddNewTask";

    public static AddNewTaskFragment newInstance()
    {
        return new AddNewTaskFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance("https://goodnote-b26ff-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mReference = mDatabase.getReference();
        progressDialog = new ProgressDialog(getContext());

        binding.descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (s.toString().trim().length() > 0) {
                    binding.button2.setEnabled(true);
                } else {
                    binding.button2.setEnabled(false);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() > 0) {
                    binding.button2.setEnabled(true);
                } else {
                    binding.button2.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.titleEditText.getText().toString();
                String task = binding.descriptionEditText.getText().toString();
                String date = binding.dateEditText.getText().toString();
                String time = binding.timeEditText.getText().toString();
                int priority;
                if(binding.highRadio.isChecked())
                {
                    priority = 1;
                }
                else if(binding.medRadio.isChecked())
                {
                    priority = 2;
                }
                else
                {
                    priority = 3;
                }

                Bundle myId = getArguments();
                String Id = myId.getString("Id");

                Log.v("UpdateNewTask", Id);
                Log.v("UpdateNewTask", Id);
                Log.v("UpdateNewTask", Id);
                Log.v("UpdateNewTask", Id);
                Log.v("UpdateNewTask", Id);
                Log.v("UpdateNewTask", Id);

                NewTask newTask = new NewTask(title, task, date, time, priority);
                progressDialog.setMessage("Adding task");
                progressDialog.show();
                mReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                        .child("Tasks")
                        .child(Id)
                        .setValue(newTask).addOnSuccessListener(new OnSuccessListener<Void>() {
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
