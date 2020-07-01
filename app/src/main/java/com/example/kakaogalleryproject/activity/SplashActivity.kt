package com.example.kakaogalleryproject.activity

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import com.example.kakaogalleryproject.R

class SplashActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val splashTimeOut = 2000
        Handler().postDelayed({
            finish()
            overridePendingTransition(0, R.anim.fade_out)
        }, splashTimeOut.toLong())
    }
}