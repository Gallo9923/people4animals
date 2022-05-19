package com.example.people4animals

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.people4animals.caseList.Adapter
import com.example.people4animals.databinding.FragmentGeneralBinding
import com.example.people4animals.databinding.FragmentHomeBinding
import com.example.people4animals.domain.user.model.Report
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class GeneralFragment : Fragment() {

    lateinit var binding : FragmentGeneralBinding
    private val adapter = Adapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGeneralBinding.inflate(layoutInflater, container, false)
        binding.rvPost.adapter = adapter
        binding.rvPost.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch(Dispatchers.IO){
            Firebase.firestore.collection("reports")
                .orderBy("date").addSnapshotListener(activity as Activity){ // Nos contextualiza en la actividad padre
                        result,error-> // Es necesario pasar estos dos elementos

                    //Elementos que hacer ante el cambio, renderiza los mensajes
                    for (doc in result!!.documents){
                        val report = doc.toObject(Report::class.java)!!
                        Log.e("ENTRA","___________________")
                        adapter.addPost(report)
                        Log.e("ENTRA",adapter.postList.value!!.toString())
                    }
                }
        }

        return binding.root
    }

    companion object{
        fun getInstance() = GeneralFragment()
    }
}