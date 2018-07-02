package com.hpandro.firebaseflow;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

public class AppConfig extends Application {

    private static FirebaseAuth mAuth;

    public static FirebaseAuth getAuth() {
        return mAuth;
    }

    @Override
    public void onLowMemory() {
        Runtime.getRuntime().gc();
        super.onLowMemory();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
    }
}