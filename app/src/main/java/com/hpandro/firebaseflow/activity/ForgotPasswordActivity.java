package com.hpandro.firebaseflow.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hpandro.firebaseflow.AppConfig;
import com.hpandro.firebaseflow.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
    }

    public void resetPassword(View v) {
        String email = edtEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(ForgotPasswordActivity.this, "Please enter required filed!", Toast.LENGTH_LONG).show();
            return;
        }
        AppConfig.getAuth().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Please check email to reset!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Something went wrong!\nPlease try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}