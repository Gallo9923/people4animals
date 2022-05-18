package com.example.people4animals

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.people4animals.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding : ActivityMapBinding

    private lateinit var mMap: GoogleMap

    private lateinit var manager : LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manager = getSystemService(LOCATION_SERVICE) as LocationManager

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.returnBtn.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }

        binding.applyBtn.setOnClickListener {
            val intent = Intent().apply{
                putExtra("latitude", mMap.cameraPosition.target.latitude.toString())
                putExtra("longitude", mMap.cameraPosition.target.longitude.toString())
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setInitialPos()

        mMap.cameraPosition.target
    }

    @SuppressLint("MissingPermission")
    fun setInitialPos(){
        val pos = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (pos != null){
            moveToMarker(pos.latitude, pos.longitude)
        }
    }

    fun moveToMarker(lat: Double, lng: Double) {
        val pos = LatLng(lat, lng)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 19.0F))
    }
}