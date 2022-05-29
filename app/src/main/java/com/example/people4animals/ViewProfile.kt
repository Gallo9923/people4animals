package com.example.people4animals

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.example.people4animals.application.session.SessionManager
import com.example.people4animals.databinding.FragmentProfileBinding
import com.example.people4animals.domain.user.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.result.ActivityResult
import com.bumptech.glide.Glide
import com.google.firebase.storage.ktx.storage
import java.util.*
import android.app.Activity.RESULT_OK

class ViewProfile : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var data : Array<String>

    private lateinit var galleryLauncher : ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        galleryLauncher = registerForActivityResult(StartActivityForResult(), ::onGalleryResult)

        data = arrayOf(
            "Bogotá",
            "Cali",
            "Santiago de Cali",
            "Medellín",
            "Barranquilla",
            "Pereira"
        )
        val citiesAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, data)
        binding.spinner.adapter = citiesAdapter
        citiesAdapter.notifyDataSetChanged()

        SessionManager.getInstance(requireContext()).getCurrentUser().let {
            loadInformation()
        }

        updateUser()
        binding.tvSignOut.setOnClickListener{

            (this@ViewProfile.activity as MainActivity).logOut()
        }

        binding.imagenUser.setOnLongClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            galleryLauncher.launch(intent)
            true
        }

        getUpdatedUser()

        return binding.root
    }

    private fun onGalleryResult(result: ActivityResult){
        if(result.resultCode == RESULT_OK){
            val uid = Firebase.auth.currentUser?.uid
            val uri = result.data?.data
            binding.imagenUser.setImageURI(uri)

            //upload image to firestorage
            val filename = UUID.randomUUID().toString()
            Firebase.storage.getReference().child("profile").child(filename).putFile(uri!!)
            Firebase.firestore.collection("users").document(uid!!).update("photoID", filename)
        }

    }
    private fun loadCity(selectedCity : String) : Int{
        var selectedOption = 0
        for(i in data.indices){
            if(data[i] == selectedCity){
                selectedOption = i
                break
            }
        }
        return selectedOption
    }

    private fun loadInformation() {

        val userDoc = Firebase.firestore
            .collection("users")
            .whereEqualTo("id", Firebase.auth.currentUser?.uid!!)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null && !snapshot.isEmpty) {
                    snapshot.forEach {
                        val user = it.toObject(User::class.java)
                        binding.phone.setText(user.phone)
                        binding.nameUser.setText(user.username)
                        binding.email.setText(user.username)
                        binding.spinner.setSelection(loadCity(user.city))
                    }
                }
            }
    }

    private fun updateUser() {
        binding.btnUpdateProfile.setOnClickListener {
            val uid = Firebase.auth.currentUser?.uid
            var user = User(
                id = uid!!,
                name = binding.nameUser.text.toString(),
                username = SessionManager.getInstance(requireContext()).getCurrentUser()!!.username,
                phone = binding.phone.text.toString(),
                city = binding.spinner.selectedItem.toString()
            )
            Firebase.firestore.collection("users").document(uid).set(user)
        }
    }

    fun getUpdatedUser(){
        val uid = Firebase.auth.currentUser?.uid
        Firebase.firestore.collection("users").document(uid!!).get().addOnSuccessListener {
            val updatedUser = it.toObject(User::class.java)
            val photoID = updatedUser?.photoID
            downloadImage(photoID)
        }
    }

    fun downloadImage(photoID:String?){

        if(photoID=="") return

        Firebase.storage.getReference().child("profile").child(photoID!!).downloadUrl.addOnSuccessListener {
            //Log.e(">>>",it.toString())
            Glide.with(binding.imagenUser).load(it).into(binding.imagenUser)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ViewProfile()
    }
}