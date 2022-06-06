package com.example.people4animals.utils

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.people4animals.ui.MainActivity
import com.example.people4animals.domain.user.model.Report
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class LocationUtils {


    fun generateSortedList(reports:ArrayList<Report>, mainActivity: MainActivity): ArrayList<Report> {
        var sortedReports = ArrayList<Report>()

        reports.forEach {
            report ->
                val c1 = abs(report.latitude - mainActivity.userLocation!!.latitude)
                val c2 = abs(report.longitude - mainActivity.userLocation!!.longitude)
                var dis =  sqrt((c1).toDouble().pow(2)+(c2).toDouble().pow(2))
            report.distance = dis
        }

        sortedReports.forEach {
            Log.e("Sin ordenar", it.distance.toString())
        }

        sortedReports.sortWith(Comparator { report1, report2 ->
            (report1.distance - report2.distance).toInt()
        } )

        sortedReports.forEach {
            Log.e("Ordenado", it.distance.toString())
        }
        return sortedReports
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