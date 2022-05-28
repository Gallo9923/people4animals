package com.example.people4animals.utils

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.people4animals.MainActivity
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
            sortedReports = orderAddReport(sortedReports, report)
        }

        sortedReports.forEach {
            Log.e("Lista Resultado = ", it.distance.toString())
        }
        return sortedReports
    }

    private fun orderAddReport(reports:ArrayList<Report>, report: Report): ArrayList<Report> {
        if(reports.size == 0){
            reports.add(report)
        } else {
            var i = 0
            while (reports[i].distance < report.distance){
                i++
            }
            //Log.e("pos = ", i.toString())
            reports.add(i,report)
        }
        //Log.e("Lista = ", reports.toString())

        return reports
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