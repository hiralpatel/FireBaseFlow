package com.hpandro.firebaseflow.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.hpandro.firebaseflow.utility.Utils;
import com.hpandro.firebaseflow.R;

public class MainActivity extends AppCompatActivity {

    private TextView tvDetails;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDetails = (TextView) findViewById(R.id.tvDetails);
        tvDetails.setText("Email: " + Utils.user.getEmail() + "\n" +
                "ID:" + Utils.user.getUid());
    }
}
