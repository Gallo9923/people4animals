package com.example.people4animals

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.people4animals.application.session.SessionManager
import com.example.people4animals.databinding.ActivityMainBinding
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment: ViewProfile
    private lateinit var generalFragment: GeneralFragment

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        homeFragment = HomeFragment.newInstance()
        generalFragment = GeneralFragment.getInstance()
        homeFragment.mainActivity = this
        profileFragment = ViewProfile.newInstance()

/*
        val bottomBarNavView =  binding.bottomNavView

        val bottomNavigationViewBackground = bottomBarNavView.background as MaterialShapeDrawable
        bottomNavigationViewBackground.shapeAppearanceModel =
            bottomNavigationViewBackground.shapeAppearanceModel.toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, 30f)
                .setTopLeftCorner(CornerFamily.ROUNDED, 30f)
                .build()*/

       /* requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), 10
        )*/


        showFragment(generalFragment)

        binding.bottomNavView.setOnNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.home_menu -> {
                    showFragment(generalFragment)
                    true
                }


                R.id.profile_menu -> {
                    showFragment(profileFragment)

                    true
                }
                else -> {super.onOptionsItemSelected(it)}
            }

        }

        /* binding.profileButtonCV.setOnClickListener {
             showFragment(profileFragment)
         }*/

        binding.fabHome.setOnClickListener {
            startActivity(Intent(this, NewReportActivity::class.java))
        }
     /*   binding.newPostButtonCV.setOnClickListener {
        }*/

        /*  binding.bottomNavView.setOnNavigationItemSelectedListener {
              when (it.toString()) {
                  "Home" -> showFragment(generalFragment)
                  "profile" -> showFragment(profileFragment)
              }
              true
          }

          binding.fabHome.setOnClickListener {
              startActivity(Intent(this, NewReportActivity::class.java))
          }*/
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
        for (result in grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                allGranted = false
                break
            }
        }

        // TODO: Check what to do if no permissions allowed
        if (allGranted) {

        } else {

        }
    }

    fun logOut() {
        SessionManager.getInstance(applicationContext).logOut()
        startActivity(Intent(this, SplashScreen::class.java))
    }
}