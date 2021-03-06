package com.example.people4animals.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.example.people4animals.R
import com.example.people4animals.databinding.ActivityNewReportBinding
import com.example.people4animals.domain.user.model.Report
import com.example.people4animals.domain.user.model.ReportStatus
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*
import kotlin.collections.ArrayList

class NewReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewReportBinding
    private lateinit var imageUri: Uri

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val galleryLauncher = registerForActivityResult(StartActivityForResult(), ::onGalleryResult)

        binding.backreportBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.publishBTN.setOnClickListener {
            createPost()
        }

        binding.addreportimageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            galleryLauncher.launch(intent)
        }

        binding.reportImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            galleryLauncher.launch(intent)
        }

        val mapIntent = Intent(this, MapActivity::class.java).apply {
        }

        val launcher = registerForActivityResult(StartActivityForResult(), ::onMapResult)
        binding.setLocationBtn.setOnClickListener {
            launcher.launch(mapIntent)
        }

    }

    fun onMapResult(result: ActivityResult) {

        if (result.resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "The location was not updated", Toast.LENGTH_SHORT).show()
        } else if (result.resultCode == RESULT_OK) {

            val data = result.data
            val lat = data?.extras?.getString("latitude")?.toDoubleOrNull()
            val lng = data?.extras?.getString("longitude")?.toDoubleOrNull()

            if (lat != null && lng != null) {
                this.latitude = lat
                this.longitude = lng
                Toast.makeText(this, "The location was updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "There was an error updating the location", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun createPost(){
        val report = createReport() ?: return

        if (!(this::imageUri.isInitialized)) {
            Toast.makeText(this, R.string.report_no_image, Toast.LENGTH_SHORT).show()
            return
        }

        val filename = UUID.randomUUID().toString()
        Firebase.storage.reference
            .child("report")
            .child(filename)
            .putFile(this.imageUri) // TODO: Determine if onSuccessListener is needed

        report.photosIds = arrayListOf(filename)

        Firebase.firestore.runBatch  { batch  ->
            val reportRef = Firebase.firestore.collection("reports").document(report.id)
            batch.set(reportRef, report)

            val userId = Firebase.auth.currentUser?.uid.toString()
            val userReport = hashMapOf("id" to report.id)
            val userReportRef = Firebase.firestore.collection("users").document(userId).collection("reports").document(report.id)
            batch.set(userReportRef, userReport)
        }.addOnSuccessListener {
            Toast.makeText(this, R.string.success_report_creation, Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, R.string.report_error_message, Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    private fun createReport(): Report? {

        val title = binding.reporttitleTV.text.toString()
        if (title.isEmpty()) {
            Toast.makeText(this, R.string.report_title_empty, Toast.LENGTH_SHORT).show()
            return null
        }

        val description = binding.reportdescriptionTV.editText!!.text.toString()
        if (description.isEmpty()) {
            Toast.makeText(this, R.string.report_description_empty, Toast.LENGTH_SHORT).show()
            return null
        }

        if (this.latitude.equals(0.0) && this.longitude.equals(0.0)) {
            Toast.makeText(this, R.string.report_no_location, Toast.LENGTH_SHORT).show()
            return null
        }

        return Report(
            UUID.randomUUID().toString(),
            Firebase.auth.currentUser?.uid.toString(),
            ReportStatus.OPEN.toString(),
            Date().time,
            title,
            this.latitude,
            this.longitude,
            description,    
            ArrayList())
    }

    private fun onGalleryResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            this.imageUri = result.data?.data!!
            this.imageUri.let {
                val bitmap: Bitmap =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, this.imageUri)
                val aspectRatio = (bitmap.width.toFloat()) / bitmap.height
                val scaledBitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    (aspectRatio * 300).toInt(),
                    300,
                    true
                )
                binding.reportImage.setImageBitmap(scaledBitmap)
            }
        } else if (result.resultCode == RESULT_CANCELED) {
            // TODO: Check if conditional needed
        }
    }


}