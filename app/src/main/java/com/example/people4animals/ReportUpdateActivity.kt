package com.example.people4animals

import android.annotation.SuppressLint
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.people4animals.databinding.ActivityMapBinding
import com.example.people4animals.databinding.ActivityReportUpdateBinding
import com.example.people4animals.domain.user.model.Report
import com.example.people4animals.domain.user.model.User
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson

class ReportUpdateActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var report: Report

    private lateinit var binding: ActivityReportUpdateBinding

    private lateinit var mMap: GoogleMap

    private lateinit var manager: LocationManager

    private lateinit var updatesAdapter: UpdatesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        report = Gson().fromJson((intent.extras?.get("report").toString()), Report::class.java)

        updatesAdapter = UpdatesAdapter(report.id)

        binding.caseStatusRecyclerView.adapter = updatesAdapter

        binding.caseStatusRecyclerView.layoutManager = LinearLayoutManager(this)

        manager = getSystemService(LOCATION_SERVICE) as LocationManager

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView2) as SupportMapFragment
        mapFragment.getMapAsync(this)


        if (!report.photosIds.isEmpty()) {
            downloadImage(report.photosIds[0])
        }

        binding.textView3.text = report.description

        binding.goToCreateUpdateField.setOnClickListener {
            val intent = Intent(this, NewReportUpdateActivity::class.java).apply {
                putExtra("report", Gson().toJson(report))
            }
            startActivity(intent)
            finish()
        }

        loadUpdates()

    }

    private fun loadUpdates() {

        //Poblamos el rv  caseStatusRecyclerView

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setInitialPos()

        val animalPos = LatLng(report.latitude, report.longitude)
        googleMap.addMarker(
            MarkerOptions()
                .position(animalPos)
                .title("Animalito")
        )

        mMap.cameraPosition.target
    }

    @SuppressLint("MissingPermission")
    fun setInitialPos() {
        val pos = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (pos != null) {
            moveToMarker(pos.latitude, pos.longitude)
        }
    }

    fun moveToMarker(lat: Double, lng: Double) {
        val pos = LatLng(lat, lng)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15.0F))

        mMap.addMarker(
            MarkerOptions()
                .position(pos)
                .title("Tú")
        )
    }

    fun downloadImage(photoID: String) {

        if (photoID == "") return

        Firebase.storage.getReference().child("report")
            .child(photoID!!).downloadUrl.addOnSuccessListener {
                //Log.e(">>>",it.toString())
                Glide.with(binding.imageView4).load(it).into(binding.imageView4)
            }
    }
}