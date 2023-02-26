package com.example.patelbike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.patelbike.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    ActivityForgotPasswordBinding binding;
    ProgressDialog dialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(ForgotPasswordActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");

        binding.buttonResetPassword.setOnClickListener(view -> {
            forgotPassword();
        });
    }

    private Boolean validateEmail() {
        String val = binding.editTextResetEmail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            binding.editTextResetEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            binding.editTextResetEmail.setError("Invalid email address");
            return false;

        } else {
            binding.editTextResetEmail.setError(null);
            return true;
        }
    }


    private void forgotPassword() {
        if (!validateEmail())
        {
            return;
        }
        dialog.show();

        auth.sendPasswordResetEmail(binding.editTextResetEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                if (task.isSuccessful())
                {
                    startActivity(new Intent(ForgotPasswordActivity.this,MainActivity.class));
                    finish();
                    Toast.makeText(ForgotPasswordActivity.this, "Please check your email ", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ForgotPasswordActivity.this, "Enter valid email address", Toast.LENGTH_SHORT).show();
                }

            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPasswordActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}