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
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.goodnote.Models.NewTask;
import com.google.goodnote.R;
import com.google.goodnote.databinding.CalenderDialogFragmentBinding;

import java.util.Objects;

public class AddNewTaskCalenderFragment extends DialogFragment {

    CalenderDialogFragmentBinding binding;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ProgressDialog progressDialog;
    private String calenderDate ;

    public static AddNewTaskCalenderFragment newInstance()
    {
        return new AddNewTaskCalenderFragment();
    }

    public String getCalenderDate() {
        return calenderDate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.CalenderDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = CalenderDialogFragmentBinding.inflate(inflater, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance("https://goodnote-b26ff-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mReference = mDatabase.getReference();
        progressDialog = new ProgressDialog(getContext());

        Bundle args = getArguments();
        String date = args.getString("Date");
        binding.dateEditText.setText(date);
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
                String title = binding.editTextTextPersonName.getText().toString();
                String event = binding.descriptionEditText.getText().toString();
                NewTask newTask = new NewTask(title, event, date,
                        "No time", priority);
                progressDialog.setMessage("Adding task");
                progressDialog.show();
                mReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                        .child("CalenderEvents")
                        .child(title+date+priority)
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
