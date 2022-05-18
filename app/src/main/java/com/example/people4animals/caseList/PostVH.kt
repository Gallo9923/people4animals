package com.example.people4animals.caseList

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.people4animals.databinding.PostCardBinding


class PostVH(root: View) : RecyclerView.ViewHolder(root){

    private var binding: PostCardBinding = PostCardBinding.bind(root)

    var postTitle = binding.tvTitle
    var postName = binding.tvProfileName
    var postDate = binding.tvDescription
    var postImg = binding.imageView2

}