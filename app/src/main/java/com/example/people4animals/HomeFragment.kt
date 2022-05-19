package com.example.people4animals

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.people4animals.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.people4animals.caseList.Adapter
import com.example.people4animals.databinding.FragmentGeneralBinding
import com.example.people4animals.domain.user.model.Report
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    var mainActivity : MainActivity? = null
    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)


        setupTabs()


        return binding.root
    }

    private fun setupTabs(){

        val adapter = VpAdapter(requireActivity().supportFragmentManager)
        adapter.addFragment(GeneralFragment(),"General")
        adapter.addFragment(MyCasesFragment(),"Mis Casos")
        //binding.viewPager.adapter = adapter
        //binding.tabLayout.setupWithViewPager(binding.viewPager)


    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object { @JvmStatic fun newInstance() = HomeFragment() }

}
