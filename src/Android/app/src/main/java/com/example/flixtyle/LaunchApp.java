package com.example.flixtyle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class LaunchApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Handler Handler = new Handler();
        Handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LaunchApp.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}

