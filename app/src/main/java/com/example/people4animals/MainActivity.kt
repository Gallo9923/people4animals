package com.example.people4animals

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.people4animals.application.session.SessionManager
import com.example.people4animals.databinding.ActivityMainBinding
import com.example.people4animals.utils.LocationUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val refreshTime: Long = 100000

    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment: ViewProfile
    private lateinit var generalFragment: GeneralFragment

    // location services
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var userLocation: LatLng

    val locationUtils:LocationUtils = LocationUtils()

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        locationUtils.locationPermissions(this)

        homeFragment = HomeFragment.newInstance()
        generalFragment = GeneralFragment.getInstance()
        homeFragment.mainActivity = this
        profileFragment = ViewProfile.newInstance()

        showFragment(generalFragment)

        // instance of location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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

        // get current or last location

        val locationManager = this?.getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, refreshTime, 1f, ::OnLocationGPSCheange)

        binding.fabHome.setOnClickListener {
            startActivity(Intent(this, NewReportActivity::class.java))
        }
    }

    fun OnLocationGPSCheange(loc: Location){
        userLocation = LatLng(loc.latitude,loc.longitude)
        Log.e("ubicación", userLocation.toString())
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