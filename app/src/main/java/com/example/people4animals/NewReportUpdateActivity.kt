package com.example.people4animals

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.people4animals.databinding.ActivityNewReportUpdateBinding
import com.example.people4animals.domain.user.model.Report
import com.example.people4animals.domain.user.model.ReportUpdate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import java.util.*


class NewReportUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewReportUpdateBinding

    private lateinit var report: Report

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    private val uid = Firebase.auth.currentUser?.uid

    private var uri: Uri? = null

    private val updateID = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewReportUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        report = Gson().fromJson((intent.extras?.get("report").toString()), Report::class.java)

        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::onGalleryResult
        )
        binding.photoTaken.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            galleryLauncher.launch(intent)
            true
        }
        binding.imageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            galleryLauncher.launch(intent)
            true
        }
        binding.sendReportBtn.setOnClickListener(::sendReport)
    }

    private fun onGalleryResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            uri = result.data?.data!!
            binding.photoTaken.setImageURI(uri)
        }
    }

    private  fun sendReport(view: View?) {

        uri?.let {
            val reportUpdate = ReportUpdate(
                uid!!,
                binding.reportDescription.text.toString(),
                Date().time,
                updateID,
                updateID
            )
            Firebase.storage.reference.child("reportUpdate").child(updateID).putFile(uri!!)
                .addOnCompleteListener {

                    Firebase.firestore.collection("reports").document(report.id)
                        .collection("updates")
                        .document(updateID).set(reportUpdate)

                    Toast.makeText(this, "Actualización creada", Toast.LENGTH_SHORT).show()

                 /*   Intent(this, ReportUpdateActivity::class.java).apply {
                        putExtra("report", Gson().toJson(report).toString())
                        startActivity(this)
                    }*/

                    finish()
                }

            return
        }
        Toast.makeText(this, "Seleccione una imagen", Toast.LENGTH_SHORT).show()
    }
}