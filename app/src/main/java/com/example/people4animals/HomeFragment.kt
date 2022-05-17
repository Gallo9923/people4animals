package com.example.people4animals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.people4animals.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupTabs()
        return binding.root
    }

    private fun setupTabs(){

        val adapter = VpAdapter(requireActivity().supportFragmentManager)
        adapter.addFragment(GeneralFragment(),"General")
        adapter.addFragment(MyCasesFragment(),"Mis Casos")
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object { @JvmStatic fun newInstance() = HomeFragment() }

}