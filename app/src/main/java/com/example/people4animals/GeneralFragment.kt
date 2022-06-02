package com.example.people4animals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.people4animals.caseList.Adapter
import com.example.people4animals.databinding.FragmentGeneralBinding
import com.google.android.material.tabs.TabLayout


class GeneralFragment : Fragment() {

    lateinit var binding: FragmentGeneralBinding
    val adapter = Adapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGeneralBinding.inflate(layoutInflater, container, false)
        binding.rvPost.adapter = adapter
        binding.rvPost.layoutManager = LinearLayoutManager(context)

        binding.swipeRvPost.setOnRefreshListener {

            (activity as MainActivity).getReportsList(adapter)
            binding.swipeRvPost.isRefreshing = false
        }

        //binding.allReports.setOnClickListener { adapter.withOutFilter() }
        //binding.myReports.setOnClickListener { adapter.filterByUser() }


        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(adapter.myActivity.userLocation != null){
                    when(tab!!.text.toString()){
                        "General" -> (activity as MainActivity).getReportsList(adapter)
                        "Tus Casos" ->(activity as MainActivity).getUserReports(adapter)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }


        })

        return binding.root
    }

    companion object {
        fun getInstance() = GeneralFragment()
    }

    override fun onStart() {
        super.onStart()

        binding.rvPost.recycledViewPool.clear()
        adapter.notifyDataSetChanged()
    }
}