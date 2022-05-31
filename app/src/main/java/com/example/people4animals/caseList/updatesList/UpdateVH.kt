package com.example.people4animals.caseList.updatesList

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.people4animals.databinding.CaseMessageCardBinding
import com.example.people4animals.databinding.PostCardBinding
import com.example.people4animals.domain.user.model.ReportUpdate
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch

class UpdateVH(root: View) : RecyclerView.ViewHolder(root) {
    private var binding: CaseMessageCardBinding = CaseMessageCardBinding.bind(root)

    private val username = binding.username
    private val description = binding.description
    private val updateImage = binding.updateImage


    fun loadData(authorName: String, url: String, update: ReportUpdate) {
        username.text = authorName
        description.text = update.content

        Glide.with(updateImage).load(url).into(updateImage)
    }
}