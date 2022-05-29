package com.example.people4animals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.people4animals.databinding.ActivityNewReportBinding
import com.example.people4animals.databinding.ActivityNewReportUpdateBinding
import com.example.people4animals.domain.user.model.Report
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import java.util.*

class NewReportUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewReportUpdateBinding

    private lateinit var report: Report

    private lateinit var galleryLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewReportUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        report =  Gson().fromJson((intent.extras?.get("report").toString()), Report::class.java)

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onGalleryResult)

        binding.imageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            galleryLauncher.launch(intent)
            true
        }

    }

    private fun onGalleryResult(result: ActivityResult){
        if(result.resultCode == RESULT_OK){
            val uid = Firebase.auth.currentUser?.uid
            val uri = result.data?.data
            binding.photoTaken.setImageURI(uri)

            //upload image to firestorage
            val filename = UUID.randomUUID().toString()
            Firebase.storage.getReference().child("reportUpdate").child(filename).putFile(uri!!)
            // TODO: Set ReportUpdate to report
            //Firebase.firestore.collection("users").document(uid!!).update("photoID", filename)
        }

    }
}