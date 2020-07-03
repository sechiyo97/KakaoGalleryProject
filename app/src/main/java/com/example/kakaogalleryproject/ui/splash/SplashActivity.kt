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
            showMain() // move on to main activity
        }, SPLASH_TIME_OUT.toLong())
    }
    private fun showMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
        overridePendingTransition(0, R.anim.fade_out)
    }
}