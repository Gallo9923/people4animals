package com.example.people4animals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.people4animals.databinding.ActivityLogInViewBinding
import com.example.people4animals.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}