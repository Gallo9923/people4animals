package com.example.people4animals.domain.user.manager

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.people4animals.R
import com.example.people4animals.domain.user.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


object UserManager {

    fun signUp(context: Context, user: User, password: String): User {
        // Register user in Firebase Auth
        Firebase.auth.createUserWithEmailAndPassword(
            user.username,
            password
        ).addOnSuccessListener {
            // Register user in Firestore
            val authUser = User(
                Firebase.auth.currentUser?.uid!!,
                user.username,
                user.name,
                user.phone,
                user.city
            )

            Firebase.firestore.collection("users").document(authUser.id).set(authUser)
                .addOnSuccessListener {
                    sendVerificationEmail(context)
                    (context as Activity).finish()
                }
        }.addOnFailureListener {
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }

        return user
    }

    private fun sendVerificationEmail(context: Context) {
        Firebase.auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
            Toast.makeText(context, R.string.verify_email, Toast.LENGTH_LONG).show()
        }?.addOnFailureListener {
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }
    }
}