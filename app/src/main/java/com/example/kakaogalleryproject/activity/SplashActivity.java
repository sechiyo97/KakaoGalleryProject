package com.example.kakaogalleryproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.kakaogalleryproject.R;

public class SplashActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int SPLASH_TIME_OUT = 2000;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                finish();
                overridePendingTransition(0, R.anim.fade_out);
            }
        }, SPLASH_TIME_OUT);
    }
}
