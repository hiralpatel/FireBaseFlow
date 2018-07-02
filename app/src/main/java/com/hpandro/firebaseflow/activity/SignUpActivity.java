package com.hpandro.firebaseflow.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.hpandro.firebaseflow.AppConfig;
import com.hpandro.firebaseflow.R;
import com.hpandro.firebaseflow.utility.Utils;

public class SignUpActivity extends AppCompatActivity {

    EditText edtPass, edtEmail;
    private String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPass = (EditText) findViewById(R.id.edtPass);
    }

    public void openSignIn(View v) {
        Intent mIntent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(mIntent);
        overridePendingTransition(0, 0);
    }

    public void signUpNewUser(View v) {
        String email = edtEmail.getText().toString();
        String password = edtPass.getText().toString();
        if (!email.trim().isEmpty() &&
                !password.trim().isEmpty()) {
            AppConfig.getAuth().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Utils.user = AppConfig.getAuth().getCurrentUser();
                                Log.d(TAG, "Email:" + Utils.user.getEmail());
                                Log.d(TAG, "Uid:" + Utils.user.getUid());
                                sendVerification();
                            } else {
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                Log.e(TAG, "create Account: Fail!", task.getException());
                            }
                        }
                    });
        } else {
            Toast.makeText(SignUpActivity.this, "Please enter required field!", Toast.LENGTH_LONG).show();
        }
    }

    public void sendVerification() {
        final FirebaseUser user = AppConfig.getAuth().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Please check email to verify Account!", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Verification email sent to " + user.getEmail());
                        } else {
                            Log.e(TAG, "sendEmailVerification failed!", task.getException());
                        }
                    }
                });
    }
}