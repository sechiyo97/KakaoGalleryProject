package com.example.kakaogalleryproject.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.kakaogalleryproject.R
import com.example.kakaogalleryproject.constants.SPLASH_TIME_OUT
import com.example.kakaogalleryproject.ui.main.MainActivity

class SplashActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            finish() // move on to main activity
            overridePendingTransition(0, R.anim.fade_out)
        }, SPLASH_TIME_OUT.toLong())
    }
}