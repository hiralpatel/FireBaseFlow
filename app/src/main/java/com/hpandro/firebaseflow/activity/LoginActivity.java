package com.hpandro.firebaseflow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.hpandro.firebaseflow.utility.Utils;
import com.hpandro.firebaseflow.AppConfig;
import com.hpandro.firebaseflow.R;

public class LoginActivity extends AppCompatActivity {

    EditText edtPass, edtEmail;
    private String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPass = (EditText) findViewById(R.id.edtPass);
    }

    public void openSignUp(View v) {
        finish();
        overridePendingTransition(0, 0);
    }

    public void loginUser(View v) {
        String email = edtEmail.getText().toString();
        String password = edtPass.getText().toString();
        if (!email.trim().isEmpty() &&
                !password.trim().isEmpty()) {
            AppConfig.getAuth().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Utils.user = AppConfig.getAuth().getCurrentUser();
                                if (!Utils.user.isEmailVerified()) {
                                    Toast.makeText(LoginActivity.this, "Please verify email first!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                Log.d(TAG, "Email:" + Utils.user.getEmail());
                                Log.d(TAG, "Uid:" + Utils.user.getUid());

                                Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mIntent);
                                overridePendingTransition(0, 0);
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                Log.e(TAG, "Authentication failed!");
                            }
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this, "Please enter required field!", Toast.LENGTH_LONG).show();
        }
    }

    public void resetPassword(View v) {
        Intent mIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(mIntent);
        overridePendingTransition(0, 0);
    }
}