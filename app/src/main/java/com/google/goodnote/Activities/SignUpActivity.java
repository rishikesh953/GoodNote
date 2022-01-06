package com.google.goodnote.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.goodnote.Activities.LoginActivity;
import com.google.goodnote.Activities.MainActivity;
import com.google.goodnote.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    private final String emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+[a-z]{2,4}$";

     ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if(mUser != null)
        {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAuth();
            }
        });



        binding.haveAccTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void performAuth() {

        if(!binding.emailSignUpEditText.getText().toString().matches("[a-z0-9._%+-]+@[a-z0-9.-]+[a-z]{2,4}$"))
        {
            binding.emailSignUpEditText.setError("Invalid Email");
        }
        else if(binding.passwordEditText.getText().toString().isEmpty() || binding.passwordEditText.getText().toString().replace(" ", "").length() <6)
        {
            binding.passwordEditText.setError("Invalid password");
        }
        else if(!binding.passwordEditText.getText().toString().equals(binding.confirmPassword.getText().toString()))
        {
            binding.confirmPassword.setError("Password do not match");
        }
        else
        {
            progressDialog.setTitle("Sign up");
            progressDialog.setMessage("Please wait for a few seconds...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(binding.emailSignUpEditText.getText().toString(),
                    binding.passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Log.v("SignUpActivity","SignUp successful");
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Sign up failed: "+task.getException(),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    private void sendUserToNextActivity() {

        Intent i  = new Intent(getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}