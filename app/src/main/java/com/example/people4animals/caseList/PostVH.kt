package com.example.people4animals.caseList

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.people4animals.databinding.PostCardBinding

class PostVH(root: View) : RecyclerView.ViewHolder(root){
    private var binding: PostCardBinding = PostCardBinding.bind(root)

    var postTitle = binding.tvTitle
    var postUsername = binding.tvProfileName
    var postDescription = binding.tvDescription
    var postImg = binding.imageView2
}