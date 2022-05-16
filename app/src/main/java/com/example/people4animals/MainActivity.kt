package com.example.people4animals

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.people4animals.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment: ViewProfile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        homeFragment = HomeFragment.newInstance()
        profileFragment = ViewProfile.newInstance()

        binding.bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                2131362030 -> showFragment(homeFragment)
                2131362172 -> showFragment(profileFragment)
            }
            true
        }

        binding.fabHome.setOnClickListener {
            startActivity(Intent(this, NewReportActivity::class.java))
        }
    }

    fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainerView.id, fragment).commit()
    }
}