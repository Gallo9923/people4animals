package com.example.people4animals.utils

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.people4animals.MainActivity
import com.example.people4animals.domain.user.model.Report

class LocationUtils {


    fun generateSortedList(reports:ArrayList<Report>, mainActivity: MainActivity){

    }


    fun locationPermissions(activity: MainActivity){
        val permissionCheck1 = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
        val permissionCheck2 = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED) {
            // ask permissions here using below code
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                10
            )
        }
        if (permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                12
            )
        }
    }
}