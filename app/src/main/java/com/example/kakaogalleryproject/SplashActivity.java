package com.example.kakaogalleryproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
    Handler handler = new Handler();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                showMain();
            }
        }, 100);
    }
    public void showMain(){
        Intent intent = new Intent(getBaseContext(), MainActivity2.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, R.anim.fadeout);
    }
}
