package com.example.people4animals

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.people4animals.caseList.PostVH
import com.example.people4animals.caseList.updatesList.UpdateVH
import com.example.people4animals.domain.user.model.ReportUpdate
import com.example.people4animals.domain.user.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UpdatesAdapter(reportID: String) : RecyclerView.Adapter<UpdateVH>() {

    private var _reportUpdatesList: MutableLiveData<ArrayList<ReportUpdate>> = MutableLiveData()
    val postList: LiveData<ArrayList<ReportUpdate>>
        get() {
            return _reportUpdatesList
        }

    lateinit var myActivity: MainActivity

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + job)


    init {
        _reportUpdatesList.value = ArrayList<ReportUpdate>()

        CoroutineScope(Dispatchers.IO).launch {
            Firebase.firestore.collection("reports").document(reportID).collection("updates")
                .orderBy("date")
                .addSnapshotListener { // Nos contextualiza en la actividad padre
                        result, error -> // Es necesario pasar estos dos elementos

                    //Elementos que hacer ante el cambio, renderiza los mensajes
                    for (doc in result!!.documents) {
                        val reportUpdate = doc.toObject(ReportUpdate::class.java)!!

                        _reportUpdatesList.value!!.add(reportUpdate)
                        notifyItemInserted(_reportUpdatesList.value!!.size - 1)

                    }
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.case_message_card, parent, false)
        return UpdateVH(view)
    }

    override fun onBindViewHolder(holder: UpdateVH, position: Int) {

        var authorName: String = ""

        _reportUpdatesList.value?.get(position)?.let { reportUpdate ->
            Firebase.firestore.collection("users").document(reportUpdate.authorID).get()
                .addOnSuccessListener {
                    authorName = it.toObject(User::class.java)?.name!!

                }.addOnCompleteListener {
                    Firebase.storage.reference.child("reportUpdate")
                        .child(reportUpdate.photoID).downloadUrl.addOnSuccessListener { profileUrl ->
                            holder.loadData(authorName, profileUrl.toString(), reportUpdate)
                        }
                }
        }
    }

    override fun getItemCount(): Int {
        return _reportUpdatesList.value?.size!!
    }
}