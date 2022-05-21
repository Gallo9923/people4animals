package com.example.people4animals

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.people4animals.application.session.SessionManager
import com.example.people4animals.databinding.FragmentProfileBinding
import com.example.people4animals.domain.user.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewProfile : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val data = arrayOf(
            "Bogotá",
            "Cali",
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

        return binding.root
    }

    private fun loadInformation() {

        val userDoc = Firebase.firestore
            .collection("users")
            .whereEqualTo("id", Firebase.auth.currentUser?.uid!!)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null && !snapshot.isEmpty) {
                    snapshot.forEach {
                        val user = it.toObject(User::class.java)
                       // binding.nameUser.editText.text = user.namesetText(user.username)
                        //binding.phone.setText(user.phone)
                        //binding.lastname.setText(user.name)
                    }
                }
            }
    }

    private fun updateUser() {
        binding.btnUpdateProfile.setOnClickListener {
            val uid = Firebase.auth.currentUser?.uid
            var user = User(
                id = uid!!,
                //name = binding.nameUser.text.toString(),
                username = SessionManager.getInstance(requireContext()).getCurrentUser()!!.username,
                //phone = binding.phone.text.toString(),
                city = binding.spinner.selectedItem.toString()
            )
            Firebase.firestore.collection("users").document(uid).set(user)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = ViewProfile()
    }
}