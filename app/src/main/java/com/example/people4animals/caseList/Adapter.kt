package com.example.people4animals.caseList

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.people4animals.MainActivity
import com.example.people4animals.R
import com.example.people4animals.application.session.SessionManager
import com.example.people4animals.domain.user.model.Report
import com.example.people4animals.domain.user.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

class Adapter() : RecyclerView.Adapter<PostVH>() {

    private var _reportList: MutableLiveData<ArrayList<Report>> = MutableLiveData()
    val postList: LiveData<ArrayList<Report>>
        get() {
            return _reportList
        }

    lateinit var myActivity: MainActivity

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + job)


    init {
        _reportList.value = ArrayList<Report>()

        CoroutineScope(Dispatchers.IO).launch {
            Firebase.firestore.collection("reports")
                .orderBy("date")
                .addSnapshotListener { // Nos contextualiza en la actividad padre
                        result, error -> // Es necesario pasar estos dos elementos

                    //Elementos que hacer ante el cambio, renderiza los mensajes
                    for (doc in result!!.documents) {
                        val report = doc.toObject(Report::class.java)!!
                        addPost(report)
                    }
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_card, parent, false)
        return PostVH(view)
    }

    override fun onBindViewHolder(holder: PostVH, position: Int) {

        val userActual = Firebase.firestore.collection("users")
            .document(_reportList.value!![position].ownerId).get().addOnSuccessListener {
                if (it != null) {
                    holder.postUsername.text = it.toObject(User::class.java)!!.name
                }

            }

        holder.postDescription.text = "${_reportList.value!![position].description}"


        var url = "https://cdn.pixabay.com/photo/2017/11/09/21/41/cat-2934720__340.jpg"
        Glide.with(holder.postUsername)
            .load(url)
            .centerCrop().into(holder.postImg)

        uiScope.launch {
            Firebase.storage.reference.child("report")
                .child(_reportList.value!![position].photosIds[0]).downloadUrl.addOnSuccessListener {
                    Glide.with(holder.postImg)
                        .load(it.toString())
                        .centerCrop().into(holder.postImg)
                }

            Firebase.storage.reference.child("profile")
                .child(_reportList.value!![position].ownerId).downloadUrl.addOnSuccessListener {
                    Glide.with(holder.profileImage)
                        .load(it.toString())
                        .centerCrop().into(holder.profileImage)
                }.addOnFailureListener {
                    Glide.with(holder.profileImage)
                        .load(url).circleCrop()
                        .into(holder.profileImage)
                }
        }

        holder.postTitle.text = _reportList.value!![position].title
    }

    override fun getItemCount(): Int {
        return _reportList.value?.size!!
    }

    fun addPost(temp: Report) {
        _reportList.value!!.add(temp)
        notifyItemInserted(_reportList.value!!.size - 1)
    }

    fun setPostList(postList: ArrayList<Report>) {
        _reportList.value = postList
    }

    fun filterByUser() {

        postList.value!!.clear()

        this.notifyDataSetChanged()

        CoroutineScope(Dispatchers.IO).launch {
            Firebase.firestore.collection("reports")
                .orderBy("date")
                .whereEqualTo("ownerId", Firebase.auth.currentUser!!.uid.toString())
                .addSnapshotListener { // Nos contextualiza en la actividad padre
                        result, error -> // Es necesario pasar estos dos elementos

                    //Elementos que hacer ante el cambio, renderiza los mensajes

                    result?.let {
                        for (doc in result!!.documents) {
                            val report = doc.toObject(Report::class.java)!!
                            addPost(report)
                        }
                    }

                }
        }


        /*ar ind: Int = 0
        while (ind < postList.value!!.indices.last) {
            if (postList.value!![0].ownerId != Firebase.auth.currentUser!!.uid) {
                postList.value!!.removeAt(ind)
                notifyItemRemoved(ind)
                ind = 0
            }
            ind++
        }*/
    }

    fun withOutFilter() {
        notifyItemRangeRemoved(0, _reportList.value!!.size)
        _reportList.value!!.clear()

        CoroutineScope(Dispatchers.IO).launch {
            Firebase.firestore.collection("reports")
                .orderBy("date")
                .addSnapshotListener { // Nos contextualiza en la actividad padre
                        result, error -> // Es necesario pasar estos dos elementos

                    //Elementos que hacer ante el cambio, renderiza los mensajes
                    for (doc in result!!.documents) {
                        val report = doc.toObject(Report::class.java)!!
                        addPost(report)
                    }
                }
        }
    }
}