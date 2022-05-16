package com.example.people4animals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.people4animals.databinding.ActivityNewReportBinding

class NewReportActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNewReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backreportBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}