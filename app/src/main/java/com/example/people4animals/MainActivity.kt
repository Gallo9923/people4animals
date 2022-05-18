package com.example.people4animals

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        homeFragment = HomeFragment.newInstance()
        profileFragment = ViewProfile.newInstance()

        requestPermissions(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ), 10)


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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var allGranted = true
        for(result in grantResults){
            if(result == PackageManager.PERMISSION_DENIED){
                allGranted = false
                break
            }
        }

        // TODO: Check what to do if no permissions allowed
        if(allGranted){

        }else{

        }
    }
}