package com.example.people4animals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.people4animals.application.session.SessionManager
import com.example.people4animals.databinding.ActivityLogInViewBinding
import com.example.people4animals.domain.user.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LogInView : AppCompatActivity() {

    private val binding: ActivityLogInViewBinding by lazy {
        ActivityLogInViewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.logInBtn.setOnClickListener(::login)
        binding.signUpLink.setOnClickListener(::signUp)
    }

    private fun signUp(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun login(view: View) {

        if(binding.loginEmailETChild.text.toString() != "" && binding.loginPassETChild.text.toString() != "" ){
            val email = binding.loginEmailETChild.text.toString()
            val password = binding.loginPassETChild.text.toString()

            Firebase.auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val fbUser = Firebase.auth.currentUser

                Firebase.firestore.collection("users").document(fbUser!!.uid).get()
                    .addOnSuccessListener {
                        val user = it.toObject(User::class.java)
                        SessionManager.getInstance(applicationContext).setCurrentUser(user!!)
                    }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }

    }
}