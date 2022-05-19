package com.example.people4animals.caseList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.people4animals.MainActivity
import com.example.people4animals.R
import com.example.people4animals.domain.user.model.Report
import kotlin.collections.ArrayList

class Adapter() : RecyclerView.Adapter<PostVH>() {

    private var _reportList: MutableLiveData<ArrayList<Report>> = MutableLiveData()
    val postList: LiveData<ArrayList<Report>>
        get() {
            return _reportList
        }

    lateinit var myActivity: MainActivity

    init {
        _reportList.value = ArrayList<Report>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_card, parent, false)
        return PostVH(view)
    }

    override fun onBindViewHolder(holder: PostVH, position: Int) {

        holder.postDescription.text = "${_reportList.value!![position].description}"
        holder.postUsername.text = "Pepito Perez"

        Glide.with(holder.postUsername)
            .load("https://cdn.pixabay.com/photo/2017/11/09/21/41/cat-2934720__340.jpg")
            .centerCrop().into(holder.postImg)

        holder.postTitle.text = _reportList.value!![position].title


    }

    override fun getItemCount(): Int {
        return _reportList.value?.size!!
    }

    fun addPost(temp: Report) {
        _reportList.value!!.add(temp)
        notifyItemInserted(_reportList.value!!.size - 1)
    }

    fun setPostList (postList: ArrayList<Report>){
        _reportList.value = postList
    }
}