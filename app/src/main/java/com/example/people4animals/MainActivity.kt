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
import com.example.people4animals.caseList.Adapter
import com.example.people4animals.databinding.ActivityMainBinding
import com.example.people4animals.domain.user.model.Report
import com.example.people4animals.utils.LocationUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.ArrayList
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


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
    var userLocation: LatLng? = null

    val locationUtils: LocationUtils = LocationUtils()

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        locationUtils.locationPermissions(this)

        homeFragment = HomeFragment.newInstance()
        generalFragment = GeneralFragment.getInstance()
        generalFragment.adapter.myActivity = this
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
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }

        }

        // get current or last location
        val locationManager = this?.getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            refreshTime,
            1f,
            ::OnLocationGPSCheange
        )

        binding.fabHome.setOnClickListener {
            startActivity(Intent(this, NewReportActivity::class.java))
        }
    }

    fun OnLocationGPSCheange(loc: Location) {
        userLocation = LatLng(loc.latitude, loc.longitude)
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

    fun getReportsList(adapter: Adapter) {
        CoroutineScope(Dispatchers.IO).launch {
            adapter.postList.value!!.clear()
            withContext(Dispatchers.Main) {
                adapter.notifyItemRangeRemoved(0, adapter.postList.value!!.size)
            }

            val result = Firebase.firestore.collection("reports")
                .orderBy("date")
                .get().await()
            val temporalList = ArrayList<Report>()
            Log.e("Results ::::: ", result!!.documents.size.toString())

            for (doc in result!!.documents) {
                val report = doc.toObject(Report::class.java)!!
                withContext(Dispatchers.Main) {
                    temporalList.add(report)
                }
            }
            Log.e("Sin ordenar", temporalList.size.toString())

            temporalList.forEach {
                        report ->
                    val c1 = abs(report.latitude - userLocation!!.latitude)
                    val c2 = abs(report.longitude - userLocation!!.longitude)
                    var dis =  sqrt((c1).toDouble().pow(2)+(c2).toDouble().pow(2))
                    report.distance = dis
                }
            temporalList.sortWith(Comparator { report1, report2 ->
                (report1.distance - report2.distance).toInt()
            } )

            Log.e("Ordenada", temporalList.size.toString())

            withContext(Dispatchers.Main) {
                temporalList.forEach {
                    adapter.addPost(it)
                }
            }

        }
    }
}