package com.example.people4animals.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.people4animals.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    var mainActivity : MainActivity? = null
    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)


        /*setupTabs()*/




        return binding.root
    }
/*
    private fun setupTabs(){

        val adapter = VpAdapter(requireActivity().supportFragmentManager)
        adapter.addFragment(GeneralFragment(),"General")
        adapter.addFragment(MyCasesFragment(),"Mis Casos")
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)


    }*/

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object { @JvmStatic fun newInstance() = HomeFragment() }

}
