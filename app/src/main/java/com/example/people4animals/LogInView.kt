package com.example.people4animals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.people4animals.databinding.ActivityLogInViewBinding

class LogInView : AppCompatActivity() {

    private val binding: ActivityLogInViewBinding by lazy {
        ActivityLogInViewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.logInBtn.setOnClickListener(::login)
        binding.signUpLink.setOnClickListener(::signUp)
    }

    private fun signUp(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun login(view: View) {
        Log.e(">>>>>>>>>>>>>>>", binding.loginEmailET.text.toString())
        startActivity(Intent(this, MainActivity::class.java))
    }
}