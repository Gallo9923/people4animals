package com.example.people4animals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.example.people4animals.application.session.SessionManager
import com.google.gson.Gson

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        object : CountDownTimer(3000, 1000) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                if (verifyCurrentSession()) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                } else {
                    startActivity(Intent(applicationContext, LogInView::class.java))
                }
            }
        }.start()

    }

    private fun verifyCurrentSession(): Boolean {
        return SessionManager.getInstance(applicationContext).getCurrentUser() != null
    }
}