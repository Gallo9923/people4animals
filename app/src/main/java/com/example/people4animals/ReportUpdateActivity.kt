package com.example.people4animals

import android.annotation.SuppressLint
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.people4animals.application.session.SessionManager
import com.example.people4animals.databinding.ActivityMapBinding
import com.example.people4animals.databinding.ActivityReportUpdateBinding
import com.example.people4animals.domain.user.model.Report
import com.example.people4animals.domain.user.model.ReportStatus
import com.example.people4animals.domain.user.model.User
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ktx.firestore
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

        SessionManager.getInstance(applicationContext).getCurrentUser().let {
            if (it?.id == report.ownerId) {
                binding.doneBtn.visibility = View.VISIBLE
                val status = report.status == ReportStatus.OPEN.toString()

                binding.doneBtn.isEnabled = status
                binding.doneBtn.isClickable = status

                binding.statusTV.text =
                    if (status) getString(R.string.report_status_open) else getString(R.string.report_status_closed)
            }
        }
        binding.doneBtn.setOnClickListener(::closeCase)
    }

    private fun closeCase(view: View?) {
        Firebase.firestore.collection("reports").document(report.id)
            .update("status", ReportStatus.CLOSED).addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Caso cerrado",
                    Toast.LENGTH_LONG
                ).show()
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Error cerrando el caso. por favor intentelo más tarde",
                    Toast.LENGTH_LONG
                ).show()
            }
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
                .title("Animalito")
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