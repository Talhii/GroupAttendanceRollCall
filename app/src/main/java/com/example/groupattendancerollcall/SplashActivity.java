package com.example.groupattendancerollcall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        boolean firstTime = sharedPreferences.getBoolean("firstTime", true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firstTime) {
                    Intent intent = new Intent(SplashActivity.this, GetStartedActivity.class);
                    startActivity(intent);
                    SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("firstTime", false);
                    editor.apply();
                } else {
                    Intent in = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(in);
                }
                finish();
            }
        }, 2000);


    }
}