package com.example.people4animals.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.people4animals.R
import com.example.people4animals.application.session.SessionManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        object : CountDownTimer(3000, 1000) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                if (needAuthentication()) {
                    startActivity(Intent(applicationContext, LogInView::class.java))
                } else {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }
                finish()
            }
        }.start()

    }

    private fun needAuthentication(): Boolean {
        val user = SessionManager.getInstance(applicationContext).getCurrentUser()

        return user == null || Firebase.auth.currentUser == null // || Firebase.auth.currentUser?.isEmailVerified == false this isn't the correct place to do this validation
    }
}