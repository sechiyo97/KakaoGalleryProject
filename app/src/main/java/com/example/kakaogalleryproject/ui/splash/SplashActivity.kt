package com.example.kakaogalleryproject.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.kakaogalleryproject.R
import com.example.kakaogalleryproject.ui.main.MainActivity

class SplashActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashTimeOut = 1000 // show splash for 2s
        Handler().postDelayed({
            showMain() // return to main actiivty
        }, splashTimeOut.toLong())
    }
    private fun showMain(){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
        overridePendingTransition(0, R.anim.fade_out)
    }
}